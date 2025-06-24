<template>
  <div id="MDEditor">
    <v-md-editor
        v-if="showEdit"
        v-model="editorValue"
        :disabled-menus="[]"
        @upload-image="handleUploadImage"
        height="80%"
    ></v-md-editor>
    <!-- 修改预览模式 -->
    <v-md-editor
        v-else
        style="border-radius: 10px"
        v-model="editorValue"
        mode="preview"
        :disabled-menus="['all']"
        hide-menu-bar
        class="preview-only"
    ></v-md-editor>
  </div>
</template>

<script>
export default {
  name: 'mdEditor',
  props: {
    content: String,
    showEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      editorValue: this.content || ''
    };
  },
  methods: {
    handleUploadImage(event, insertImage, files) {
      for (let i = 0; i < files.length; i++) {
        // 替换为你的上传逻辑
        this.uploadFile(files[i]).then(res => {
          insertImage({
            url: res.url,
            desc: res.name
          });
        });
      }
    },
    uploadFile(file) {
      // 示例上传逻辑，需替换为实际的上传接口
      return new Promise(resolve => {
        setTimeout(() => {
          resolve({
            url: 'https://example.com/uploaded-image.jpg',
            name: file.name
          });
        }, 1000);
      });
    }
  },
  watch: {
    editorValue(newVal) {
      this.$emit('update:content', newVal);
    },
    content(newVal) {
      // 确保父组件传入的内容能实时更新编辑器
      this.editorValue = newVal;
    }
  },
  computed: {}
};
</script>

<style scoped>
#MDEditor {
  width: 80%;
  height: 100%;
  text-align: start;
}

.preview-only {
  background-color: var(--second-color);
  border: none !important;
  padding: 0 !important;
}

.preview-only ::v-deep .github-markdown-body {
  padding: 0 !important;
  line-height: 1 !important;
  background: transparent !important;
}

.preview-only ::v-deep .github-markdown-body p {
  margin: 0 !important;
}

.preview-only ::v-deep .scrollbar__wrap {
  padding: 10px !important;
}
</style>