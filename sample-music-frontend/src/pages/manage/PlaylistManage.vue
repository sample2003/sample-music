<template>
  <div class="playlistManage">
    <h2>编辑歌单信息</h2>
    <form @submit.prevent="handleSubmit">
      <div class="ipt" v-for="(value, field) in fields" :key="field">
        <!--                <label class="ipt1" :for="field">{{ field }}</label>-->
        <!-- 输入参数 -->
        <div class="ipt2" v-if="field === 'id'" style="display: none;">
          <input :id="field" v-model="formData[field]"/>
        </div>
        <div class="ipt2" v-else-if="field === 'title'">
          <input
              :id="field"
              :placeholder="value"
              required
              v-model="formData[field]"/>
          <span>{{ titleLength }}/15</span>
        </div>
        <div class="ipt2" v-else-if="field === 'description'">
                    <textarea
                        :id="field"
                        :placeholder="value || '暂无'"
                        required
                        v-model="formData[field]"/>
          <span>{{ descriptionLength }}/1000</span>
        </div>
        <!-- 上传图片 -->
        <div v-else-if="field === 'imageFiles'" class="image-file-upload">
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
        </div>
      </div>
      <button type="submit">提交</button>
      <button @click="handleCancel">取消</button>
    </form>
  </div>
</template>

<script>
import PlaylistService from "@/api/service/PlaylistService";

export default {
  data() {
    return {
      fields: {},
      formData: {}, // 表单数据
      playlistId: 1,
    };
  },
  computed: {
    titleLength() {
      return this.formData.title ? this.formData.title.length : 0;
    },
    descriptionLength() {
      return this.formData.description ? this.formData.description.length : 0;
    }
  },
  watch: {
    '$route'(to) {
      this.fields = to.query.fields;
      this.playlistId = to.params.id || this.playlistId;
    }
  },
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
        // 判断文件大小是否超过设定的限制（这里以5MB为例，可按需调整）
        const maxFileSize = 1024 * 1024; // 5MB，单位为字节
        if (file.size > maxFileSize) {
          this.$message(`当前文件大小为 ${Math.ceil(file.size / 1024 / 1024)} MB ，限制 ${Math.ceil(maxFileSize / 1024 / 1024)} MB`);
          return; // 如果文件过大，直接返回，不进行后续的数据更新和视图更新操作
        }
        this.formData[field] = file;
        // 立即更新预览
        this.$nextTick(() => {
          this.$forceUpdate(); // 强制更新视图
        });
      }
    },
    async handleSubmit() {
      const formData = new FormData();
      formData.append("id", this.fields.id)
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

      try {
        await PlaylistService.updatePlaylist(formData);
        // 处理响应
        await this.$router.push({path: `/music/list/detail/playlist/${this.playlistDetail.id}`})
      } catch (error) {
        console.error('上更新失败:', error);
      }
    },
    handleCancel() {
      this.$router.push({path: `/music/list/detail/playlist/${this.playlistDetail.id}`})
    },
  },
  mounted() {
    this.fields = this.$route.query.fields;
    this.playlistId = this.$route.params.id || this.playlistId;
  }
};
</script>

<style scoped>
.playlistManage {
  width: 90%;
  height: 50%;
  margin-left: 5%;
  display: flex;
  flex-direction: column;
  justify-content: start;

}

.playlistManage h2 {
  text-align: start;
}

form {
  width: 70%;
  height: 100%;
  gap: 5px;
  padding: 5px;
  border-radius: 5px;
  position: relative;
}

.ipt:nth-of-type(2) {
  width: 60%;
  height: 10%;
  position: absolute;
  left: 0;
  top: 0;
}

.ipt:nth-of-type(3) {
  width: 60%;
  height: 70%;
  position: absolute;
  left: 0;
  top: 15%;
}

.ipt:nth-of-type(4) {
  width: 30%;
  position: absolute;
  right: 5%;
  top: 0;
}

.ipt2 {
  width: 100%;
  height: 100%;
  position: relative;
}

.ipt2 textarea, .ipt2 input {
  width: 100%;
  height: 100%;
  text-indent: 1em;
  border-radius: 10px;
  border: 1px solid #000000;
  outline: none;
  resize: none; /* 禁止用户调整文本域的大小 */
}

.ipt2 textarea:valid, .ipt2 input:valid, .ipt2 textarea:focus, .ipt2 input:focus {
  border: 1px solid var(--main-color);
}

.ipt2 span {
  color: #888888;
  position: absolute;
  bottom: 1%;
  right: 1%;
}

.image-file-upload {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
}

.image-file-upload img {
  width: 100%;
  border-radius: 10px;
  height: auto; /* 先设置高度为自动，后续根据宽度来等比例调整高度 */
  aspect-ratio: 1 / 1; /* 设置宽高比为1:1，确保图片为正方形 */
  object-fit: contain; /* 使用 contain 属性，让图片在保持宽高比的前提下，尽可能填满容器，同时不会变形 */
}


button {
  position: absolute;
  bottom: 0;
  border: 1px solid #0000;
  padding: 5px;
  margin: 5px;
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.2s;
  background-color: var(--second-color);
  box-shadow: 2px 2px 4px #b3b3b3, -2px -2px 4px #ffffff;
}

button:hover {
  box-shadow: none;
  border: 1px solid var(--fourth-color);
}

button:nth-of-type(1) {
  left: 5px;
}

button:nth-of-type(2) {
  left: 55px;
}
</style>