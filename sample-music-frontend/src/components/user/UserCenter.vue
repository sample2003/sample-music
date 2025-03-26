<template>
  <div id="userCenter">
    <div class="top flex">
      <div class="userBasicInfo flex">
        <img
            :src="this.userDetail.avatar ?? Icon.notLoginIcon"
            alt="">
        <span>{{ this.userDetail.username ?? accessEnum.NOT_LOGIN }}</span>
      </div>
      <div class="nav flex">
        <div class="btns" v-for="b in btn" :key="b.id">
          <button
              class="btn flex"
              @click="jump(b.btnName)"
              :class="{'it': isCurrentRoute(b.btnName)}">
            <img :src="b.btnIcon" alt="">
            <span>{{ b.name }}</span>
          </button>
        </div>
      </div>
    </div>
    <div class="player" v-if="show">
      <img :src="songPlaying.cover" alt="" :class="isPlay? 'songRunning':'songPaused'">
      <img :src="Icon.nextPageWhiteIcon" alt="" @click="jump('play')">
      <div class="text flex">
        <ScrollText></ScrollText>
        <span class="song-artist" @click="searchArtist(songPlaying.artist)">{{ songPlaying.artist }}</span>
        <span class="song-lyric">{{ currentLyric }}</span>
      </div>
      <div class="icon flex">
        <img :src="Icon.lastIcon" alt="" @click="playPrevious">
        <img class="anm" v-show="isPlay" :src="Icon.pauseIcon" alt="" @click="PlayOrPause">
        <img class="anm" v-show="!isPlay" :src="Icon.playIcon" alt="" @click="PlayOrPause">
        <img :src="Icon.nextIcon" alt="" @click="playNext">
        <img :src="returnPlayIcon" alt="" @click="togglePlayMode">
        <img :src="Icon.soundOnIcon" alt="">
        <span>{{ songMode === "mp3" ? "高品" : "无损" }}</span>
      </div>
      <div class="progress-container flex">
        <span>{{ formattedTime(currentTime) }}</span>
        <input type="range" class="level" min="0" :max="songDuration" v-model="timer" @input="changeProgress">
      </div>
    </div>
    <div class="main">
      <transition name="fade">
        <router-view></router-view>
      </transition>
    </div>
  </div>
</template>

<script>
import {nanoid} from "nanoid";
import ACCESS_ENUM from "@/util/access/accessEnum";
import Icon from "@/util/common/Icon";
import accessEnum from "@/util/access/accessEnum";
import ScrollText from "@/pages/common/ScrollText.vue";

export default {
  name: "UserCenter",
  components: {ScrollText},
  computed: {
    accessEnum() {
      return accessEnum
    },
    Icon() {
      return Icon
    },
    show() {
      const isEmpty = this.songPlaying == null || Object.keys(this.songPlaying).length === 0;
      return !isEmpty;
    },
    timer: {
      get() {
        return this.currentTime;
      },
      set(value) {
        this.setCurrentTime(value);
      }
    },
    returnPlayIcon() {
      if (this.playMode === 'loop') return Icon.loopIcon
      else if (this.playMode === 'random') return Icon.randomIcon
      else if (this.playMode === 'single') return Icon.singleIcon
      else return Icon.loopIcon
    },
  },
  data() {
    const userDetail = this.$store.state.user.userDetail;
    const btn = [
      {
        btnID: nanoid(),
        btnName: "info",
        name: "账号信息",
        btnIcon: Icon.userIcon
      },
      {
        btnID: nanoid(),
        btnName: "collect",
        name: "我的收藏",
        btnIcon: Icon.collectIcon
      },
      {
        btnID: nanoid(),
        btnName: "suggest",
        name: "评价建议",
        btnIcon: Icon.suggestIcon
      },
      {
        btnID: nanoid(),
        btnName: "music",
        name: "进入音乐",
        btnIcon: Icon.homeIcon
      },
    ]
    // 如果用户是管理员，添加数据管理按钮
    if (userDetail && userDetail.role === ACCESS_ENUM.ADMIN) {
      btn.push({
        btnID: nanoid(),
        btnName: "manage",
        name: "数据管理",
        btnIcon: Icon.dataManageIcon
      });
    }
    return {
      btn: btn,
      animation: null
    }
  },
  methods: {
    // 检测滚动显示
    checkScroll() {
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
    },
    // 格式化时间格式
    formattedTime(time) {
      const minutes = Math.floor(time / 60);
      const seconds = Math.floor(time % 60);
      return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
    },
    // 搜索歌手
    searchArtist(value) {
      this.getSongsByCondition(value);
      this.$router.push({
        name: "search"
      }, () => {
      })
    },
    changeProgress() {
      this.audio.currentTime = this.timer
    },
    isCurrentRoute(btnName) {
      return this.$route.matched.some(record => record.name === btnName);
    },
    jump(name) {
      // 导航到新路由
      if (name === 'music') {
        this.$router.push({path: `/music/home`}, () => {
        })
      } else if (name === 'play') {
        this.$router.push({path: `/music/play`}, () => {
        })
      } else {
        this.$router.push({path: `/user/center/${name}`}, () => {
        })
      }
    },
  },
  mounted() {
    this.checkScroll()
    window.addEventListener('resize', this.checkScroll)
  }
}
</script>

