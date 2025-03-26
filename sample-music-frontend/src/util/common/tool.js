/*
import {mapActions} from "vuex";

export default {
    data() {
        return {
            lyricsLoaded: false,
        };
    },
    computed: {
        watchIsPlay: {
            get() {
                return this.isPlay;
            },
            set(value) {
                if (this.audio) {
                    if (value) {
                        this.audio.play()
                    } else {
                        this.audio.pause();
                    }
                }
            }
        }
    },
    methods: {
        ...mapActions(['getSongById']),
        // 获取歌曲
        /!*playSong(id) {
            this.isLoading = true;
            this.lyricsLoaded = false;
            this.$store.dispatch('getSongById', id)
                .then(() => {
                    // getSongById action 完成后的逻辑
                    this.playAudio();
                    this.isLoading = false;
                })
                .catch((error) => {
                    // 处理可能发生的错误
                    console.error('Error playing song:', error);
                    this.isLoading = false;
                })
                .finally(() => {
                    // 无论成功或失败都会执行的逻辑
                    this.isLoading = false;
                });
        },*!/
        // 播放音频
        playAudio() {
            // 销毁之前播放的音频并移除事件监听器
            this.initPlayState()

            // 新建音频并添加事件监听器
            this.setAudio(new Audio());
            this.audio.src = this.songPlaying.api;
            this.audio.play()
                .then(() => {
                    // 设置为正在播放
                    this.setIsPlay(true);
                    // 设置当前歌曲时长
                    this.setDuration(this.audio.duration)
                    this.audio.currentTime = this.currentTime
                    // 设置播放模式
                    this.audio.addEventListener('ended', this.handleAudioEnd);
                    // 随着歌曲播放，而更新时间
                    this.audio.addEventListener('timeupdate', this.handleAudioTimeUpdate);
                })
                .catch((error) => {
                    console.error('Error playing audio:', error);
                    this.setIsPlay(false);
                });
            this.setAudio(this.audio);
        },
        // 音频播放时间更新时调用的方法
        handleAudioTimeUpdate() {
            // 解析歌词文件
            this.parseLyric(this.songPlaying.lyric).then(data => {
                // 保存全部歌词及其时间戳到songLyric数组
                this.setSongLyric(data.lyricsWithTimestamp.map(item => (item.lyricitem.timestamp)));
                this.setTimestamp(data.lyricsWithTimestamp.map(item => item.timestamp));

                const currentTime = this.audio.currentTime;
                // 获取当前播放时间
                this.setCurrentTime(currentTime);

                // 根据当前播放时间找到对应的歌词
                let currentLyric = null;
                for (let i = data.lyricsWithTimestamp.length - 1; i >= 0; i--) {
                    if (data.lyricsWithTimestamp[i].timestamp <= currentTime+0.5) {
                        currentLyric = data.lyricsWithTimestamp[i].lyric;
                        break;
                    }
                }

                // 更新当前播放的歌词
                if (currentLyric) {
                    this.setCurrentLyric(currentLyric);
                }
            }).catch(error => {
                console.error('Error parsing lyric:', error);
            });
        },
        // 解析歌词文件的方法
        parseLyric(api) {
            return new Promise((resolve, reject) => {
                fetch(api)
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok: ' + response.statusText);
                        }
                        return response.text();
                    })
                    .then(data => {
                        const lines = data.split('\n');
                        let lyricsWithTimestamp = [];

                        lines.forEach(line => {
                            if (line.startsWith('[')) {
                                const timestampWith = line.substring(1, line.indexOf(']'));
                                const timestamp = this.convertTimeFormat(timestampWith);
                                const lyricLine = line.substring(line.indexOf(']') + 1).trim();
                                lyricsWithTimestamp.push({ timestamp, lyric: lyricLine });
                            }
                        });

                        resolve({
                            lyricsWithTimestamp
                        });
                    })
                    .catch(error => {
                        reject(new Error('Request error: ' + error.message));
                    });
            });
        },
        // 时间转换方法
        convertTimeFormat(timeString) {
            if (!timeString) return 0.0; // 检查 timeString 是否存在，如果不存在返回 0.0

            const [minSec, milli] = timeString.split('.');
            const [min, sec] = minSec.split(':').map(Number);

            // 确保 milli 存在，如果不存在则设置为 "000"
            const milliseconds = milli ? milli.padEnd(3, '0') : "000";

            // 计算总秒数
            const totalSeconds = min * 60 + sec + parseFloat(`.${milliseconds}`);
            return parseFloat(totalSeconds.toFixed(3)); // 将结果格式化为6位小数
        },
        // 开始播放
        play() {
            if (!this.audio) {
                this.playAudio();
            } else {
                this.audio.play()
                    .then(() => {
                        this.setIsPlay(true);
                    })
                    .catch(error => {
                        console.error('播放音频时出错:', error);
                        this.setIsPlay(false);
                    });
            }

        },
        // 暂停歌曲
        pause() {
            this.audio.pause();
            this.setIsPlay(false);
        },
        // 暂停或继续播放歌曲
        PlayOrPause() {
            if (this.isPlay) {
                this.pause()
            } else {
                this.play()
            }
        },
        // 切换播放模式
        togglePlayMode() {
            switch (this.playMode) {
                case 'loop':
                    this.setPlayMode('single');
                    break;
                case 'single':
                    this.setPlayMode('random');
                    break;
                case 'random':
                    this.setPlayMode('loop');
                    break;
                default:
                    this.setPlayMode('loop');
            }
        },
        // 根据播放模式设置播放结束时的事件监听器
        handleAudioEnd() {
            // 根据 mode_play 的值来决定播放逻辑
            switch (this.playMode) {
                case 'loop':
                    this.playNext();
                    break;
                case 'single':
                    this.playSingle();
                    break;
                case 'random':
                    this.playRandom();
                    break;
                default:
                    // 如果 mode_play 没有设置，就默认播放下一首
                    this.playNext();
                    break;
            }
        },
        // 播放上一首
        playPrevious() {
            if (this.songIndex > 0) {
                this.setSongIndex(this.songIndex - 1);
            } else {
                this.setSongIndex(this.playlistPlaying.length - 1);
            }
            this.setSongIndex(this.playlistPlaying[this.songIndex]);
            this.playAudio();
        },
        // 播放下一首
        playNext() {
            this.setCurrentLyric([]);
            this.setSongLyric([]);
            this.setTimestamp([]);
            if (this.songIndex < this.playlistPlaying.length - 1) {
                this.setSongIndex(this.songIndex + 1);
            } else {
                this.setSongIndex(0);
            }
            this.setSongPlaying(this.playlistPlaying[this.songIndex]);
            this.playAudio();
        },
        // 随机播放
        playRandom() {
            this.setSongIndex(Math.floor(Math.random() * this.playlistPlaying.length));
            this.setSongPlaying(this.playlistPlaying[this.songIndex]);
            this.playAudio();
        },
        // 单曲循环
        playSingle() {
            this.setSongPlaying(this.playlistPlaying[this.songIndex]);
            this.playAudio();
        },
        // 初始化播放状态
        initPlayState() {
            // 刷新页面暂停播放
            if (this.audio) {
                this.audio.pause();
                this.audio.src = '';
                this.setAudio(null);
            }
        },
    },
};*/
