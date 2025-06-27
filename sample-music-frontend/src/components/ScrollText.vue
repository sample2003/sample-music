<template>
  <div class="title-artist" :style="{ maxWidth: this.maxWidth }">
    <div ref="container" class="title">
      <span ref="text" class="song-title" :style="{ fontSize: this.fontSize, color: this.color }">{{ this.content || '暂无歌曲' }}</span>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ScrollText',
  props: {
    content: String,
    color: {  // 字体颜色
      type: String,
      default: 'var(--main-color)'
    },
    maxWidth: {  // 控制容器最大宽度
      type: String,
      default: '75%'
    },
    fontSize: {  // 控制字体大小
      type: String,
      default: null
    }
  },
  data() {
    return {
      animation: null // 增加动画实例引用
    }
  },
  methods: {
    // 检测滚动显示
    checkScroll() {
      const container = this.$refs.container
      const text = this.$refs.text
      if (!container || !text) return

      // 记录原始容器宽度
      const originalWidth = container.clientWidth

      // 清除旧动画和样式
      if (this.animation) {
        this.animation.cancel()
        this.animation = null
        text.style.paddingLeft = 0
        text.style.paddingRight = 0
      }

      // 先移除padding计算真实宽度
      const isOverflow = text.scrollWidth > originalWidth

      if (isOverflow) {
        // 设置padding触发滚动空间
        text.style.paddingLeft = originalWidth + 'px'
        text.style.paddingRight = originalWidth + 'px'

        // 重新计算滚动距离（使用原始宽度）
        const scrollDistance = text.scrollWidth - originalWidth

        // 创建动画
        this.animation = text.animate([
          { transform: 'translateX(0)' },
          { transform: `translateX(-${scrollDistance}px)` }
        ], {
          duration: (scrollDistance / 50) * 1000,
          iterations: Infinity
        })
      } else {
        container.style.textAlign = 'center'
        text.style.transform = 'none' // 重置位置
      }
    }
  },
  mounted() {
    this.checkScroll()
    window.addEventListener('resize', this.checkScroll)
  },
  beforeDestroy() {
  }
}
</script>

<style scoped>
.title-artist {
  height: auto;
  overflow: hidden; /* 隐藏超出部分 */
  white-space: nowrap; /* 防止换行 */
}

.title {
  height: 100%;
  display: inline-flex; /* 内联块级元素 */
  align-items: center; /* 垂直居中 */
}

.song-title {
  font-weight: 600;
  line-height: 100%;
  display: inline-flex;
  backface-visibility: hidden; /* 防止闪烁 */
}
</style>