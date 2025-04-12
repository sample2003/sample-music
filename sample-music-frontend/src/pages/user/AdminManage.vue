<template>
  <div id="adminManage">
    <div class="nav">
      <ButtonSelect
          :button-list="btnList"
          :is-active="(item) => currentBtn === item.btnName"
          :click-param-field="'btnName'"
          :key-field="'btnID'"
          @button-click="show"
      ></ButtonSelect>
    </div>
    <div class="main">
      <transition name="fade" mode="out-in">
        <DataManagement :key="currentBtn" :param="currentBtn"></DataManagement>
      </transition>
    </div>
  </div>
</template>

<script>
import ButtonSelect from "@/pages/common/ButtonSelect.vue";
import {nanoid} from "nanoid";
import DataManagement from "@/pages/common/DataManagement.vue";

export default {
  name: "AdminManage",
  components: {DataManagement, ButtonSelect},
  data() {
    return {
      currentBtn: 'song',
      btnList: [
        {btnID: nanoid(), btnName: "song", name: "歌曲"},
        {btnID: nanoid(), btnName: "playlist", name: "歌单"},
        {btnID: nanoid(), btnName: "album", name: "专辑"},
        {btnID: nanoid(), btnName: "artist", name: "艺术家"},
        {btnID: nanoid(), btnName: "user", name: "用户"},
        {btnID: nanoid(), btnName: "publicity", name: "公告"},
        {btnID: nanoid(), btnName: "work", name: "工单"},
        {btnID: nanoid(), btnName: "comment", name: "评论"},
      ],
    }
  },
  computed: {

  },
  methods: {
    show(param) {
      this.$nextTick(() =>{
        if (param === this.currentBtn) return;
        else {
          this.currentBtn = param;
        }
      })
    },
    jump(name) {
      // 导航到新路由
      this.$router.push({path: `/user/center/manage/${name}`}, () => {
      })
    },
  },
  mounted() {

  }
};
</script>

<style scoped>
#adminManage {
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.nav {
  display: inline-flex;
  justify-content: start;
  align-items: center;
  gap: 1vw;
  height: 5%;
}

.main {
  width: 100%;
  height: 95%;
  overflow: hidden;
}

</style>