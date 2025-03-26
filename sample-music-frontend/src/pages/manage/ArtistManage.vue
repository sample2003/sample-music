<template>
  <div>
    <h3>艺术家管理</h3>
    <div>
      <!-- 渲染传入的表单 -->
      <form @submit.prevent="handleSubmit">
        <div v-for="(field, index) in fields" :key="index">
          <label :for="field">{{ field }}</label>
          <input v-if="!field?.endsWith('Files')" :id="field" v-model="formData[field]"/>
          <input v-else type="file" :id="field" @change="handleFileChange($event, field)"/>
        </div>
        <button type="submit">提交</button>
      </form>
    </div>
    <div>
      <!-- 渲染列表 -->
      <h4>列表</h4>
    </div>
  </div>
</template>

<script>

import ArtistService from "@/api/service/ArtistService";

export default {
  props: {
    title: String,
    fields: Array, // 字段名称
    items: Array, // 数据列表
  },
  data() {
    return {
      formData: {}, // 表单数据
    };
  },
  methods: {
    handleFileChange(event, field) {
      const file = event.target.files[0];
      if (file) {
        this.formData[field] = file;
      }
    },
    async handleSubmit() {
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

      try {
        await ArtistService.insertArtist(formData);
        // 处理响应
        this.$message("上传艺术家信息成功")
      } catch (error) {
        console.error('上传失败:', error);
      }
    },
  },
};
</script>