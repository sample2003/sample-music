<template>
  <div id="Main" :style="mainStyle">
    <!-- 内容区 -->
    <div class="main">
      <transition name="fade" mode="out-in">
        <router-view></router-view>
      </transition>
    </div>
    <span style="font-size: 0.8em;line-height: 1em;padding: 0.1em;">本网站只做于学习用途，若有问题联系作者删除</span>
  </div>
</template>

<script>

export default {
  name: "MusicMain",
  data() {
    return {
      mainHeight: '100%'
    }
  },
  methods: {
    updateSize() {
      this.$nextTick(() => {
        this.mainHeight = this.show ? "89%" : "100%"
      })
    }
  },
  watch: {
    '$route'() {
      this.updateSize()
    },
    // 监听播放状态变化
    songPlaying: {
      deep: true,
      handler() {
        this.updateSize()
      }
    }
  },
  computed: {
    mainStyle() {
      return {
        height: this.mainHeight
      }
    },
    show() {
      const isPlayRoute = this.$route.path.includes('/music/play');
      const isEmpty = this.songPlaying == null || Object.keys(this.songPlaying).length === 0;
      return !isEmpty && !isPlayRoute;
    }
  },
  mounted() {
    this.updateSize()
  }
}
</script>

<style scoped>
#Main {
  display: flex;
  justify-content: start;
  align-items: center;
  flex-direction: column;
  transition: height 0.2s ease;
}

.main {
  width: 100%;
  height: calc(100% - 1.4em);
}

</style>