<style scoped>
#userCenter {
  width: 100%;
  height: 100%;
  background-color: var(--second-color);
  position: relative;
}

.player {
  width: 25%;
  min-width: 300px;
  height: 100px;
  position: fixed;
  right: 1%;
  bottom: 1%;
  z-index: 99;
  border-radius: 60px 10px 10px 60px;
  border: 1px solid var(--fourth-color);
  box-shadow: inset 2px 2px 4px #b3b3b3, inset -2px -2px 4px #ffffff;
}

.player > img:nth-child(1) {
  height: 90px;
  position: absolute;
  left: 5px;
  top: 5px;
  border-radius: 50%;
  z-index: 2;
  box-shadow: 2px 2px 4px #b3b3b3, -2px -2px 4px #ffffff;
}

.player > img:nth-child(2) {
  height: 40px;
  position: absolute;
  padding: 25px;
  left: 5px;
  top: 5px;
  border-radius: 50%;
  z-index: 3;
  opacity: 0;
  cursor: pointer;
  transition: all 0.2s;
  background-color: #0008;
}

.player > img:nth-child(2):hover {
  opacity: 1;
}

.songPaused {
  animation: rotate 8s infinite linear paused;
}

.songRunning {
  animation: rotate 8s infinite linear running;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.text {
  width: 65%;
  position: absolute;
  right: 0;
  top: 0;
  justify-content: start;
  gap: 5%;
  flex-wrap: wrap;
  margin: 1%;
}

.text span {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  text-align: start;
  cursor: pointer;
}

.song-artist:hover {
  max-width: 20%;
  text-decoration: underline;
}

.song-lyric {
  color: #888888;
  width: 100%;
  font-size: 0.9em;
}

.icon {
  width: 65%;
  position: absolute;
  right: 0;
  bottom: 30%;
  margin-top: 1%;
  justify-content: space-between;
}

.icon img {
  width: 8%;
  padding: 1%;
  cursor: pointer;
  border-radius: 5px;
  transition: all 0.2s;
}

.icon img:hover {
  background-color: #8888;
}

.icon span {
  cursor: pointer;
  padding-right: 10px;
}

.progress-container {
  width: 65%;
  position: absolute;
  bottom: 2%;
  right: 0;
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

.top {
  width: 100%;
  height: 7%;
  justify-content: start;
}

.userBasicInfo {
  height: 96%;
  margin: 0 2%;
  cursor: default;
  border-radius: 60px;
  background-color: var(--second-color);
  box-shadow: 2px 2px 4px #b3b3b3, -2px -2px 4px #ffffff;
}

.userBasicInfo img {
  height: 90%;
  margin: 5px;
  border-radius: 50%;
}

.userBasicInfo span {
  padding-right: 15px;
  text-align: center;
}

.nav {
  width: 45%;
  height: 100%;
}

.btns {
  white-space: nowrap;
  margin: 0 1%;
}

.btns img {
  width: 20%;
}

.btn {
  cursor: pointer;
  border-radius: 10px;
  padding: 10px;
  transition: all 0.2s;
  box-sizing: border-box;
  border: 1px solid #0000;
  box-shadow: 2px 2px 4px #b3b3b3, -2px -2px 4px #ffffff;
}

.btn:hover {
  box-shadow: none;
  border: 1px solid var(--fourth-color);
}

.it {
  box-shadow: inset 2px 2px 4px #b3b3b3, inset -2px -2px 4px #ffffff;
}

.it span {
  color: var(--main-color);
}

.main {
  width: 96%;
  height: 90%;
  margin-top: 1%;
  margin-left: 2%;
  overflow: scroll;
}

/* 元素进入（淡入）阶段 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease; /* 设置过渡动画的时长为0.5秒，缓动函数为ease，这里时长可根据需求调整 */
  opacity: 1; /* 设置元素进入和离开时的初始透明度为0，实现淡入淡出的视觉起点和终点效果 */
}

.fade-enter,
.fade-leave-to {
  opacity: 0; /* 设置元素进入和离开时的初始透明度为0，实现淡入淡出的视觉起点和终点效果 */
}

.fade-enter-to,
.fade-leave {
  opacity: 1; /* 设置元素进入完成和离开前的透明度为1，也就是完全可见的状态 */
}


.anm {
  animation: keyframes-fill .5s;
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