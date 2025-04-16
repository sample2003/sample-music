<template>
  <div id="userCollect">
    <div class="nav">
      <ButtonSelect
          :button-list="btn"
          :show-icon="false"
          :is-active="(item) => isCurrentRoute(item.btnName)"
          :click-param-field="'btnName'"
          :key-field="'btnID'"
          @button-click="jump"
      />
    </div>
    <div class="detail">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import {nanoid} from "nanoid";
import ButtonSelect from "@/components/ButtonSelect.vue";

export default {
  name: 'UserCollect',
  components: {ButtonSelect},
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

.detail {
  width: 100%;
  overflow: scroll;
}

</style>