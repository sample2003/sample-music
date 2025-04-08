<template>
  <div class="confirm-dialog" v-if="isVisible">
    <div class="dialog-content">
      <div class="actions">
        <h3>{{ message }}</h3>
        <div class="a1" v-for="i in items" :key="i.id" @click="handleConfirm(i.id)">
          <img :src="i.cover" alt="">
          <span>{{ i.title }}</span>
        </div>
        <button @click="handleCancel()">取消</button>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: 'ChangeDialog',
  data() {
    return {
      song_lists: []
    }
  },
  props: {
    message: {
      type: String,
      required: true
    },
    isVisible: {
      type: Boolean,
      default: false
    },
    items: {
      type: Array,
    }
  },
  methods: {
    handleConfirm(targetId) {
      this.$emit('confirm', targetId);
      // this.addSongToSongList();
    },
    handleCancel() {
      this.$emit('cancel');
    },
  },
  mounted() {
  }
};
</script>

<style scoped>
.confirm-dialog {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(10px);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 100;
}

.dialog-content {
  width: 20%;
  position: relative;
  left: 0;
}

.dialog-content::before {
  content: "";
  width: 100%;
  height: 100%;
  position: absolute;
  top: 10px;
  left: 10px;
  border-radius: 5px;
  background: var(--main-color);
}

.actions {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  left: 0;
  padding: 20px;
  border-radius: 5px;
  text-align: center;
  background: rgba(255, 255, 255, 0.8);
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.a1 {
  width: 95%;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 2%;
  border: 1px solid var(--main-color);
  padding: 5px 0;
  border-radius: 5px;
  transition: 0.2s all ease;
}

.a1:hover {
  background-color: var(--main-color);
}

.a1:hover span {
  color: var(--second-color);
}

.a1 img {
  width: 20%;
  border-radius: 5px;
  height: auto; /* 先设置高度为自动，后续根据宽度来等比例调整高度 */
  aspect-ratio: 1 / 1; /* 设置宽高比为1:1，确保图片为正方形 */
  object-fit: contain; /* 使用 contain 属性，让图片在保持宽高比的前提下，尽可能填满容器，同时不会变形 */
}

.a1 span {
  width: 70%;
  overflow: hidden;
  text-overflow: ellipsis;
}

button {
  cursor: pointer;
  display: inline-block;
  font-size: 1.1em;
  color: #111;
  background: rgba(255, 255, 255, 0);
  border: 1px solid #111;
  border-radius: 5px;
  text-decoration: none;
  font-weight: 600;
  transition: 0.2s;
  padding: 5px 15px;
  margin: 2%;
}

button:hover {
  background: #222;
  color: #fff;
  border-radius: 15px;
  box-shadow: 0 2px 15px #222;
  border: 1px solid #222;
}
</style>