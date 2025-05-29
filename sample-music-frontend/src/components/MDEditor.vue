<template>
  <div id="MDEditor">
    <v-md-editor
        v-if="showEdit"
        v-model="editorValue"
        :disabled-menus="[]"
        @upload-image="handleUploadImage"
        height="calc(80vh)"
    ></v-md-editor>
    <div v-else v-html="editorValue"></div>
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
    }
  },
  computed: {

  }
};
</script>

<style scoped>
#MDEditor {
  width: 100%;
  height: 100%;
  text-align: start;
}
</style>