<!-- components/ErrorPage.vue -->
<template>
  <div>
    <h1>404 - 页面未找到</h1>
    <p>抱歉，您访问的页面不存在。</p>
    <button @click="goBack">返回</button>
  </div>
</template>

<script>
export default {
  name: 'ErrorPage',
  methods: {
    goBack() {
      if (window.history.length > 1) {
        window.history.back();
      } else {
        this.$router.push('/'); // 如果没有历史记录，跳转到首页
      }
    }
  }
};
</script>