<template>
  <div id="Side">
    <div class="top">
      <h1><span>样本</span>音乐</h1>
    </div>
    <div class="side-items">
      <div
          class="side-item"
          v-for="(b,index) in btn"
          :key="index"
          :ref="b.btnName"
          :class="{ 'it' : $route.name.includes(b.btnR) }"
          @click="jump(b.btnR)"
      >
        <img :src="b.btnIcon" alt="">
        <span class="flex">{{ b.btnName }}</span>
      </div>
    </div>
  </div>
</template>

<script>
import {nanoid} from "nanoid";
import Icon from "@/util/common/Icon";

export default {
  name: "MusicSide",
  data() {
    return {
      btn: [
        {
          btnID: nanoid(),
          btnName: "首页",
          btnR: "home",
          btnIcon: Icon.homeIcon
        },
        {
          btnID: nanoid(),
          btnName: "收纳",
          btnR: "list",
          btnIcon: Icon.listIcon
        },
        {
          btnID: nanoid(),
          btnName: "搜索",
          btnR: "search",
          btnIcon: Icon.searchIcon
        },
        {
          btnID: nanoid(),
          btnName: "播放",
          btnR: "play",
          btnIcon: Icon.diskIcon
        },
        {
          btnID: nanoid(),
          btnName: "聊天",
          btnR: "AI",
          btnIcon: Icon.deepseekIcon
        },
        {
          btnID: nanoid(),
          btnName: "设置",
          btnR: "set",
          btnIcon: Icon.noteIcon
        },
      ]
    }
  },
  methods: {
    jump(name) {
      // 导航到新路由
      if (name === 'list') {
        if (this.playlistDetail && this.playlistDetail.id) this.$router.push({path: `/music/${name}/detail/playlist/${this.playlistDetail.id}`});
        else this.$router.push({path: `/music/${name}`});
      } else {
        this.$router.push({path: `/music/${name}`}, () => {
        })
      }
    },
  },
  mounted() {
  },
  beforeDestroy() {
  }
}
</script>

<style scoped>
#Side {
  min-width: 80px;
  min-height: 540px;
  display: grid;
  grid-template-rows: 10% 40% 50%;
}

.top {
  grid-row: 1 / span 1;
  display: flex;
  flex-direction: column;
  justify-content: start;
  align-items: center;
}

.top h1 {
  margin-top: 10px;

}

.top span {
  color: var(--main-color);
  animation: changeTextColor 4s ease infinite running;
}

@keyframes changeTextColor {
  0% {
    color: var(--main-color);
  }
  50% {
    color: var(--fourth-color);
  }
  100% {
    color: var(--main-color);
  }
}

/* 按钮区域 */
.side-items {
  width: calc(100% - 20px);
  grid-row: 2 / span 1;
  display: grid;
  border-radius: 5px;
  padding: 5px 0;
  margin-left: 10px;
  grid-template-rows: repeat(6, 1fr);
}

/* 按钮统一格式 */
.side-item {
  width: 80%;
  height: 80%;
  margin: auto;
  border-radius: 8px;
  transition: all 0.3s ease;
  cursor: pointer;
  border: 1px solid #00000000;
  text-align: center;
  background-color: var(--second-color);
  box-shadow: 2px 2px 4px #b3b3b3, -2px -2px 4px #ffffff;
  position: relative;
}

/* 按钮图标 */
.side-item img {
  width: 20%;
  height: 100%;
  transition: 0.3s ease;
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  margin: auto;
}

/* 按钮文本 */
.side-item span {
  width: 100%;
  height: 100%;
  opacity: 0;
  color: inherit;
  transition: 0.3s ease;

  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  margin: auto;
  pointer-events: none;
}

/* 悬浮在按钮上 */
.side-item:hover {
  width: 90%;
  box-shadow: none;
  border: 1px solid var(--fourth-color);
}

.side-item:hover img, .it img {
  width: 22%;
  right: 45%;
}

.side-item:hover span {
  opacity: 1;
  left: 15%;
  transition: opacity 0.1s ease 0.2s;
}

.it {
  width: 90%;
  box-shadow: inset 2px 2px 4px #b3b3b3, inset -2px -2px 4px #ffffff;
}

.it span {
  opacity: 1;
  left: 15%;
  font-weight: 600;
  color: var(--main-color);
  transition: opacity 0.1s ease 0.2s;
}
</style>