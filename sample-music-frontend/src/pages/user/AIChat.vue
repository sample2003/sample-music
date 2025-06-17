<template>
  <div id="AIChat" class="flex">
    <div class="left" :class="{ collapsed: isCollapsed }">
      <div class="control flex">
        <img class="kik" :src="isCollapsed ? Icon.toggleRightIcon : Icon.toggleLeftIcon" alt="" @click="toggleCollapse">
        <img class="kik" :src="Icon.addIcon" alt="" @click="toggleCollapse">
      </div>
      <div class="message-list">
        <div class="message flex" v-for="c in chatList" :key="c.id">
          <p>{{ c.title }}</p>
          <img :src="Icon.moreIcon" alt="">
          <div class="change">
            <button class="change-button">重命名</button>
            <button class="change-button">删除</button>
          </div>
        </div>
      </div>
    </div>
    <div class="right flex">
      <div v-if="!isChat" class="welcome flex">
        <img :src="Icon.deepseekTIcon" alt="">
        <p>欢迎使用由样本音乐提供的聊天服务，你的聊天数据将只会存储不会被分析使用</p>
      </div>

      <div v-else class="content">
        <div class="user-info info flex">
          <p>{{ value }}</p>
          <img :src="userDetail.avatar" alt="">
        </div>
        <div class="response-area info flex">
          <img :src="Icon.deepseekIcon" alt="">
          <MdEditor :content="streamingText" :show-edit="false"></MdEditor>
<!--          <p>{{ streamingText }}</p>-->
        </div>
      </div>

      <div class="input">
        <TextArea message="寻找你想听的歌吧" :value="content" type="chat" @submit="streamGenerate"></TextArea>
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
import OpenAI from "openai";
import ChatService from "@/api/service/ChatService";
import MdEditor from "@/components/MDEditor.vue";

export default {
  name: 'AiChat',
  components: {MdEditor, TextArea},
  computed: {
    Icon() {
      return Icon
    }
  },
  data() {
    return {
      isCollapsed: false,
      value: '',
      streamingText: '',   // 实时流式文本
      chatList: [],
      content: '',
      isChat: false,
    }
  },
  methods: {
    toggleCollapse() {
      this.isCollapsed = !this.isCollapsed
    },

    async streamGenerate(content) {
      this.isChat = true;
      this.value = content;
      this.streamingText = '';
      this.controller = new AbortController();

      try {
        const response = await fetch('http://localhost:8080/api/chat/generate/stream', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('userToken')
          },
          body: JSON.stringify({ prompt: content }),
          signal: this.controller.signal
        });

        const reader = response.body.getReader();
        const decoder = new TextDecoder();
        let buffer = ''; // 新增缓冲区

        while (true) {
          const { done, value } = await reader.read();
          if (done) break;

          // 将二进制数据解码并添加到缓冲区
          buffer += decoder.decode(value, { stream: true });

          // 处理缓冲区中完整的SSE事件
          while (buffer.includes('\n\n')) {
            const eventEndIndex = buffer.indexOf('\n\n');
            const eventData = buffer.substring(0, eventEndIndex);
            buffer = buffer.substring(eventEndIndex + 2); // 移除已处理数据

            // 提取事件内容（关键步骤）
            const contentLine = eventData.split('\n').find(line => line.startsWith('data:'));

            if (contentLine) {
              // 移除"data:"前缀并获取实际内容
              const content = contentLine.replace('data:', '').trim();

              // 逐字追加到显示文本
              this.streamingText += content;
              await this.$nextTick(); // 确保Vue更新渲染
            }
          }
        }
      } catch (error) {
        if (error.name !== 'AbortError') {
          console.error('请求失败:', error);
        }
      }
    },

    // 新增停止生成方法
    stopGenerate() {
      if (this.controller) {
        this.controller.abort();
        this.controller = null;
      }
    },
    async main(content) {
      const openai = new OpenAI({
        baseURL: 'https://api.deepseek.com',
        apiKey: 'sk-43af2c2f320944408bd6f24d25a58f61'
      });
      const completion = await openai.chat.completions.create({
        messages: [{ role: "system", content: content }],
        model: "deepseek-chat",
      });
      console.log(completion)
      alert(completion.choices[0].message.content);
    }
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
  position: relative;
}

.change {
  width: 100px;
  height: 50px;
  position: absolute;
  top: 0;
  display: flex;
  right: -100px;
}

.change-button {
  white-space: nowrap;
  border-radius: 10px;
  margin: 0 2px;
  padding: 10px;
  transition: all 0.2s ease;
  box-sizing: border-box;
  border: 1px solid #0000;
  box-shadow: 2px 2px 4px #b3b3b3, -2px -2px 4px #ffffff;
}

.change-button:hover {
  box-shadow: none;
  border: 1px solid var(--fourth-color);
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

.response-area {
  justify-content: start;
  white-space: pre-wrap;
  max-height: 60vh;
  overflow-y: auto;
  padding: 10px;
  border-radius: 4px;
}

.input {
  flex-grow: 1;
  width: 80%;
}

</style>