<template>
  <div id="musicMainSet">
    <h2>还在测试阶段，敬请期待</h2>
    <div>
      <button @click="check()">解析</button>
      <input type="file" id="folderInput" webkitdirectory directory multiple>
      <button @click="aaa">aaa</button>
      <button @click="bbb">bbb</button>
    </div>
  </div>
</template>

<script>
import ChatService from "@/api/service/AIService";
import TextInput from "@/components/TextInput.vue";
import TextArea from "@/components/TextArea.vue";
import StatisticsService from "@/api/service/StatisticsService";
import {mapGetters} from "vuex";

export default {
  name: 'MusicMainSet',
  components: {TextArea, TextInput},
  data() {
    return {
      chat: '',
      streamChat: '',
      content: '',         // 用户输入内容
      streamingText: '',   // 实时流式文本
      controller: null     // 用于中断请求的控制器
    };
  },
  computed: {
    ...mapGetters({
      // 从Vuex获取全局管理的音频实例
      audio: 'music/getAudio'
    })
  },
  methods: {
    getRawAudio() {
      return this.$store.state.music.play.audio;
    },

    async aaa() {
      const rawAudio = this.getRawAudio();
      rawAudio.currentTime = 30;
    },
    async bbb() {
      alert(this.audio.currentTime)
    },

    async check() {
      const input = document.getElementById('folderInput');
      if (!input.files.length) return;

      // 获取相对路径（示例：第一个文件的路径）
      const fullPath = input.files[0].webkitRelativePath;
      const folderPath = fullPath.split('/')[0];

      try {
        const response = await fetch(`http://localhost:3000/api/music/parse?folder=${encodeURIComponent(folderPath)}`, {
          method: 'POST'
        });
        const result = await response.json();
        alert(result.success || result.error);
      } catch (error) {
        console.error('请求失败:', error);
      }
    },
    aa(content) {
      ChatService.getChat(content)
          .then(content => {
            console.log(content);
            this.chat = content;
          })
          .catch(err => console.error("请求失败:", err));
    },
    /*aaa() {
      const moment = require('moment')
      const formattedTime = moment().format('YYYY-MM-DD HH:mm:ss')
      const listenDetail = {
        userId: this.userDetail,
        songId: this.songPlaying.id,
        duration: 10,
        listenTime: formattedTime
      }
      StatisticsService.recordListen(listenDetail)
      console.log(listenDetail)
    },*/
    // 新增：启动流式请求
    async startStream(content) {
      try {
        this.streamingText = '' // 清空上次内容Ni
        this.controller = new AbortController() // 创建中断控制器

        const response = await fetch('http://localhost:11434/api/chat', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            model: "deepseek-r1:8b",
            messages: [
              { role: "user", content: content }
            ],
            stream: true
          }),
          signal: this.controller.signal // 绑定中断信号
        })
        const reader = response.body.getReader()
        const decoder = new TextDecoder()

        // 实时流式处理
        while (true) {
          const { done, value } = await reader.read()
          if (done) break

          const chunk = decoder.decode(value)
          const data = JSON.parse(chunk)

          // 逐字追加到 streamingText（模拟逐字效果）
          if (data.message?.content) {
            this.streamingText += data.message.content
            await this.$nextTick() // 确保 Vue 渲染更新
          }
        }
      } catch (error) {
        if (error.name !== 'AbortError') {
          console.error('请求失败:', error)
        }
      }
    },
    // 新增：中断当前流式请求
    stopStream() {
      if (this.controller) {
        this.controller.abort()
        this.controller = null
      }
    }
  },
  mounted() {
  },
  beforeUnmount() {
    // 组件卸载时自动中断请求
    this.stopStream()
  }
};
</script>

<style scoped>
#musicMainSet {
  width: 100%;
  height: 100%;
  position: relative;
}
.stream-output {
  margin-top: 20px;
  border: 1px solid #eee;
  padding: 10px;
  min-height: 100px;
  white-space: pre-wrap; /* 保留换行格式 */
}
.text {
  position: absolute;
  bottom: 20%;
  left: 0;
  width: 100%;
  height: 40px;
}
</style>