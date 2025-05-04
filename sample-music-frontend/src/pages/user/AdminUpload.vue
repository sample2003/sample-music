<template>
  <div id="AdminUpload">
    <form @submit.prevent="handleSubmit">
      <h3>上传歌曲</h3>
      <div class="content">
        <div class="ipt" v-for="(field, index) in fields" :key="index">
          <!-- 字段名 -->
          <label class="label" :for="field.title">{{ field.title }}</label>

          <!-- 输入值 -->
          <input
              class="input"
              v-if="check(field)"
              :id="field.key"
              placeholder="请输入"
              v-model="formData[field.key]"/>

          <!--上传图片-->
          <div v-else-if="field.key.endsWith('vatar')" class="file-upload">
            <img
                :src="formData[field.key] ? getURL().createObjectURL(formData[field.key]) : Icon.uploadIcon"
                alt="Preview"
                @click="triggerFileInput(field.key)">
            <input
                type="file"
                :id="field.key"
                :ref="field.key"
                @change="handleFileChange($event, field.key)"
                style="display: none;"/>
            <span class="file-name" v-if="formData[field.key]">{{ formData[field.key].name }}</span>
          </div>
          <div v-else-if="field.key.endsWith('over')" class="file-upload">
            <img
                :src="formData[field.key] ? getURL().createObjectURL(formData[field.key]) : Icon.uploadIcon"
                alt="Preview"
                @click="triggerFileInput(field.key)">
            <input
                type="file"
                :id="field.key"
                :ref="field.key"
                @change="handleFileChange($event, field.key)"
                style="display: none;"/>
            <span class="file-name" v-if="formData[field.key]">{{ formData[field.key].name }}</span>
          </div>

          <!--上传歌词-->
          <div v-else-if="field.key.endsWith('lyric')" class="file-upload">
            <img
                :src="formData[field.key] ? Icon.fileIcon : Icon.uploadIcon"
                alt="Preview"
                @click="triggerFileInput(field.key)">
            <input
                type="file"
                :id="field.key"
                :ref="field.key"
                @change="handleFileChange($event, field.key)"
                style="display: none;"/>
            <span class="file-name" v-if="formData[field.key]">{{ formData[field.key].name }}</span>
          </div>

          <!--上传歌曲-->
          <div v-else-if="field.key.endsWith('Url')" class="file-upload">
            <img
                :src="formData[field.key] ? Icon.fileIcon : Icon.uploadIcon"
                alt="Preview"
                @click="triggerFileInput(field.key)">
            <input
                type="file"
                :id="field.key"
                :ref="field.key"
                @change="handleFileChange($event, field.key)"
                style="display: none;"/>
            <span class="file-name" v-if="formData[field.key]">{{ formData[field.key].name }}</span>
            <img
                :src="Icon.parseIcon"
                @click="parseSong(formData[field.key])"
                v-show="formData[field.key]"
                style="width: 20px"
                alt="">
          </div>

          <!--上传标签-->
          <div v-else-if="field.key === 'tags'" style="display: flex;flex-wrap: wrap;">
            <input v-model="newTag" placeholder="回车新增标签" class="input" style="width: 100%;">
            <button @click="addTag($event)" style="display: none">add</button>
            <div class="tags">
              <span v-for="tag in formData[field.key]" :key="tag" class="tag">
                  {{ tag }}
                  <img :src="Icon.closeIcon" alt="" @click="removeTag(tag)">
              </span>
            </div>
          </div>

          <div v-else-if="field.key === 'artists'" style="width: 60%;">
            <input v-model="newArtist" placeholder="回车新增其他歌手" class="input" style="width: 100%;">
<!--            <button @click="addTag($event)" style="display: none">add</button>-->
            <div class="tags">
              <span v-for="tag in formData[field.key]" :key="tag" class="tag">
                  {{ tag }}
                  <img :src="Icon.closeIcon" alt="" @click="removeTag(tag)">
              </span>
            </div>
          </div>
        </div>
      </div>

      <button type="submit">提交</button>
    </form>
  </div>
</template>

<script>
import {parseBlob} from "music-metadata";
import Icon from "@/util/common/Icon";

export default {
  name: "DynamicCrud",
  computed: {
    Icon() {
      return Icon
    }
  },
  props: [],
  data() {
    return {
      fields: [],
      formData: {},
      // 表单信息
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
          if (field.key.includes('url') || field.key.includes('cover') || field.key.includes('lyric')) {
            this.$set(newFormData, field.key, null);
          } else {
            this.$set(newFormData, field.key, '');
          }
        });
        this.formData = newFormData;
        console.log(this.formData)
      },
    },
  },
  methods: {
    check(field) {
      return!field.key.endsWith('over') &&
          !field.key.endsWith('vatar') &&
          !field.key.endsWith('lyric') &&
          !field.key.endsWith('Url') &&
          field.key !== 'tags'
    },
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
          this.$set(this.formData, 'lyric', new File([lyricsBlob], `${metadata.common.title}-${metadata.common.artist}.txt`, {type: 'text/plain'}));
        }
        if (metadata.common.picture && metadata.common.picture.length > 0) {
          const coverData = metadata.common.picture[0].data;
          const coverArrayBuffer = new ArrayBuffer(coverData.length);
          const coverUint8Array = new Uint8Array(coverArrayBuffer);
          for (let i = 0; i < coverData.length; i++) {
            coverUint8Array[i] = coverData[i];
          }
          const coverBlob = new Blob([coverUint8Array], {type: metadata.common.picture[0].format});
          this.formData['cover'] = new File([coverBlob], `${metadata.common.title}-${metadata.common.artist}.jpg`, {type: metadata.common.picture[0].format});
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
  },
  mounted() {
    // 从路由query中获取参数
    const arrayParam = this.$route.query.arrayParam;
    if (arrayParam) {
      // const arrayData = JSON.parse(arrayParam);
      // this.fields = arrayData.map(item => item.key); // 提取key作为字段名
      this.fields = JSON.parse(arrayParam);
      console.log(arrayParam)
    }
  },
};
</script>

<style scoped>


#AdminUpload {
  width: 100%;
}

form {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.content {
  width: 100%;
  display: flex;
  align-items: start;
  justify-content: start;
  flex-wrap: wrap;
}

.ipt {
  max-width: 100%;
  display: flex;
  align-items: center;
  margin: 1%;
  gap: 5px;
  padding: 5px;
  border-radius: 10px;
  background-color: var(--fourth-color);
}

.label {
  white-space: nowrap;
}

.input {
  min-height: 2em;
  height: auto;
  text-indent: 1em;
  border-radius: 10px;
  transition: 0.5s;
  border: 1px solid #000000;
  outline: none;
}

.file-upload {
  height: 4em;
  display: flex;
  align-items: center;
  gap: 1%;
  cursor: pointer;
}

.file-upload img {
  height: 100%;
  border-radius: 5px;
}

.file-name {
  white-space: nowrap;
  text-align: start;
}

.tags {
  max-width: 100%;
  display: flex;
  justify-content: start;
  gap: 5px;
}

.tag {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2px;
  margin: 2px;
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
  max-width: 20%;
  cursor: pointer;
  display: inline-block;
  font-size: 1.1em;
  background: #222;
  color: #fff;
  box-shadow: 0 2px 15px #222;
  border-radius: 5px;
  text-decoration: none;
  font-weight: 700;
  transition: 0.2s all ease;
  padding: 5px 0;
  margin: 15px;
}

button:hover {
  color: #222;
  background: #fff;
  box-shadow: 0 2px 15px #fff;
}
</style>