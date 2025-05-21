<template>
  <div id="userInfo">
    <div class="left">
      <div class="left-top">
        <img
          :src="coverPreview || (userDetail?.avatar ?? Icon.notLoginIcon)"
          alt="">
        <input
          type="file"
          ref="field"
          @change="handleFileChange($event)"
          style="display: none;"/>
        <span @click="triggerFileInput">修改图片</span>
      </div>
      <div class="left-bottom">
        <div>
          <p>简介：{{ userDetail.description }}</p>
        </div>
      </div>
    </div>
    <div class="right-top">
      <p>{{ userDetail?.username }}</p>
      <p>性别：{{ userDetail?.sex }}</p>
      <p>关注数：{{ userDetail?.likes }}</p>

      <p>等级：{{ userDetail?.exp }}</p>
    </div>
  </div>
</template>

<script>
import Icon from "@/util/common/Icon";

export default {
  name: "UserInfo",
  computed: {
    Icon() {
      return Icon
    }
  },
  data() {
    return {
      coverPreview: null
    }
  },
  methods: {
    handleFileChange(event) {
      const file = event.target.files[0];
      if (file) {
        // 判断文件大小是否超过设定的限制（这里以5MB为例，可按需调整）
        const maxFileSize = 1024 * 1024; // 5MB，单位为字节
        if (file.size > maxFileSize) {
          this.$message(`当前文件大小为 ${Math.ceil(file.size / 1024 / 1024)} MB ，限制 ${Math.ceil(maxFileSize / 1024 / 1024)} MB`);
          return; // 如果文件过大，直接返回，不进行后续的数据更新和视图更新操作
        }
        // 使用 FileReader 读取文件内容
        const reader = new FileReader();
        reader.onload = (e) => {
          this.coverPreview = e.target.result; // 这里得到的是 Data URL
        };
        reader.readAsDataURL(file);
        
        // 保存文件对象用于后续上传
        this.selectedFile = file;
        
        // 立即更新预览
        this.$nextTick(() => {
          this.$forceUpdate(); // 强制更新视图
        });
      }
    },
    triggerFileInput() {
      this.$nextTick(() => {
        // 获取input的上传功能
        const fileInput = this.$refs.field;
        // 点击上传文件
        if (fileInput instanceof HTMLInputElement && fileInput.click) {
          fileInput.click();
        } else {
          console.error(`不能获取文件上传`);
        }
      });
    },
  },
  mounted() {
    console.log(this.userDetail)
  }
}
</script>

<style scoped>
#userInfo {
  width: 100%;
  height: 100%;
  display: flex;
  gap: 1%;
}

.left {
  width: 15%;
  height: 100%;
  display: flex;
  gap: 1%;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}

.left-top {
  width: 80%;
  display: flex;
  align-items: center;
  justify-content: center;
  height: auto; /* 先设置高度为自动，后续根据宽度来等比例调整高度 */
  aspect-ratio: 1 / 1; /* 设置宽高比为1:1，确保图片为正方形 */
  object-fit: contain;
  position: relative;
}

.left-top img {
  width: 90%;
  border-radius: 50%;
  box-shadow: var(--boxShadow);
  height: auto; /* 先设置高度为自动，后续根据宽度来等比例调整高度 */
  aspect-ratio: 1 / 1; /* 设置宽高比为1:1，确保图片为正方形 */
  object-fit: contain; /* 使用 contain 属性，让图片在保持宽高比的前提下，尽可能填满容器，同时不会变形 */
  max-height: 300px; /* 限制最大高度 */
  position: absolute;
  top: 0;
  left: 0;
  z-index: 0;
}

.left-top span {
  width: 90%;
  position: absolute;
  height: auto; /* 先设置高度为自动，后续根据宽度来等比例调整高度 */
  aspect-ratio: 1 / 1; /* 设置宽高比为1:1，确保图片为正方形 */
  object-fit: contain; /* 使用 contain 属性，让图片在保持宽高比的前提下，尽可能填满容器，同时不会变形 */
  max-height: 300px; /* 限制最大高度 */
  display: flex;
  justify-content: center;
  align-items: center;
  color: var(--second-color);
  border-radius: 50%;
  backdrop-filter: blur(5px);
  top: 0;
  left: 0;
  z-index: 1;
  opacity: 0;
  cursor: pointer;
  transition: all 0.2s ease;
  text-decoration: underline;
}

.left-top span:hover {
  opacity: 1;
}

.left-bottom {
  width: 100%;
  flex-grow: 1;
}

.right-top {
}
</style>