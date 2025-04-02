<template>
  <div id="textInput">
    <div class="inputBox">
      <input
          type="text"
          required="required"
          @input="emitValue"
          @keyup.enter="inputFx"
      >
      <span :class="className">{{ this.message }}</span>
    </div>
  </div>
</template>

<script>
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
    return {}
  },
  methods: {
    emitValue(event) {
      this.$emit('inputChange', event.target.value);
    },
    inputFx() {
      if (this.method === 'fetchSongs') this.$emit('fetch');
      // else if(this.method === 'insertPlaylist') this.$emit('insertPlaylist');
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
  position: relative;
  text-align: center;
}

.inputBox input {
  width: 100%;
  height: 100%;
  text-indent: 1em;
  border-radius: 10px;
  transition: 0.5s;
  background: var(--second-color);
  cursor: pointer;
  outline: none;
  border: 1px solid #0000;
  box-sizing: border-box;
  box-shadow: var(--boxShadow);
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

.inputBox input:valid,
.inputBox input:focus {
  box-shadow: inset 2px 2px 4px #b3b3b3,
  inset -2px -2px 4px #ffffff;
  border: 1px solid var(--main-color);
}

</style>