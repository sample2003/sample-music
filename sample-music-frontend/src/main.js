import Vue from 'vue'
import App from './App.vue'
import audioMixin from "@/util/audioMixin";
import store from "@/store";
import router from "@/router";
import ConfirmDialogPlugin from "@/util/common/confirmDialogPlugin";
import musicStore from "@/util/common/MusicStore";
import userStore from "@/util/common/UserStore";

Vue.config.productionTip = false
Vue.mixin(audioMixin)
Vue.mixin(musicStore)
Vue.mixin(userStore)
Vue.use(ConfirmDialogPlugin)

new Vue({
  render: h => h(App),
  router: router,
  store: store,
  mounted() {
    // Vue 初始化完成后移除加载动画
    const loadingElement = document.getElementById('app-loading')
    if (loadingElement) {
      loadingElement.style.display = 'none'
    }
  }
}).$mount('#app')
