<template>
  <div id="aiChat">
    <!-- 会话历史 -->
    <div class="message-list">
      <div v-for="msg in messages" :key="msg.id" :class="['message', msg.role]">
        <div class="role">{{ msg.role === 'user' ? '你' : 'AI助手' }}</div>
        <div class="content">{{ msg.content }}</div>
        <div class="time">{{ formatTime(msg.createdAt) }}</div>
      </div>
    </div>

    <!-- 输入区域 -->
    <div class="input-area">
      <input v-model="inputText" @keyup.enter="sendMessage" placeholder="输入消息...">
      <button @click="sendMessage">发送</button>
      <button @click="newChat">新对话</button>
    </div>

    <div
      @click="aaa"
    ></div>
  </div>
</template>

<script>
export default {
  name: 'AiChat',
  data() {
    return {

    }
  },
  methods: {
    aaa() {
      this.setSongLyric()
    }
  },

}
</script>

<style scoped>
.message-list {
  height: 70vh;
  overflow-y: auto;
  padding: 10px;
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