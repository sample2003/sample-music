<template>
  <div id="textInput">
    <div class="inputBox flex">
      <img :src="Icon.searchIcon" alt="搜索"
           class="searchIcon"
           @click="inputFx"
           v-if="this.searchIcon">
      <img :src="Icon.userIcon" alt="用户"
           class="userIcon"
           v-if="this.userIcon">
      <img :src="Icon.emailIcon" alt="邮箱"
           class="emailIcon"
           v-if="this.emailIcon">
      <img :src="Icon.passwordIcon" alt="密码"
           class="passwordIcon"
           v-if="this.passwordIcon">
      <img :src="Icon.verifyIcon" alt="验证码"
           class="verifyIcon"
           v-if="this.verifyIcon">
      <img :src="Icon.invitationIcon" alt="邀请码"
           class="invitationIcon"
           v-if="this.invitationIcon">
      <input
          :type="type"
          required="required"
          :value="value"
          @input="emitValue($event)"
          @keyup.enter="inputFx"
      >
      <span :class="className">{{ message }}</span>
      <img
          :src="Icon.closeIcon"
          alt="关闭"
          class="cancelIcon"
          v-if="this.cancelIcon"
          v-show="value"
          @click="clearInput"
      >
    </div>
  </div>
</template>

<script>
import Icon from "@/util/common/Icon";

export default {
  name: 'TextInput',
  props: {
    method: {type: String, default: 'input',},
    message: {type: String, default: ''},
    type: {type: String, default: 'text'},
    value: {type: String, default: ''},
    paramKey: {type: String, default: null},
    searchIcon: {type: Boolean, default: false},
    cancelIcon: {type: Boolean, default: false},
    userIcon: {type: Boolean, default: false},
    passwordIcon: {type: Boolean, default: false},
    verifyIcon: {type: Boolean, default: false},
    invitationIcon: {type: Boolean, default: false},
    emailIcon: {type: Boolean, default: false},
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
    }
  },
  methods: {
    emitValue(event) {
      if (this.paramKey) {
        this.$emit('inputChange', this.paramKey, event.target.value);
        this.$emit('loginInput', this.paramKey, event.target.value);
        this.$emit('registerInput', this.paramKey, event.target.value);
      } else {
        // 保持原有单个参数触发方式
        this.$emit('inputChange', event.target.value);
      }
    },
    inputFx() {
      if (this.method === 'fetchSongs') this.$emit('fetch');
      // else if(this.method === 'insertPlaylist') this.$emit('insertPlaylist');
    },
    clearInput() {
      this.$emit('inputChange', '');
      this.$emit('inputChange', this.paramKey, '');
      this.$emit('loginInput', this.paramKey, '');
      this.$emit('registerInput', this.paramKey, '');
    },
  }
}
</script>

<style scoped>
#textInput {
  width: 100%;
  min-width: 100px;
  height: 96%;
  margin: 2% 0;
}

.inputBox {
  width: 100%;
  height: 100%;
  font-size: 1.1em;
  transition: 0.2s all;
  position: relative;
  text-align: center;
  background: var(--second-color);
  border-radius: 10px;
  border: 1px solid var(--fourth-color);
  box-sizing: border-box;
}

.inputBox img {
  width: 10%;
  font-size: 1.1em;
  padding: 2px;
  box-sizing: border-box;
  border-radius: 5px;
  transition: 0.2s all;
  text-align: center;
}

.searchIcon {
  left: 0;
  cursor: pointer;
}

.userIcon {
  left: 0;
}

.cancelIcon {
  right: 0;
  cursor: pointer;
}

.passwordIcon {
  right: 0;
}

.searchIcon:hover,
.cancelIcon:hover {
  background: var(--fourth-color);
}

.inputBox input {
  width: 78%;
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