<template>
  <div id="textInput">
    <div class="inputBox">
      <img :src="Icon.searchIcon" alt=""
           @click="inputFx"
           v-if="this.method === 'fetchSongs'">
      <input
          type="text"
          required="required"
          v-model="value"
          @input="emitValue"
          @keyup.enter="inputFx"
      >
      <span :class="className">{{ message }}</span>
      <img
          :src="Icon.closeIcon"
          alt="关闭"
          v-show="value"
          @click="clearInput"
          v-if="this.method === 'fetchSongs'"
      >
    </div>
  </div>
</template>

<script>
import Icon from "@/util/common/Icon";

export default {
  name: 'TextInput',
  props: {
    method: {
      type: String,
      default: 'input',
    },
    message: {
      type: String,
      default: ''
    },
  },
  computed: {
    Icon() {
      return Icon
    },
    className() {
      // 根据 method 的值返回相应的类名
      if (this.method === 'fetchSongs') {
        return 'fs';
      } else if (this.method === 'input') {
        return 'it';
      }
      return ''; // 如果不需要添加类名，则返回空字符串
    }
  },
  data() {
    return {
      value: '',
    }
  },
  methods: {
    emitValue(event) {
      this.$emit('inputChange', event.target.value);
    },
    inputFx() {
      if (this.method === 'fetchSongs') this.$emit('fetch');
      // else if(this.method === 'insertPlaylist') this.$emit('insertPlaylist');
    },
    clearInput() {
      this.value = '';
      this.$emit('inputChange', '');
    },
  }
}
</script>

<style scoped>
#textInput {
  width: 100%;
  height: 100%;
}

.inputBox {
  width: 100%;
  height: 100%;
  font-size: 1.1em;
  transition: 0.5s all;
  position: relative;
  text-align: center;
  background: var(--second-color);
  border-radius: 10px;
  border: 1px solid var(--fourth-color);
  box-sizing: border-box;
}

.inputBox img {
  height: 60%;
  font-size: 1.1em;
  position: absolute;
  top: 20%;
  padding: 2px;
  box-sizing: border-box;
  border-radius: 50%;
  transition: 0.2s all;
  text-align: center;
}

.inputBox img:hover {
  background: var(--fourth-color);
}

.inputBox img:nth-child(1) {
  left: 0;
}

.inputBox img:nth-child(2) {
  right: 0;
}

.inputBox input {
  width: 80%;
  height: 100%;
  outline: none;
  background: #0000;
  border: none;
}

.inputBox > span {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 0.9em;
  white-space: nowrap;
  letter-spacing: 0.1em;
  border-radius: 3px;
  transition: all 0.5s ease;
  pointer-events: none;
}

.inputBox input:valid ~ .fs,
.inputBox input:focus ~ .fs {
  left: 0;
  transform: translate(-100%, -50%);
  color: var(--main-color);
}

.inputBox input:valid ~ .it,
.inputBox input:focus ~ .it {
  left: 20%;
  opacity: 0;
}

.inputBox:focus-within,
.inputBox:has(input:valid) {
  box-shadow: inset 2px 2px 4px #b3b3b3,
  inset -2px -2px 4px #ffffff;
  border: 1px solid var(--main-color);
}
</style>