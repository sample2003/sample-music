<template>
  <div class="confirm-dialog" v-if="isVisible">
    <div class="dialog-content">
      <div class="actions">
        <form @submit.prevent="handleConfirm">
          <h3>{{ title }}</h3>
          <div class="ipt" v-for="(value, field) in fields" :key="field">
            <label class="ipt1" :for="field">{{ field }}</label>
            <!-- 输入参数 -->
            <input
                class="ipt2"
                :id="field"
                v-if="field !== 'imageFiles'"
                v-model="formData[field]"/>
            <!-- 上传图片 -->
            <div v-else class="image-file-upload">
              <img
                  :src="formData[field] ? getURL().createObjectURL(formData[field]) : value"
                  alt="Preview"
                  @click="triggerFileInput(field)">
              <input
                  type="file"
                  :id="field"
                  :ref="field"
                  @change="handleFileChange($event, field)"
                  style="display: none;"/>
              <span class="file-name" v-if="formData[field]">{{ formData[field].name }}</span>
              <span v-else>上传图片</span>
            </div>
          </div>
          <button type="submit">提交</button>
          <button @click="handleCancel">取消</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: 'UpdateDialog',
  data() {
    return {
      formData: {}
    }
  },
  props: ['type', 'fields', 'isVisible'],
  methods: {
    // 但图片被点击时，上传文件
    triggerFileInput(field) {
      this.$nextTick(() => {
        // 获取input的上传功能
        const fileInput = this.$refs[field][0];
        // 点击上传文件
        if (fileInput instanceof HTMLInputElement && fileInput.click) {
          fileInput.click();
        } else {
          console.error(`不能获取文件上传: ${field}`);
        }
      });
    },
    // 获取url
    getURL() {
      return window.URL;
    },
    // 获取上传的第一个文件
    handleFileChange(event, field) {
      const file = event.target.files[0];
      if (file) {
        this.formData[field] = file;
      }
    },
    handleConfirm() {
      const formData = new FormData();
      for (const [key, value] of Object.entries(this.formData)) {
        if (value instanceof File || value instanceof FileList) {
          if (value instanceof FileList) {
            for (let i = 0; i < value.length; i++) {
              formData.append(key, value[i]);
            }
          } else {
            formData.append(key, value);
          }
        } else {
          formData.append(key, value);
        }
      }
      this.$emit('confirm', formData);
    },
    handleCancel() {
      this.$emit('cancel');
    }
  },
  computed: {
    title() {
      if (this.type === 'playlist') {
        return "编辑歌单信息";
      } else {
        return "暂无";
      }
    },
  },
  mounted() {
  },
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

.ipt {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.ipt1 {
  width: 20%;
}

.ipt img {
  width: 10%;
}
</style>