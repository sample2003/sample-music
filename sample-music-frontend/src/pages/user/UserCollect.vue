<template>
  <div id="userCollect">
    <div class="nav">
      <button
          class="btn"
          v-for="b in btn"
          :key="b.btnID"
          @click="jump(b.btnName)"
          :class="{'it': isCurrentRoute(b.btnName)}">
        <span>{{ b.name }}</span>
      </button>
    </div>
    <div class="detail">
      <transition name="fade">
        <router-view></router-view>
      </transition>
    </div>
  </div>
</template>

<script>
import {nanoid} from "nanoid";

export default {
  name: 'UserCollect',
  data() {
    return {
      btn: [
        {
          btnID: nanoid(),
          btnName: "playlist",
          name: "歌单",
        },
        {
          btnID: nanoid(),
          btnName: "album",
          name: "专辑",
        },
        {
          btnID: nanoid(),
          btnName: "song",
          name: "歌曲",
        },
        {
          btnID: nanoid(),
          btnName: "artist",
          name: "艺术家",
        },
      ]
    }
  },
  methods: {
    isCurrentRoute(btnName) {
      // 检查当前路由的name是否与按钮名称匹配
      const matchedRoute = this.$route.matched.find(record => record.name === btnName);
      if (matchedRoute) {
        return true;
      }
      // 检查当前路由的path是否与按钮名称匹配
      const currentPath = this.$route.path.split('/').pop();
      return currentPath === btnName;
    },
    jump(name) {
      // 导航到新路由
      this.$router.push({path: `/user/center/collect/music/${name}`}, () => {
      })
    },
  }
}
</script>

<style scoped>
#userCollect {
  width: 100%;
  height: 100%;
  background-color: var(--second-color);
}

.nav {
  display: inline-flex;
  justify-content: start;
  align-items: center;
  gap: 1vw;
}

.btn {
  border: 1px solid #0000;
  padding: 5px;
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.2s;
  background-color: var(--second-color);
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

.detail {
  width: 100%;
  overflow: scroll;
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