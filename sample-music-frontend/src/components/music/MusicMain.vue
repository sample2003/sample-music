<template>
  <div id="Main" class="flex">
    <!-- 内容区 -->
    <div class="main">
      <transition name="fade">
        <keep-alive>
          <router-view :property="this.show"></router-view>
        </keep-alive>
      </transition>
    </div>
  </div>
</template>

<script>

export default {
  name: "MusicMain",
  data() {
    return {}
  },
  methods: {},
  computed: {
    show() {
      const isPlayRoute = this.$route.path.includes('/music/play');
      const isEmpty = this.songPlaying == null || Object.keys(this.songPlaying).length === 0;
      return !isEmpty && !isPlayRoute;
    }
  }
}
</script>

<style scoped>
#Main {
}

.main {
  width: 100%;
  height: 100%;
}

</style>