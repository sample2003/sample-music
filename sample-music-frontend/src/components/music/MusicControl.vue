<template>
  <div id="Control" class="flex">
    <!-- 播放栏左侧 -->
    <div id="left" class="flex">
      <img :src="songPlaying.cover" alt="" @click="jumpPlay">
      <div class="text flex">
        <ScrollText></ScrollText>
        <span class="song-artist" @click="searchArtist(songPlaying.artist)">{{ songPlaying.artist }}</span>
        <span class="song-lyric">{{ currentLyric }}</span>
      </div>
    </div>
    <!-- 播放栏中间 -->
    <div id="middle">
      <div class="control_m flex">
        <img :src="Icon.lastIcon" alt="" @click="playPrevious">
        <img class="anm" v-show="isPlay" :src="Icon.pauseIcon" alt="" @click="PlayOrPause">
        <img class="anm" v-show="!isPlay" :src="Icon.playIcon" alt="" @click="PlayOrPause">
        <img :src="Icon.nextIcon" alt="" @click="playNext">
      </div>
    </div>
    <div class="temp"></div>
    <!-- 播放栏右侧 -->
    <div id="right">
      <div class="control_r flex">
        <div class="control_icon" @click="toggleSongMode">
          <img :src="songMode === 'mp3' ? Icon.mp3Icon : Icon.flacIcon" alt="">
        </div>
        <div class="control_icon" @click="togglePlayMode">
          <img :src="returnPlayIcon" alt="">
        </div>
        <div class="control_icon">
          <img :src="volume > 0 ? Icon.soundOnIcon : Icon.soundOnIcon" alt=""/>
          <div class="progress_volume progress-container">
            <!-- 音量控制条 -->
            <label class="slider">
              <input type="range" class="level" min="0" max="100" v-model="volume" @input="changeVolume">
            </label>
          </div>
        </div>
        <div class="control_icon" @click="isShowHistoryList">
          <img :src="Icon.historyIcon" alt="" :class="{'isShow':this.showList}">
          <div class="historyList" v-show="showList">
            <div class="historySong" v-for="s in playlistPlaying" :key="s.id" :class="{'hoi': s.id === songPlaying.id}">
              <img :src="s.cover" alt="">
              <div>
                <span style="font-weight: 600;">{{ s.title }}</span>
                <span style="opacity: 0.75">{{ s.artist }}</span>
              </div>
              <img :src="Icon.playIcon" alt="" @click="playBySong(s)">
              <img :src="Icon.closeIcon" alt="" @click="removeFromHistory(s.id, $event)">
            </div>
          </div>
        </div>
      </div>
      <div class="progress flex">
        <div class="progress-container">
          <span>{{ formattedTime(currentTime) }}</span>
          <input class="level" type="range" min="0" :max="songDuration" v-model="timer" @input="changeProgress">
          <span>{{ formattedTime(songDuration) }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Icon from "@/util/common/Icon";
import ScrollText from "@/pages/common/ScrollText.vue";

export default {
  name: 'MusicControl',
  components: {ScrollText},
  data() {
    return {
      showList: false,
    }
  },
  computed: {
    Icon() {
      return Icon
    },
    timer: {
      get() {
        return this.currentTime;
      },
      set(value) {
        this.setCurrentTime(value);
      }
    },
  },
  methods: {
    // 检测滚动显示
    /*checkScroll() {
      const container = this.$refs.container
      const text = this.$refs.text
      if (!container || !text) return

      // 清除旧动画
      if (this.animation) {
        this.animation.cancel()
        this.animation = null
      }

      // 重置位置
      text.style.transform = 'none'

      const isOverflow = text.scrollWidth > container.clientWidth
      if (isOverflow) {
        container.style.textAlign = 'left'
        const scrollDistance = text.scrollWidth - container.clientWidth

        // 创建滚动动画
        this.animation = text.animate([
          { transform: 'translateX(0)' },
          { transform: `translateX(-${scrollDistance}px)` }
        ], {
          duration: (scrollDistance / 50) * 1000,
          iterations: Infinity
        })
      } else {
        container.style.textAlign = 'center'
      }
    },*/
    // 跳转到播放页面
    jumpPlay() {
      this.$router.push({
        name: "play"
      }, () => {
      })
    },
    // 搜索歌手
    searchArtist(value) {
      this.$router.push({
        name: "search",
        params: { // 必须与路由 path 中的 :xxx 名称对应
          condition: value,
          params: 'ARTIST',
          sortType: 'LISTENERS_DESC',
          pageNum: 1,
          pageSize: 10
        }
      }, () => {
      })
    },
    // 改变进度
    changeProgress() {
      this.audio.currentTime = this.timer
    },
    // 格式化时间格式
    formattedTime(time) {
      const minutes = Math.floor(time / 60);
      const seconds = Math.floor(time % 60);
      return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
    },
    // 展示或隐藏历史播放记录
    isShowHistoryList(event) {
      this.showList = !this.showList;
      event.stopPropagation(); // 阻止事件冒泡
    },
    handleGlobalClick(event) {
      // 检查点击是否发生在历史播放记录列表之外
      if (this.showList && !event.target.closest('#right, #middle')) {
        this.showList = false;
      }
    },
    removeFromHistory(id, event) {
      this.setPlaylistPlaying(this.playlistPlaying.filter(item => item.id !== id));
      event.stopPropagation(); // 阻止事件冒泡，避免关闭历史列表
    }
  },
  mounted() {
    document.addEventListener('click', this.handleGlobalClick);
  },
  beforeDestroy() {
    document.removeEventListener('click', this.handleGlobalClick);
  },
}
</script>

<style scoped>
#Control {
  min-width: 1010px;
  min-height: 80px;
  border-radius: 10px;
  box-shadow: 0 0 5px #0000001a;
  background-color: var(--second-color);
  z-index: 10;
}

