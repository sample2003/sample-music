<template>
  <div id="AIChat" class="flex">
    <div class="message-list" :class="{ collapsed: isCollapsed }">
      <img class="kik" :src="isCollapsed ? Icon.nextIcon : Icon.lastIcon" alt="" @click="toggleCollapse">
      <div class="history">

      </div>
    </div>
    <div class="content flex">
      <div class="welcome flex">
        <img :src="Icon.deepseekTIcon" alt="">
        <p>你的聊天数据将被收集哦，开始聊天将视为同意</p>
      </div>

      <div>
        <p>{{  }}</p>
        <p>{{ streamingText }}</p>
      </div>

      <div class="input">
        <TextArea message="寻找你想听的歌吧" max-limit="0" type="chat" @submit="startStream"></TextArea>
      </div>
    </div>
    <!--    &lt;!&ndash; 会话历史 &ndash;&gt;
        <div class="message-list">
          <div v-for="msg in messages" :key="msg.id" :class="['message', msg.role]">
            <div class="role">{{ msg.role === 'user' ? '你' : 'AI助手' }}</div>
            <div class="content">{{ msg.content }}</div>
            <div class="time">{{ formatTime(msg.createdAt) }}</div>
          </div>
        </div>

        &lt;!&ndash; 输入区域 &ndash;&gt;
        <div class="input-area">
          <input v-model="inputText" @keyup.enter="sendMessage" placeholder="输入消息...">
          <button @click="sendMessage">发送</button>
          <button @click="newChat">新对话</button>
        </div>-->
  </div>
</template>

<script>
import Icon from "@/util/common/Icon";
import TextArea from "@/pages/common/TextArea.vue";

export default {
  name: 'AiChat',
  components: {TextArea},
  computed: {
    Icon() {
      return Icon
    }
  },
  data() {
    return {
      isCollapsed: false,
      streamingText: '',   // 实时流式文本
    }
  },
  methods: {
    toggleCollapse() {
      this.isCollapsed = !this.isCollapsed
    },
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

          console.log(data)

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
  },
  mounted() {

  }
}
</script>

<style scoped>
#AIChat {
  width: 100%;
  height: 100%;
}

.message-list {
  width: 15%;
  height: 100%;
  transition: 0.2s all ease;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  border-radius: 10px;
  box-shadow: var(--insetBoxShadow);
  background-color: var(--second-color);
}

.kik {
  width: 40px;
  display: block;
}

.history {
  width: 100%;
  height: 80%;
  transition: 0.1s all ease;
}

.message-list.collapsed .history {
  width: 0;
  opacity: 0;
  pointer-events: none;
}

.message-list.collapsed {
  width: 40px;
}

.content {
  height: 100%;
  flex-grow: 1;
  flex-direction: column;
}

.welcome {
  width: 100%;
  height: 85%;
  flex-direction: column;
  justify-content: center;
}

.welcome img {
  width: 20%;
}

.welcome p {
  padding: 10px;
}

.input {
  flex-grow: 1;
  width: 80%;
}

.message {
  margin: 10px;
  padding: 15px;
  border-radius: 8px;
  max-width: 80%;
}

.message.user {
  background: #e3f2fd;
  margin-left: auto;
}

.message.assistant {
  background: #f5f5f5;
  margin-right: auto;
}
</style>