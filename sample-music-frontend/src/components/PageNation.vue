<template>
  <div class="pagination-container">
    <!-- 每页大小 -->
    <div class="page-size-selector">
      每页显示：
      <select v-model="localPageSize" @change="emitChangePageSize">
        <option v-for="option in pageSizeOptions" :key="option" :value="option">
          {{ option }}
        </option>
      </select>
    </div>
    <!-- 上一页 -->
    <button class="prev-page" v-show="currentPage > 1" @click="changePageNum(currentPage - 1)">
      <span>上</span>
    </button>
    <!-- 显示页数 -->
    <div class="page-numbers">
      <button
          v-for="(page, index) in visiblePages"
          :key="index"
          :class="{ 'page-number': true, active: page === currentPage, disabled: page === '...' }"
          :disabled="page === '...'"
          @click="changePageNum(page)"
      >
        {{ page }}
      </button>
      <button style="width: 100%" v-if="visiblePages.length === 0"> null</button>
    </div>

    <!-- 下一页 -->
    <button class="next-page" v-show="currentPage < pageCount" @click="changePageNum(currentPage + 1)">
      <span>下</span>
    </button>
    <!-- 跳转到指定页数 -->
    <div class="page-size-selector">
      跳转到：<input type="number" v-model="cp" :max="this.pageCount" @keyup.enter="changePageNum(Number(cp))">
    </div>
  </div>

</template>

<script>
export default {
  name: 'PageNation',
  props: {
    // 查询总数
    totalItems: {
      type: Number,
      required: true
    },
    // 当前页码
    pageSize: {
      type: Number,
      default: 10
    },
    // 每页大小
    pageSizeOptions: {
      type: Array,
      default: () => [10, 20, 50, 100]
    },
    // 当前页数
    currentPage: {
      type: Number,
      default: 1
    }
  },
  data() {
    return {
      localPageSize: this.pageSize,
      cp: this.currentPage,
    };
  },
  computed: {
    // 页数
    pageCount() {
      return Math.ceil(this.totalItems / this.localPageSize);
    },
    // 计算页数
    visiblePages() {
      const pages = [];
      if (this.pageCount <= 7) {
        for (let i = 1; i <= this.pageCount; i++) {
          pages.push(i);
        }
      } else {
        if (this.currentPage <= 3) {
          // 当前页码在前三页
          pages.push(1, 2, 3, '...', this.pageCount - 2, this.pageCount - 1, this.pageCount);
        } else if (this.currentPage >= this.pageCount - 2) {
          // 当前页码在后三页
          pages.push(1, 2, 3, '...', this.pageCount - 2, this.pageCount - 1, this.pageCount);
        } else {
          // 当前页码在中间
          pages.push(1, '...', this.currentPage - 1, this.currentPage, this.currentPage + 1, '...', this.pageCount);
        }
      }
      return pages;
    }
  },
  methods: {
    // 改变当前页数
    async changePageNum(page) {
      if (page < 1 || page > this.pageCount) return;
      this.$emit('update:currentPage', page); // 发射更新当前页码的事件
      this.cp = page;
    },
    // 改变页码大小
    async emitChangePageSize() {
      this.$emit('update:pageSize', this.localPageSize);
      this.$emit('update:currentPage', 1); // 发射更新当前页码的事件
      this.cp = 1; // 重置跳转框中的页码为第一页
    }
  },
  watch: {
    pageSize: {
      immediate: true,
      handler(newVal) {
        this.localPageSize = newVal;
      }
    }
  },
};
</script>

<style scoped>
/* 分页组件布局 */
.pagination-container {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  gap: 16px;
}

/* 每页大小选择 */
.page-size-selector {
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 5px;
}

.page-size-selector select {
  width: 60px;
  height: 90%;
  outline: none;
  border-radius: 5px;
}

.page-size-selector input {
  width: 60px;
  height: 90%;
  outline: none;
  text-indent: 5px;
  border-radius: 5px;
  border: 1px solid var(--fourth-color);
}

.page-size-selector input:valid,
.page-size-selector input:target {
  border: 1px solid var(--main-color);
}

/* 页数 */
.page-numbers {
  display: flex;
  justify-content: space-between;
  gap: 8px;
}

.page-number {
  width: 35px;
  height: 100%;
  border: 1px solid var(--fourth-color);
  cursor: pointer;
  border-radius: 5px;
}

.page-number.active {
  font-weight: bold;
  border-color: var(--main-color);
  background-color: var(--fourth-color);
}

.prev-page,
.next-page {
  width: 35px;
  height: 100%;
  max-width: 5%;
  border: 1px solid var(--main-color);
  border-radius: 5px;
  background-color: var(--fourth-color);
  cursor: pointer;
}

.prev-page[disabled],
.next-page[disabled] {
  background-color: #f0f0f0;
  cursor: not-allowed;
}
</style>