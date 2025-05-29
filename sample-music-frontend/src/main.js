import Vue from 'vue'
import App from './App.vue'
import audioMixin from "@/util/audioMixin";
import store from "@/store";
import router from "@/router";
import ConfirmDialogPlugin from "@/util/common/confirmDialogPlugin";
import musicStore from "@/util/common/MusicStore";
import userStore from "@/util/common/UserStore";
import pageNation from "@/util/common/pageNation";
import musicTools from "@/util/common/musicTools";

/**
  md文档
* */
import VueMarkdownEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import '@kangc/v-md-editor/lib/theme/style/vuepress.css';
import Prism from 'prismjs';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import hljs from 'highlight.js';
VueMarkdownEditor.use(vuepressTheme, { Prism });
VueMarkdownEditor.use(githubTheme, { Hljs: hljs });
Vue.use(VueMarkdownEditor);

Vue.config.productionTip = false
Vue.mixin(audioMixin)
Vue.mixin(musicTools)
Vue.mixin(pageNation)
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
