<template>
  <div id="Main" class="flex">
    <!-- 内容区 -->
    <div class="main">
      <transition name="fade" mode="out-in">
        <keep-alive>
          <router-view></router-view>
        </keep-alive>
      </transition>
    </div>
    <span>本网站只做于学习用途，若有问题联系作者删除</span>
  </div>
</template>

<script>

export default {
  name: "MusicMain",
  data() {
    return {}
  },
  methods: {
    initSize() {
      if(this.show) {
        document.getElementById("Main").style.height = "90%"
      }else {
        document.getElementById("Main").style.height = "100%"
      }
    }
  },
  computed: {
    show() {
      const isPlayRoute = this.$route.path.includes('/music/play');
      const isEmpty = this.songPlaying == null || Object.keys(this.songPlaying).length === 0;
      return !isEmpty && !isPlayRoute;
    }
  },
  mounted() {
    this.initSize()
  }
}
</script>

<style scoped>
#Main {
  flex-direction: column;
}

.main {
  width: 100%;
  height: calc(100% - 2.5em);
}

</style>