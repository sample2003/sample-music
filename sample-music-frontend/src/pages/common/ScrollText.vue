<!-- ScrollText.vue -->
<template>
  <div class="title-artist" :style="{ maxWidth: maxWidth }">
    <div ref="container" class="title">
      <span ref="text" class="song-title" :style="{ fontSize: fontSize }">{{ songPlaying.title || '暂无歌曲' }}</span>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ScrollText',
  props: {
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

      // 清除旧动画
      if (this.animation) {
        this.animation.cancel()
        this.animation = null
      }

      // 重置位置
      text.style.transform = 'none'

      const isOverflow = text.scrollWidth > container.clientWidth
      if (isOverflow) {
        text.style.paddingLeft = container.clientWidth + 'px'
        text.style.paddingRight = container.clientWidth + 'px'
        container.style.textAlign = 'center'
        const scrollDistance = text.scrollWidth - container.clientWidth

        // 创建动画
        this.animation = text.animate([
          {
            transform: 'translateX(0)',
          }, {
            transform: `translateX(-${scrollDistance}px)`,
          }
        ], {
          duration: (scrollDistance / 50) * 1000,
          iterations: Infinity
        })
      } else {
        container.style.textAlign = 'center'
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
  overflow: hidden; /* 隐藏超出部分 */
  white-space: nowrap; /* 防止换行 */
}

.song-title {
  font-weight: 600;
  color: var(--main-color);
  display: inline-flex;
  backface-visibility: hidden; /* 防止闪烁 */
}
</style>