<template>
  <div id="Main" class="flex">
    <!-- 内容区 -->
    <div class="main">
      <transition name="fade">
        <keep-alive>
          <router-view></router-view>
        </keep-alive>
      </transition>
    </div>

  </div>
</template>

<script>

export default {
  name: "MusicMain",
  data() {
    return {}
  },
  methods: {},
  computed: {}
}
</script>

<style scoped>
#Main {
}

.main {
  width: 100%;
  height: 100%;
}

/* 元素进入（淡入）阶段 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease; /* 设置过渡动画的时长为0.5秒，缓动函数为ease，这里时长可根据需求调整 */
  opacity: 1; /* 设置元素进入和离开时的初始透明度为0，实现淡入淡出的视觉起点和终点效果 */
}

.fade-enter,
.fade-leave-to {
  opacity: 0; /* 设置元素进入和离开时的初始透明度为0，实现淡入淡出的视觉起点和终点效果 */
}

.fade-enter-to,
.fade-leave {
  opacity: 1; /* 设置元素进入完成和离开前的透明度为1，也就是完全可见的状态 */
}
</style>