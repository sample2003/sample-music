<template>
  <div id="AIChat" class="flex">
    <div class="welcome flex">
      <img :src="Icon.deepseekTIcon" alt="" >
      <p>你所输入的数据将被收集哦，开始聊天将视为同意</p>
    </div>
    <div class="input">
      <TextArea message="寻找你想听的歌吧" max-limit="0"></TextArea>
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

    <div
      @click="aaa"
    ></div>
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
#AIChat {
  width: 100%;
  height: 100%;
  flex-direction: column;
}

.welcome {
  width: 100%;
  height: 50%;
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
  width: 60%;
}

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