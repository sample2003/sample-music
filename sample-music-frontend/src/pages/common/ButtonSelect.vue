<template>
  <div id="ButtonSelect">
    <div
        class="btns"
        v-for="b in btn"
        :key="`btn-${b.id}`"
    >
      <button
          class="btn flex"
          @click="jump(b.btnName)"
          :class="{'it': isCurrentRoute(b.btnName)}"
      >
        <img v-if="b.btnIcon" :src="b.btnIcon" alt="">
        <span>{{ b.name }}</span>
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: "ButtonSelect",
  props: {
    method: {
      type: String,
      default: 'input',
    },
    btn: {
      type: Array,
      default: () => []
    }
  },
  methods: {
    jump(btnName) {
      // 示例路由跳转，根据实际项目调整
      if (this.$route.name !== btnName) {
        this.$router.push({name: btnName})
      }
    },
    select(btnName) {
      // 示例路由跳转，根据实际项目调整
      if (this.$route.name !== btnName) {
        this.$router.push({name: btnName})
      }
    },
    isCurrentRoute(btnName) {
      return this.$route.name === btnName
    }
  }
}
</script>

<style scoped>
#ButtonSelect {
  display: flex;

}

.btns {
  white-space: nowrap;
  margin: 0 1%;
}

.btns img {
  width: 20%;
}

.btn {
  cursor: pointer;
  border-radius: 10px;
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

.it {
  box-shadow: inset 2px 2px 4px #b3b3b3, inset -2px -2px 4px #ffffff;
}

.it span {
  color: var(--main-color);
}
</style>