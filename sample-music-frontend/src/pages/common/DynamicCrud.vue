<template>
  <div id="dynamicCrud">
    <div class="top">
      <form @submit.prevent="handleSubmit">
        <h3>上传{{ title }}</h3>
        <div class="ipt" v-for="(field, index) in fields" :key="index">
          <!-- 字段名 -->
          <label class="ipt1" :for="field">{{ change(field) }}</label>
          <input
              class="ipt2"
              v-if="!field.endsWith('Files') && field!=='tags'"
              :id="field"
              :placeholder="change(field)"
              v-model="formData[field]"/>
          <!--上传图片-->
          <div v-else-if="field==='imageFiles'" class="image-file-upload">
            <img
                :src="formData[field] ? getURL().createObjectURL(formData[field]) : Icon.uploadIcon"
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
          <!--上传歌词-->
          <div v-else-if="field==='textFiles'" class="image-file-upload">
            <img
                :src="formData[field] ? Icon.fileIcon : Icon.uploadIcon"
                alt="Preview"
                @click="triggerFileInput(field)">
            <input
                type="file"
                :id="field"
                :ref="field"
                @change="handleFileChange($event, field)"
                style="display: none;"/>
            <span class="file-name" v-if="formData[field]">{{ formData[field].name }}</span>
            <span v-else>上传歌词</span>
          </div>
          <!--上传歌曲-->
          <div v-else-if="field.endsWith('AudioFiles')" class="image-file-upload">
            <img
                :src="formData[field] ? Icon.fileIcon : Icon.uploadIcon"
                alt="Preview"
                @click="triggerFileInput(field)">
            <input
                type="file"
                :id="field"
                :ref="field"
                @change="handleFileChange($event, field)"
                style="display: none;"/>
            <span class="file-name" v-if="formData[field]">{{ formData[field].name }}</span>
            <span v-else @click="triggerFileInput(field)">上传歌曲</span>
            <img
                :src="Icon.parseIcon"
                @click="parseSong(formData[field])"
                v-show="field === 'flacAudioFiles' && formData[field]"
                style="width: 20px"
                alt="">
          </div>
          <!--上传标签-->
          <div v-else-if="field==='tags'" style="width: 60%;">
            <input v-model="newTag" placeholder="回车新增标签" class="ipt2" style="width: 100%;">
            <button @click="addTag($event)" style="display: none">add</button>
            <div class="tags">
                            <span v-for="tag in formData[field]" :key="tag" class="tag">
                                {{ tag }}
                                <img :src="Icon.closeIcon" alt="" @click="removeTag(tag)">
                            </span>
            </div>
          </div>
          <div v-else-if="field==='artists'" style="width: 60%;">
            <input v-model="newArtist" placeholder="回车新增其他歌手" class="ipt2" style="width: 100%;">
            <button @click="addTag($event)" style="display: none">add</button>
            <div class="tags">
                            <span v-for="tag in formData[field]" :key="tag" class="tag">
                                {{ tag }}
                                <img :src="Icon.closeIcon" alt="" @click="removeTag(tag)">
                            </span>
            </div>
          </div>
        </div>
        <button type="submit">提交</button>
      </form>
    </div>
    <div class="bottom">
      <h4>列表</h4>
      <SongList v-if="this.title === '歌曲'" :songs="items" :operator="'playlist'"></SongList>
    </div>
  </div>
</template>

<script>
import SongService from "@/api/service/SongService";
import SongList from "@/pages/music/SongList.vue";
import PlaylistService from "@/api/service/PlaylistService";
import {parseBlob} from "music-metadata";
import Icon from "@/util/common/Icon";

