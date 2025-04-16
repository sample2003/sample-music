<template>
  <div id="AIChat" class="flex">
    <div class="left" :class="{ collapsed: isCollapsed }">
      <div class="control flex">
        <img class="kik" :src="isCollapsed ? Icon.toggleRightIcon : Icon.toggleLeftIcon" alt="" @click="toggleCollapse">
        <img class="kik" :src="Icon.addIcon" alt="" @click="toggleCollapse">
      </div>
      <div class="message-list">
        <div class="message flex">
          <p>你是谁</p>
          <img :src="Icon.moreIcon" alt="">
        </div>
        <div class="message flex">
          <p>复述aaaaaaaaaaaaaaaaaaaaaaaaaaa</p>
          <img :src="Icon.moreIcon" alt="">
        </div>
      </div>
    </div>
    <div class="right flex">
      <div v-if="streamingText === ''" class="welcome flex">
        <img :src="Icon.deepseekTIcon" alt="">
        <p>你的聊天数据将被收集哦，开始聊天将视为同意</p>
      </div>

      <div v-else class="content">
        <div class="user-info info flex">
          <p>{{ value }}</p>
          <img :src="userDetail.avatar" alt="">
        </div>
        <div class="chat-info info flex">
          <img :src="Icon.deepseekIcon" alt="">
          <p>{{ streamingText }}</p>
        </div>
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
import TextArea from "@/components/TextArea.vue";

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
      value: '复述aaaaaaaaaaaaaaaaaaaaaaaaaaa',
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

.left {
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

.left.collapsed .message-list {
  width: 0;
  opacity: 0;
  pointer-events: none;
}

.left.collapsed .control {
  flex-direction: column;
}

.kik {
  width: 40px;
  display: block;
  cursor: pointer;
  padding: 5px;
  box-sizing: border-box;
  border-radius: 10px;
  margin: 5px;
}

.kik:hover {
  background-color: var(--fourth-color);
}

.left.collapsed {
  width: 50px;
}

.message-list {
  width: 100%;
  height: 80%;
  display: flex;
  flex-direction: column;
  align-items: start;
  justify-content: start;
  transition: 0.2s width ease;
}

.message {
  width: 94%;
  height: 50px;
  margin: 3%;
  border-radius: 10px;
  transition: 0.2s all ease;
  border: 1px solid #0000;
  box-shadow: var(--boxShadow);
  background-color: var(--second-color);
  cursor: pointer;
}

.message:hover {
  box-shadow: none;
  border: 1px solid var(--fourth-color);
}

.message:hover img {
  opacity: 1;
}

.message p {
  width: 85%;
  text-indent: 1em;
  text-align: left;
  display: block;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.message img {
  width: 9%;
  margin: 1%;
  padding: 5px;
  opacity: 0;
  box-sizing: border-box;
  border-radius: 5px;
  transition: 0.1s all ease;
}

.message img:hover {
  background-color: var(--fourth-color);
}

.right {
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

.content {
  width: 80%;
  height: 85%;
  display: flex;
  flex-direction: column;
  justify-content: start;
}

.info {
  width: 94%;
  align-items: start;
  margin: 1em;
  text-align: left;
}

.info p {
  max-width: calc(89% - 1em);
  padding: 1em;
  border-radius: 10px;
  box-shadow: var(--boxShadow);
  white-space: wrap;
  overflow-wrap: break-word;
  word-break: break-word;
}

.info img {
  width: 50px;
  padding: 10px;
  box-sizing: border-box;
  border-radius: 50%;
  user-select: none;
  pointer-events: none;
}

.user-info {
  justify-content: end;
}

.user-info p {
  background-color: rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(10px);
}

.chat-info {
  justify-content: start;
}

.input {
  flex-grow: 1;
  width: 80%;
}

</style>