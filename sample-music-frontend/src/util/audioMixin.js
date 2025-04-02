import {mapGetters} from "vuex";
import ACCESS_ENUM from "@/util/access/accessEnum";
import Vue from "vue";
import Icon from "@/util/common/Icon";
import songService from "@/api/service/SongService";
import userService from "@/api/service/userService";

export default {
    data() {
        return {
            lyricsLoaded: false, // 歌词是否已加载
            volume: 50, // 声音大小，默认一半
            expAddedTime: 0, // 播放时间
            hasAddedExp: false, // 是否已增加过经验
            lastProcessedSecond: 0, //
            trackStartTime: null,   // 记录歌曲开始播放的时间戳（毫秒）
            currentTrackDuration: 0 // 当前歌曲已统计的时长（秒）
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
        },
        returnPlayIcon() {
            if (this.playMode === 'loop') return Icon.loopIcon
            else if (this.playMode === 'random') return Icon.randomIcon
            else if (this.playMode === 'single') return Icon.singleIcon
            else return Icon.loopIcon
        },
        ...mapGetters({getSongMode: 'music/getSongMode'})
    },
    watch: {},
    methods: {
        // 获取歌曲并播放音频·
        playBySong(song) {
            this.setIsLoading(true);
            this.lyricsLoaded = false;
            const songIndex = this.playlistPlaying.findIndex(s => s.id === song.id);
            if (songIndex === -1) {
                // 如果歌曲不在播放列表中,则添加到播放列表的末尾
                this.setPlaylistPlaying([...this.playlistPlaying, song])
                this.setSongIndex(this.playlistPlaying.length - 1)
            } else {
                // 如果歌曲已在播放列表中,则移动到播放列表的末尾
                const updatedPlaylist = [
                    ...this.playlistPlaying.slice(0, songIndex),
                    ...this.playlistPlaying.slice(songIndex + 1),
                    song
                ];
                this.setPlaylistPlaying(updatedPlaylist);
                this.setSongIndex(updatedPlaylist.length - 1);
            }
            this.setSongPlaying(song)
            this.playAudio();
            this.setIsLoading(false);
        },
        // 播放音频
        playAudio() {
            if (this.songPlaying.permission !== 0 && this.userDetail.role !== ACCESS_ENUM.ADMIN) {
                Vue.prototype.$message("暂无权限")
                return;
            }
            // 销毁之前播放的音频并移除事件监听器
            this.initPlayState()

            // 新建音频并添加事件监听器
            this.setAudio(new Audio());
            this.audio.src = this.getSongMode === 'flac' ? this.songPlaying.flacUrl : this.songPlaying.mp3Url;
            this.audio.volume = this.volume / 100;
            this.audio.play()
                .then(() => {
                    // 设置为正在播放
                    this.setIsPlay(true);
                    // 设置当前歌曲时长
                    this.setSongDuration(this.audio.duration)
                    this.audio.currentTime = this.currentTime
                    // 设置播放模式
                    this.audio.addEventListener('ended', this.handleAudioEnd);
                    // 随着歌曲播放，而更新时间
                    this.audio.addEventListener('timeupdate', this.handleAudioTimeUpdate);
                    this.fetchAndParseLyric();
                })
                .catch((error) => {
                    this.$message("播放歌曲失败")
                    console.error('播放歌曲失败:', error);
                    this.setIsPlay(false);
                });
            this.setAudio(this.audio);
        },
        // 音频播放时间更新时调用的方法
        handleAudioTimeUpdate() {
            console.log(this.expAddedTime)
            const currentTime = this.audio.currentTime;
            let currentLyric = null;
            this.setCurrentTime(currentTime);

            // 新增：经验时间处理逻辑
            if (this.audio && !this.audio.paused) {
                this.expAddedTime += 1;
                if(this.expAddedTime === 30) {
                    // 用户听歌经验+30
                    userService.updateUserExp(30).then();
                    // 歌曲收听数+1
                    songService.listenersPlusOne(this.songPlaying.id).then();
                }
            }

            // 在歌词数组中查找当前时间对应的歌词
            for (let i = this.songLyric.length - 1; i >= 0; i--) {
                if (this.Timestamp[i] <= currentTime + 0.5) {
                    currentLyric = this.songLyric[i];
                    break;
                }
            }
            // 如果找到了当前歌词，更新当前歌词变量
            if (currentLyric) {
                this.setCurrentLyric(currentLyric);
            }
        },
        // 请求并解析歌词文件
        fetchAndParseLyric() {
            const lyricsUrl = this.songPlaying.lyric; // 假设这是歌词文件的URL
            this.parseLyric(lyricsUrl).then(data => {
                const temp = data.lyricsWithTimestamp;
                temp.forEach(lyricObj => {
                    if (lyricObj.lyric !== '') {
                        this.setSongLyric([...this.songLyric, lyricObj.lyric]);
                        this.setTimestamp([...this.Timestamp, lyricObj.timestamp]);
                    }
                })
                this.lyricsLoaded = true;
                // 初始化当前播放的歌词
                if (this.songLyric.length > 0) {
                    this.setCurrentLyric(this.songLyric[0].lyric);
                }
            }).catch(error => {
                console.error('Error parsing lyric:', error);
            });
        },
        // 解析歌词文件的方法
        parseLyric(url) {
            return new Promise((resolve, reject) => {
                fetch(url)
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
                                lyricsWithTimestamp.push({timestamp, lyric: lyricLine});
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
        // 改变播放歌曲格式
        toggleSongMode() {
            if (this.songMode === "mp3" && this.songPlaying.flacUrl) {
                this.setSongMode("flac")
                this.playAudio();
            } else if (this.songMode === "flac" && this.songPlaying.mp3Url) {
                this.setSongMode("mp3");
                this.playAudio();
            } else alert("暂无另一格式歌曲");
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
            this.setSongPlaying(this.playlistPlaying[this.songIndex]);
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
            this.setCurrentTime(0)
            this.setTimestamp([]);
            this.setSongLyric([]);
        },
        // 播放选中歌曲
        playSelectSongs() {

        },
        // 播放全部歌曲
        async playAllSongs(type, items) {
            if (items.songs && items.songs.length > 0) {
                this.setPlaylistPlaying(items.songs);
                this.setSongIndex(0);
                this.setCurrentTime(0)
                this.setSongPlaying(this.playlistPlaying[this.songIndex]);
                this.playAudio()
            } else {
                this.$message("未选中歌曲")
            }
        },
        // 改变声音大小
        changeVolume() {
            this.audio.volume = this.volume / 100;
        },
    },

    // 上报播放时长
    logTrackDuration() {
        if (!this.trackStartTime || !this.songPlaying) return;

        // 计算已播放时长（秒）
        const durationSec = Math.floor((Date.now() - this.trackStartTime) / 1000);

        // 避免上报无效值（如瞬间切歌）
        if (durationSec < 1) return;

        // 调用API上报数据（示例接口）
        songService.logPlayDuration({
            songId: this.songPlaying.id,
            duration: durationSec
        }).then(() => {
            console.log('播放时长上报成功');
        }).catch(error => {
            console.error('上报失败:', error);
        });

        // 重置计时状态
        this.trackStartTime = null;
        this.currentTrackDuration = 0;
    },
};