<template>
  <div id="app">
    <keep-alive>
      <router-view></router-view>
    </keep-alive>
  </div>
</template>

<script>

export default {
  name: 'App',
  data() {
    return {
      loading: false,
    }
  },
  methods: {
    // 页面关闭前紧急上报用户听歌时长
    handleBeforeUnload() {
      if (this.trackStartTime && this.songPlaying) {
        const duration = Math.floor((Date.now() - this.trackStartTime) / 1000);
        // 使用sendBeacon确保可靠上报
        navigator.sendBeacon(
            `/api/song/log-duration?songId=${this.songPlaying.id}&duration=${duration}`
        );
      }
    },
  },
  mounted() {
    this.setIsPlay(false);
    this.setAudio(null);
    window.addEventListener('beforeunload', this.handleBeforeUnload);
  },
  beforeDestroy() {
    window.removeEventListener('beforeunload', this.handleBeforeUnload);
  },
}
</script>

<style>
*, html {
  margin: 0;
  padding: 0;
  --main-color: rgba(139, 128, 175);
  --second-color: #F0F2F0;
  --third-color: #000000;
  --fourth-color: #aaaaaa;
  --main-backgroundColor: linear-gradient(to right, var(--main-color), var(--second-color));
  --fontSize: 15px;
  --boxShadow: 2px 2px 4px var(--fourth-color), -2px -2px 4px #ffffff;
  --insetBoxShadow: inset 2px 2px 4px var(--fourth-color), inset -2px -2px 4px #ffffff;
}

#app {
  width: 100vw;
  height: 100vh;
  background: var(--second-color);
  font-size: var(--fontSize);
  color: var(--third-color);
  font-family:
      'Times New Roman',
      '华文宋体',
      sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
}

/* 滚动条 */
::-webkit-scrollbar {
  width: 10px !important;
  height: 10px !important;
  transition: 0.2s all ease;
}

::-webkit-scrollbar-track {
  background-color: #0000;
  transition: 0.2s all ease;
}

::-webkit-scrollbar-thumb {
  background: var(--fourth-color);
  border-radius: 5px;
}

::-webkit-scrollbar-thumb:hover {
  background: var(--main-color);
}

::-webkit-scrollbar-corner {
  background-color: #0000;
}

.flex {
  display: flex;
  justify-content: space-evenly;
  align-items: center;
}

.songPaused {
  animation: rotate 6s infinite linear paused;
}

.songRunning {
  animation: rotate 6s infinite linear running;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

/* 元素进入（淡入）阶段 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease; /* 设置过渡动画的时长为0.5秒，缓动函数为ease */
  opacity: 1; /* 设置元素进入和离开时的初始透明度为0，实现淡入淡出的视觉起点和终点效果 */
}

.fade-enter,
.fade-leave-to {
  transition: opacity 0.2s ease;
  opacity: 0; /* 设置元素进入和离开时的初始透明度为0，实现淡入淡出的视觉起点和终点效果 */
}

.fade-enter-to,
.fade-leave {
  transition: opacity 0.2s ease;
  opacity: 1; /* 设置元素进入完成和离开前的透明度为1，也就是完全可见的状态 */
}
</style>
