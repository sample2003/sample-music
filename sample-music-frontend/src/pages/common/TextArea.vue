<template>
  <div id="textArea">
    <textarea
        required="required"
        @input="limitCommentLength"
        v-model="comment"
    ></textarea>
    <span>说点什么吧^^</span>
    <button @click="submitFx">发布</button>
    <p><span style="color: var(--main-color)">{{ comment.length }}</span>/{{ this.num }}</p>
  </div>
</template>

<script>
export default {
  name: "TextArea",
  data() {
    return {
      comment: '',
      num: 200
    }
  },
  methods: {
    // 评论
    async submitFx() {
      this.$emit('submit', this.comment)
      this.comment = ''
    },
    // 限制评论长度
    limitCommentLength() {
      if (this.comment.length > this.num) {
        this.comment = this.comment.slice(0, this.num);
      }
    },
  }
}
</script>

<style scoped>
#textArea {
  height: 150px;
  width: 90%;
  margin-left: 5%;
  border-radius: 20px;
  box-sizing: border-box;
  font-size: var(--fontSize);
  position: relative;
  text-align: center;
  box-shadow: inset 2px 2px 4px #b3b3b3, inset -2px -2px 4px #ffffff;
}

#textArea > span {
  width: 100%;
  left: 0;
  bottom: 50%;
  color: #999999;
  letter-spacing: 0.1em;
  border-radius: 3px;
  transition: all 0.5s ease;
  position: absolute;
  pointer-events: none;
}

#textArea textarea {
  width: 100%;
  height: calc(100% - 2em);
  padding: 10px;
  box-sizing: border-box;
  border-radius: 10px;
  resize: none; /* 禁止用户调整文本域的大小 */
  transition: 0.5s;
  border: 1px solid #0000;
  background: #0000;
  outline: none;
}

#textArea textarea:valid ~ span,
#textArea textarea:focus ~ span {
  bottom: 5px;
  letter-spacing: 0.2em;
  color: var(--main-color);
}

#textArea textarea:valid,
#textArea textarea:focus {
}

#textArea button {
  border: none;
  cursor: pointer;
  position: absolute;
  bottom: 5px;
  padding: 1px 10px;
  right: 5px;
  border-radius: 10px;
  background-color: var(--second-color);
  color: #888888;
  box-shadow: 2px 2px 4px #b3b3b3, -2px -2px 4px #ffffff;
}

#textArea p {
  position: absolute;
  bottom: 5px;
  padding: 1px 10px;
  left: 5px;
  border-radius: 10px;
  background-color: var(--second-color);
  color: #888888;
  box-shadow: 2px 2px 4px #b3b3b3, -2px -2px 4px #ffffff;
}
</style>