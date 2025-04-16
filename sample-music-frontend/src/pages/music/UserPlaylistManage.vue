<template>
  <div id="UserPlaylistManage">
    <h2>编辑歌单信息</h2>
    <form @submit.prevent="handleSubmit">
      <div class="left-column">
        <div class="title">
          <TextInput :message="playlistUpdate.title" :value="title" param-key="title" @inputChange="inputTitle"></TextInput>
        </div>
        <div class="description">
          <TextArea :message="playlistUpdate.description" :value="description" @input="inputDescription"></TextArea>
        </div>
        <div class="button">
          <button class="handleButton" type="submit">提交</button>
          <button class="handleButton" @click="handleCancel">取消</button>
        </div>
      </div>
      <div  class="cover">
        <img
            :src="coverPreview || playlistUpdate.cover"
            @click="triggerFileInput"
            alt="">
        <input
            type="file"
            ref="field"
            @change="handleFileChange($event)"
            style="display: none;"/>
      </div>
    </form>
  </div>
</template>

<script>
import PlaylistService from "@/api/service/PlaylistService";
import TextInput from "@/components/TextInput.vue";
import TextArea from "@/components/TextArea.vue";

export default {
  name: "UserPlaylistManage",
  components: {TextArea, TextInput},
  props: {
    id: {
      type: String
    }
  },
  computed: {},
  data() {
    return {
      playlistUpdate: {},
      title: '',
      description: '',
      coverPreview: null,  // 用于预览新上传的图片
      selectedFile: null    // 存储选中的文件对象
    }
  },
  methods: {
    async fetchData() {
      const flag = await PlaylistService.checkBelongTo(this.id);
      if (flag) {
        this.playlistUpdate = await PlaylistService.playlistWithSongs(this.id);
      }else {
        this.$message("无权修改该歌单")
        this.$router.go(-1);
      }
    },
    inputTitle(param, key) {
      this[param] = key
      console.log(this.title)
    },
    inputDescription(key) {
      this.description = key
      console.log(this.description)
    },
    // 但图片被点击时，上传文件
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

    handleCancel() {
      this.$router.push({path: `/music/list/detail/playlist/${this.id}`})
    },
    async handleSubmit() {
      const formData = new FormData();
      formData.append("id", this.id);
      if(this.title) {
        formData.append("title", this.title);
      }
      if(this.description) {
        formData.append("description", this.description);
      }
      if(this.selectedFile) {
        formData.append("imageFiles", this.selectedFile);
      }
      try {
        await PlaylistService.updatePlaylist(formData);
        // 处理响应
        await this.$router.push({path: `/music/list/detail/playlist/${this.id}`})
      } catch (error) {
        console.error('上更新失败:', error);
      }
    },
  },
  mounted() {
    this.fetchData();
  }
}
</script>

<style scoped>
#UserPlaylistManage {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: start;
}

h2 {
  margin: 1em 2em;
}

form {
  width: 50%;
  display: flex;
  align-items: flex-start;/* 顶部对齐 */
}


/* 左侧列样式 */
.left-column {
  flex: 2;                /* 占据剩余空间 */
  display: flex;
  flex-direction: column; /* 垂直排列子元素 */
  justify-content: center;
  align-items: center;
  gap: 10px;              /* 子元素间距 */
}

.title {
  width: 90%;
  height: 60px;
}

/* 描述区域高度自适应 */
.description {
  width: 100%;
  flex: 1;                /* 占据剩余垂直空间 */
  min-height: 150px;      /* 最小高度保证比标题高 */
}

.button {
  width: 90%;
  display: flex;
}

/* 右侧封面样式 */
.cover {
  flex: 1;                /* 与左侧宽度相等 */
  position: sticky;       /* 可选：实现滚动时固定 */
  top: 20px;
}

.cover img {
  width: 70%;
  border-radius: 5px;
  box-shadow: var(--boxShadow);
  height: auto; /* 先设置高度为自动，后续根据宽度来等比例调整高度 */
  aspect-ratio: 1 / 1; /* 设置宽高比为1:1，确保图片为正方形 */
  object-fit: contain; /* 使用 contain 属性，让图片在保持宽高比的前提下，尽可能填满容器，同时不会变形 */
  max-height: 300px; /* 限制最大高度 */
}

.handleButton {
  cursor: pointer;
  display: inline-block;
  font-size: 1.1em;
  background: #222;
  color: #fff;
  box-shadow: 0 2px 15px #222;
  border: none;
  border-radius: 5px;
  text-decoration: none;
  font-weight: 700;
  transition: 0.2s;
  padding: 5px 10px;
  margin: 10px;
}

.handleButton:hover {
  color: #222;
  background: #fff;
  box-shadow: 0 2px 15px #fff;
}
</style>