export default {
  name: "DynamicCrud",
  computed: {
    Icon() {
      return Icon
    }
  },
  components: {SongList},
  props: {
    title: String,
    fields: Array,
  },
  data() {
    return {
      // 表单信息
      formData: {},
      // 传入列表的数据
      items: [],
      tags: [],
      newTag: '',
      artists: [],
      newArtist: '',
    };
  },
  watch: {
    // 查看字段的变化
    fields: {
      immediate: true,
      handler(fields) {
        const newFormData = {};
        fields.forEach(field => {
          if (field.includes('url') || field.includes('cover') || field.includes('lyric')) {
            this.$set(newFormData, field, null);
          } else {
            this.$set(newFormData, field, '');
          }
        });
        this.formData = newFormData;
      },
    },
    // 查看标题的变化
    title: {
      immediate: true,
      handler() {
        this.initItems();
      },
    },
  },
  methods: {
    addTag(event) {
      // 阻止默认行为和事件冒泡
      event.preventDefault();
      event.stopPropagation();
      if (this.newTag.trim() && !this.tags.includes(this.newTag.trim())) {
        this.tags.push(this.newTag.trim());
        const formTags = this.formData['tags'] || []; // 获取当前 formData['tags'] 的值，如果不存在则初始化为空数组
        formTags.push(this.newTag.trim()); // 向 formData['tags'] 数组中添加新标签
        this.$set(this.formData, 'tags', formTags); // 触发响应式更新
        this.newTag = '';
      } else {
        this.$message("重新输入标签")
      }
    },
    removeTag(tag) {
      const index = this.tags.indexOf(tag);
      if (index > -1) {
        this.tags.splice(index, 1);
        this.$set(this.formData, 'tags', [...this.tags]); // 更新 formData['tags'] 保持数据一致性
      }
    },
    async parseSong(file) {
      try {
        const metadata = await parseBlob(file);
        console.log(metadata)
        // 提取标题、歌手等信息（之前已有的代码部分）
        this.$set(this.formData, 'flacAudioFiles', file || null);
        this.$set(this.formData, 'title', metadata.common.title || '');
        this.$set(this.formData, 'artist', metadata.common.artist || '');
        this.$set(this.formData, 'album', metadata.common.album || '');
        this.$set(this.formData, 'duration', Math.ceil(metadata.format.duration) || 0);
        this.$set(this.formData, 'style', metadata.common.genre || '');
        this.$set(this.formData, 'tags', []);
        this.$set(this.formData, 'permission', 0);
        this.$set(this.formData, 'releaseDate', metadata.common.date || '');
        // 提取歌词信息

        const lyricsData = metadata.native.vorbis.find(item => item.id === 'LYRICS');
        if (lyricsData) {
          // 将歌词文本转换为 Blob 对象，模拟文件内容
          const lyricsBlob = new Blob([lyricsData.value], {type: 'text/plain'});
          // 创建歌词文件对象
          this.$set(this.formData, 'textFiles', new File([lyricsBlob], `${metadata.common.title}-${metadata.common.artist}.txt`, {type: 'text/plain'}));
        }
        if (metadata.common.picture && metadata.common.picture.length > 0) {
          const coverData = metadata.common.picture[0].data;
          const coverArrayBuffer = new ArrayBuffer(coverData.length);
          const coverUint8Array = new Uint8Array(coverArrayBuffer);
          for (let i = 0; i < coverData.length; i++) {
            coverUint8Array[i] = coverData[i];
          }
          const coverBlob = new Blob([coverUint8Array], {type: metadata.common.picture[0].format});
          this.formData['imageFiles'] = new File([coverBlob], `${metadata.common.title}-${metadata.common.artist}.jpg`, {type: metadata.common.picture[0].format});
        }
      } catch (error) {
        console.error('解析歌曲文件出错：', error);
        this.$message({
          message: '歌曲文件解析失败，请检查文件是否正确或稍后重试',
          type: 'error'
        });
        // 可以考虑在这里恢复一些表单数据状态，比如重新设置歌曲文件相关字段为空等
        this.$set(this.formData, 'formData[field]', null);
      }
    },
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
    async initItems() {
      if (this.title === "歌曲") {
        this.items = await SongService.conditionAndPaged(null, null, null, 1, 100);
      } else if (this.title === "歌单") {
        this.items = await PlaylistService.conditionAndPaged(null, 1, 100);
      }
    },
    // 获取上传的第一个文件
    handleFileChange(event, field) {
      const file = event.target.files[0];
      if (file) {
        this.formData[field] = file;
      }
    },
    // 点击上传
    handleSubmit() {
      const formDataToSubmit = {...this.formData};
      const promises = [];
      for (const field of ['url', 'cover', 'lyric']) {
        if (this.formData[field]) {
          const reader = new FileReader();
          reader.onload = (e) => {
            formDataToSubmit[field] = e.target.result;
          };
          reader.readAsDataURL(this.formData[field]);
          promises.push(new Promise((resolve) => {
            reader.onloadend = () => {
              resolve();
            };
          }));
        }
      }
      Promise.all(promises).then(() => {
        this.$emit("submit", formDataToSubmit);
        this.formData = {}; // 重置表单
      });
    },
    change(field) {
      if (field === 'title') return '歌名'
      else if (field === 'artist') return '歌手'
      else if (field === 'album') return '专辑'
      else if (field === 'duration') return '时长'
      else if (field === 'textFiles') return '歌词'
      else if (field === 'imageFiles') return '封面'
      else if (field === 'mp3AudioFiles') return 'mp3格式'
      else if (field === 'flacAudioFiles') return 'flac无损格式'
      else if (field === 'style') return '风格'
      else if (field === 'tags') return '标签'
      else if (field === 'permission') return '是否需要权限'
      else if (field === 'releaseDate') return '发行日期'
      else return '暂无该字段名'
    }
  },
  mounted() {
    this.initItems();
  },
};
</script>

<style scoped>
#dynamicCrud {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
  overflow: scroll;
}

.top {
  max-width: 100%;
}

form {
  max-width: 100%;
  display: flex;
  flex-direction: column;
  gap: 5px;
  padding: 5px;
  border-radius: 5px;
  background-color: var(--fourth-color);
}

.ipt {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.ipt1 {
  width: 38%;
  border-right: 5px solid var(--main-color);
}

.ipt2 {
  width: 60%;
  min-height: 2em;
  height: auto;
  text-indent: 1em;
  border-radius: 10px;
  transition: 0.5s;
  border: 1px solid #000000;
  outline: none;
}

.ipt img {
  width: 10%;
}

.image-file-upload {
  width: 60%;
  height: 4em;
  display: flex;
  align-items: center;
  gap: 1%;
  cursor: pointer;
}

.image-file-upload span {
}

.image-file-upload img {
  width: 30px;
  border-radius: 5px;
}

.file-name {
  width: 70%;
  text-align: start;
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

button {
  cursor: pointer;
  display: inline-block;
  font-size: 1.1em;
  color: #111;
  background: var(--second-color);
  border: none;
  border-radius: 4px;
  text-decoration: none;
  font-weight: 700;
  transition: 0.2s;
  padding: 10px;
}

button:hover {
  background: #222;
  color: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 15px #222;
}

.bottom {
  width: 50%;
  height: 70vh;
}
</style>