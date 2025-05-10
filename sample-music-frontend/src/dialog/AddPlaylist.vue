<template>
  <div id="addPlaylist">
    <div class="box">
      <TextInput
          @inputChange="handleInput"
          style="width: 300px;height: 40px;"
          method="input"
          param-key="playlistTitle"
          message="请输入歌单标题"></TextInput>
      <div style="width: 100%;">
        <div style="display: flex;flex-wrap: wrap;">
          <input v-model="newTag" placeholder="回车新增标签" class="input" style="width: 100%;">
          <button @click="addTag($event)" style="display: none">add</button>
          <div class="tags">
              <span v-for="tag in tags" :key="tag" class="tag">
                  {{ tag }}
                  <img :src="Icon.closeIcon" alt="" @click="removeTag(tag)">
              </span>
          </div>
        </div>
      </div>
      <div class="in flex">
        <input
            type="checkbox"
            class="songCheck"
            v-model="isPublic">
        <span style="cursor: default">创建为私密歌单</span>
      </div>
      <button @click="handleConfirm()">确认</button>
      <button @click="handleCancel()">取消</button>
    </div>
  </div>
</template>

<script>
import TextInput from "@/components/TextInput.vue";
import PlaylistService from "@/api/service/PlaylistService";
import Icon from "@/util/common/Icon";

export default {
  name: "AddPlaylist",
  computed: {
    Icon() {
      return Icon
    }
  },
  components: {TextInput},
  props: {
    isVisible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      isPublic: false,
      inputTitle: '', // 用于存储输入框的值
      newTag: '',
      tags: [],
    }
  },
  methods: {
    handleInput(param, value) {
      if(param === "playlistTitle") {
        this.inputTitle = value;
      }else {
        this.newTag = value
      }
    },
    handleConfirm() {
      this.insertPlaylistSimple();
      this.$emit('confirm');
    },
    handleCancel() {
      this.$emit('cancel');
    },
    addTag() {
      alert("a")
      if (this.newTag.trim() && !this.tags.includes(this.newTag.trim())) {
        this.tags.push(this.newTag.trim());
        // this.$set(this.formData, 'tags', formTags); // 触发响应式更新
        this.newTag = '';
      } else {
        this.$message("重新输入标签")
      }
    },
    removeTag(tag) {
      const index = this.tags.indexOf(tag);
      if (index > -1) {
        this.tags.splice(index, 1);
      }
    },
    // 增加歌单
    async insertPlaylistSimple() {
      // 创建提交表单
      const formData = new FormData();
      // 遍历获得表单中的值
      formData.append("title", this.inputTitle);
      formData.append("isPublic", this.isPublic ? 1 : 0);
      if (this.inputTitle.length > 15) {
        this.$message("歌单名过长")
        return;
      } else if (this.inputTitle.length < 1) {
        this.$message("歌单名不可为空");
        return;
      }
      try {
        await PlaylistService.insertPlaylist(formData);
        this.inputTitle = "";
        await this.initPlaylist(); // 重新加载歌单列表
      } catch (error) {
        this.$message('添加歌单失败');
      }
    },
  }
}
</script>

<style scoped>
#addPlaylist {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 100;
}

.box {
  padding: 30px;
  border-radius: 10px;
  background-color: var(--second-color);
}


/* 选择框设计 */
.songCheck {
  appearance: none;
  width: 1.5em;
  height: 1.5em;
  margin: 0 5px;
  position: relative;
  outline: none;
  background: #ffffff80;
  backdrop-filter: blur(10px);
  border: 1px solid #888;
  border-radius: 50%;
  transition: 0.2s all;
  cursor: pointer;
}

.songCheck:hover {
  border: 1px solid var(--main-color);
}

.songCheck:checked {
  background: var(--main-color);
  border: 1px solid var(--main-color);
}

.songCheck::after {
  content: "";
  height: 25%;
  width: 50%;
  border: #fff solid 3px;
  position: absolute;
  top: 4px;
  left: 3px;
  opacity: 0;
  border-top: none;
  border-right: none;
  transform: rotate(-15deg);
  transition: 0.2s;
}

.songCheck:checked::after {
  content: "";
  height: 25%;
  width: 50%;
  border: #fff solid 3px;
  position: absolute;
  top: 4px;
  left: 3px;
  opacity: 1;
  border-top: none;
  border-right: none;
  transform: rotate(-45deg);
}

button {
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

.in {
  justify-content: start;
}

.tags {
  max-width: 100%;
  display: flex;
  justify-content: start;
  flex-wrap: wrap;
  gap: 5px;
}

.tag {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2px;
  margin: 5px;
  font-size: 10px;
  border: 1px solid var(--main-color);
  border-radius: 5px;
  background-color: #8b80af80;
}

.tag img {
  width: 15px;
  cursor: pointer;
  opacity: 0.5;
}

.tag img:hover {
  opacity: 1;
}
</style>