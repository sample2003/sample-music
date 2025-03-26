<template>
  <div class="confirm-dialog" v-if="isVisible">
    <div class="dialog-content">
      <div class="actions">
        <h4>{{ message }}</h4>
        <button @click="handleConfirm">确认</button>
        <button @click="handleCancel">取消</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ConfirmDialog',
  props: {
    message: {
      type: String,
      required: true
    },
    isVisible: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    handleConfirm() {
      this.$emit('confirm');
    },
    handleCancel() {
      this.$emit('cancel');
    }
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
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 100;
}

.dialog-content {
  left: 0;
  position: relative;
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
  align-items: center;
  position: relative;
  left: 0;
  padding: 20px;
  border-radius: 5px;
  text-align: center;
  background: rgba(255, 255, 255, 0.8);
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.actions h4 {
  padding: 5px;
}

button {
  width: 40%;
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
  padding: 2%;
  margin: 5%;
}

button:hover {
  background: #222;
  color: #fff;
  border-radius: 15px;
  box-shadow: 0 2px 15px #222;
  border: 1px solid #222;
}
</style>