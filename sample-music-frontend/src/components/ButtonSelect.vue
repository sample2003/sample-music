<!-- Button组件改造后 -->
<template>
  <div id="ButtonSelect">
    <div
        class="btns"
        v-for="btn in buttonList"
        :key="getKey(btn)"
    >
      <button
          class="btn flex"
          @click="handleClick(btn)"
          :class="{ 'it': isButtonActive(btn) }"
      >
        <img v-if="getIcon(btn)" :src="getIcon(btn)" alt="">
        <p>{{ getLabel(btn) }}</p>
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ButtonSelect',
  props: {
    // 按钮配置数组
    buttonList: {
      type: Array,
      required: true
    },
    // 激活状态判断函数
    isActive: {
      type: Function,
      default: () => false
    },
    // 图标字段名或获取方法
    iconField: {
      type: [String, Function],
      default: 'btnIcon'
    },
    // 文本字段名或获取方法
    labelField: {
      type: [String, Function],
      default: 'name'
    },
    // 点击参数字段名或获取方法
    clickParamField: {
      type: [String, Function],
      default: 'fx'
    },
    // 唯一键字段名或获取方法
    keyField: {
      type: [String, Function],
      default: 'id'
    }
  },
  methods: {
    getKey(item) {
      return typeof this.keyField === 'function'
          ? this.keyField(item)
          : item[this.keyField];
    },
    getIcon(item) {
      return typeof this.iconField === 'function'
          ? this.iconField(item)
          : item[this.iconField];
    },
    getLabel(item) {
      return typeof this.labelField === 'function'
          ? this.labelField(item)
          : item[this.labelField];
    },
    getClickParam(item) {
      return typeof this.clickParamField === 'function'
          ? this.clickParamField(item)
          : item[this.clickParamField];
    },
    isButtonActive(item) {
      return this.isActive(item);
    },
    handleClick(item) {
      this.$emit('button-click', this.getClickParam(item));
    }
  },
  mounted() {

  }
};
</script>

<style scoped>
#ButtonSelect {
  display: flex;
  justify-content: center;
  align-items: center;
}

.btns {
  white-space: nowrap;
}

.btns img {
  width: 20%;
}

.btn {
  cursor: pointer;
  border-radius: 10px;
  margin: 5px;
  padding: 10px;
  transition: all 0.2s;
  box-sizing: border-box;
  border: 1px solid #0000;
  box-shadow: 2px 2px 4px #b3b3b3, -2px -2px 4px #ffffff;
}

.btn:hover {
  box-shadow: none;
  border: 1px solid var(--fourth-color);
}

.btn p {
  overflow: hidden;
  text-overflow: ellipsis;
}

.it {
  box-shadow: inset 2px 2px 4px #b3b3b3, inset -2px -2px 4px #ffffff;
}

.it p {
  color: var(--main-color);
}
</style>