/* 歌曲基本信息 */
#left {
  width: 40%;
  height: 100%;
  justify-content: end;
}

/* 歌曲封面 */
#left img {
  width: 12%;
  margin: 1%;
  border-radius: 10px;
  cursor: pointer;
  height: auto; /* 先设置高度为自动，后续根据宽度来等比例调整高度 */
  aspect-ratio: 1 / 1; /* 设置宽高比为1:1，确保图片为正方形 */
  object-fit: contain; /* 使用 contain 属性，让图片在保持宽高比的前提下，尽可能填满容器，同时不会变形 */
}

.text {
  width: 85%;
  justify-content: start;
  gap: 1vw;
  flex-wrap: wrap;
  margin-left: 1%;
}

.song-artist:hover {
  max-width: 20%;
  text-decoration: underline;
}

.song-lyric {
  color: #888888;
  width: 100%;
  text-align: start;
}

/* 歌名歌手的文字格式 */
span {
  cursor: pointer;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 歌词 */
.lyric span {
  font-size: var(--fontSize);
  color: #00000080;
}

/* 歌曲控制 */
#middle {
  width: 20%;
  height: 100%;
}

/* 中间控制按钮布局 */
.control_m {
  width: 100%;
  height: 100%;
  border-radius: 10px;
  justify-content: center;
  gap: 1vw;
}

.control_m img {
  width: 10%;
  display: block;
  cursor: pointer;
  padding: 10px;
  border-radius: 5px;
}

.control_m img:hover {
  background-color: var(--fourth-color);
}

.anm {
  animation: keyframes-fill .5s;
}

.temp {
  width: 15%;
}

/* 歌曲模式 */
#right {
  width: 25%;
  height: 100%;
  position: relative;
}

/* 右边控制按钮布局 */
.control_r {
  height: 50%;
  width: 100%;
  border-radius: 10px;
  gap: 5%;
}

/* 右边控制按钮样式 */
.control_icon {
  height: 100%;
  display: flex;
  justify-content: center; /* 水平居中对齐 */
  align-items: center; /* 垂直居中对齐 */
}

.control_icon svg {
  height: 50%;
  text-align: center;
  display: block;
  cursor: pointer;
  padding: 5px;
  border-radius: 5px;
}

.control_icon > svg:hover {
  background-color: var(--fourth-color);
}

.control_icon:nth-of-type(3) {
  width: 40%;
}

.control_icon img {
  height: 50%;
  text-align: center;
  display: block;
  cursor: pointer;
  padding: 5px;
  border-radius: 5px;
}

.isShow, .control_icon > img:hover {
  background-color: var(--fourth-color);
}

/* 歌曲品质切换按钮 */
.control_icon:nth-of-type(1) span {
  border-radius: 5px;
  padding: 5px;
  transition: 0.1s all ease;
}

.control_icon:nth-of-type(1) span:hover {
  color: var(--second-color);
  background-color: var(--main-color);
}

.historyList {
  max-width: 100%;
  max-height: 800%;
  position: absolute;
  overflow: auto;
  right: 0;
  bottom: 105%;
  background-color: rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 5px;
}

.historySong {
  display: flex;
  justify-content: start;
  align-items: center;
  margin: 5px;
  border-radius: 5px;
  transition: 0.2s all ease;
}

.historySong span {
  color: black;
}

.historySong:hover {
  background-color: var(--fourth-color);
}

.hoi {
  transition: 0.2s all ease;
  background-color: var(--main-color);
}

.hoi:hover {
  background-color: var(--main-color);
}

.historySong:hover span {
  color: var(--second-color);
}

.historySong img:nth-of-type(1) {
  width: 12%;
  border-radius: 5px;
}

.historySong img:nth-of-type(2), .historySong img:nth-of-type(3) {
  width: 5%;
}

.historySong div {
  width: 65%;
  display: flex;
  transition: 0.2s all ease;
  flex-direction: column;
}

.historySong div span {
  width: 100%;
  text-align: start;
  overflow: hidden;
  transition: 0.2s all ease;
  text-overflow: ellipsis;
}

.progress {
  width: 100%;
  height: 50%;
}

.progress-container {
  width: 100%;
}

.progress-container span {
  width: 15%;
}

.progress_volume input {
  width: 100%;
}

/* 进度条 */
.level {
  width: 70%;
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  height: 10px;
  background: var(--fourth-color);
  overflow: hidden;
  border-radius: 5px;
  -webkit-transition: height 5s;
  -o-transition: height 5s;
  transition: height 5s;
  cursor: pointer;
}

.level::-webkit-slider-thumb {
  -webkit-appearance: none;
  width: 0;
  height: 0;
  -webkit-box-shadow: -2000px 0 0 2000px;
  box-shadow: -1000px 0 5px 1000px var(--main-color),
  -1000px 0 20px 1000px var(--main-color);
}

@keyframes keyframes-fill {
  0% {
    transform: rotate(-180deg) scale(0);
    opacity: 0;
  }

  50% {
    transform: rotate(-10deg) scale(1.2);
  }
}
</style>