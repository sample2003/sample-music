<template>
  <div id="textArea">
    <textarea
        required="required"
        @input="limitCommentLength"
        v-model="area"
    ></textarea>
    <span>{{ message }}</span>
    <button v-if="area && type !=='text'" @click="submitFx">Shoot!</button>
    <p v-show="maxLimit !== '0' && area.length !== 0"><span style="color: var(--main-color)">{{
        area.length
      }}</span>/{{ this.maxLimit }}</p>
  </div>
</template>

<script>
export default {
  name: "TextArea",
  props: {
    message: {type: String},
    maxLimit: {type: String, default: '0'},
    type: {type: String, default: 'text'}
  },
  data() {
    return {
      area: ''
    }
  },
  methods: {
    // 评论
    async submitFx() {
      this.$emit('submit', this.area)
      this.area = ''
    },
    // 限制评论长度
    limitCommentLength(event) {
      if (this.maxLimit !== '0' && this.maxLimit !== null) {
        if (this.area.length > this.maxLimit) {
          this.area = this.area.slice(0, parseInt(this.maxLimit));
        }
      }
      if (this.paramKey) {
        this.$emit('input', this.paramKey, event.target.value);
      } else {
        // 保持原有单个参数触发方式
        this.$emit('input', event.target.value);
      }
    },
  },
  mounted() {
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