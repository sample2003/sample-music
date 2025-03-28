<template>
  <div id="app">
    <LoadingPage v-if="loading"></LoadingPage>
    <keep-alive v-else>
      <router-view></router-view>
    </keep-alive>
    <span style="font-size: 70px;font-weight: bold;align-items: center"></span>
  </div>
</template>

<script>
import LoadingPage from "@/pages/common/LoadingPage.vue";

export default {
  name: 'App',
  components: {LoadingPage},
  data() {
    return {
      loading: false,
    }
  },
  methods: {
    handleLoad() {
      this.loading = false;
    }
  },
  mounted() {
    this.setIsPlay(false);
    this.setAudio(null);
    window.addEventListener('load', this.handleLoad);
  },
  beforeDestroy() {
    window.removeEventListener('load', this.handleLoad);
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

::-webkit-scrollbar {
  width: 0; /* 横向滚动条的宽度 */
  height: 0;
}

#app {
  width: 100vw;
  height: 100vh;
  background: var(--second-color);
  font-family: Consolas, Avenir, Helvetica, Arial, sans-serif;
  font-size: var(--fontSize);
  color: var(--third-color);
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
}

.flex {
  display: flex;
  justify-content: space-evenly;
  align-items: center;
}

</style>
