<template>
  <div class="confirm-dialog" v-if="isVisible">
    <div class="dialog-content">
      <p>{{ message }}</p>
    </div>
  </div>
</template>

<script>

export default {
  name: 'MessageDialog',
  data() {
    return {}
  },
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
  watch: {
  },
  methods: {
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
  background-color: rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(10px);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 100;
}

.dialog-content {
  position: relative;
}

.dialog-content p {
  position: relative;
  left: 0;
  padding: 20px 40px;
  border-radius: 5px;
  text-align: center;
  background: rgba(255, 255, 255, 0.9);
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
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
  margin-top: 20px;
  display: flex;
  flex-direction: column;
}

.actions button {
  color: var(--main-color);
  background-color: var(--second-color);
  border: 1px solid #000;
  border-radius: 3px;
  margin: 10px 10px;
  padding: 10px 20px;
  transition: .3s all ease;
}

.actions button:hover {
  color: var(--second-color);
  background-color: var(--main-color);
  border: 1px solid var(--main-color);
}
</style>