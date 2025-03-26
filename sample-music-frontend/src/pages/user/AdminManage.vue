<template>
  <div id="adminManage">
    <div class="nav">
      <button
          class="btn"
          v-for="b in btn"
          :key="b.btnID"
          @click="currentPage = b.btnName"
          :class="{'it': currentPage === b.btnName}">
        <span>{{ b.name }}</span>
      </button>
    </div>
    <div class="main">
      <transition name="fade">
        <DynamicCrud
            :title="pageConfig[currentPage]?.title"
            :fields="pageConfig[currentPage]?.fields"
            @submit="handleSubmit"
            @edit="handleEdit"
            @delete="handleDelete"
        ></DynamicCrud>
      </transition>
    </div>
  </div>
</template>

<script>
import DynamicCrud from "@/pages/common/DynamicCrud";
import {nanoid} from "nanoid";
import User from "@/api/pojo/User";
import SongUpload from "@/api/pojo/upload/SongUpload";
import SongManage from "@/pages/manage/SongManage.vue";
import PlaylistUpload from "@/api/pojo/upload/PlaylistUpload";
import PlaylistManage from "@/pages/manage/PlaylistManage.vue";
import AlbumManage from "@/pages/manage/AlbumManage.vue";
import AlbumUpload from "@/api/pojo/upload/AlbumUpload";
import ArtistUpload from "@/api/pojo/upload/ArtistUpload";
import ArtistManage from "@/pages/manage/ArtistManage.vue";
import UserManage from "@/pages/manage/UserManage.vue";
import PublicityUpload from "@/api/pojo/upload/PublicityUpload";
import PublicityManage from "@/pages/manage/PublicityManage.vue";

export default {
  name: "AdminManage",
  components: {DynamicCrud},
  data() {
    const songFields = Object.keys(SongUpload);
    const playlistFields = Object.keys(PlaylistUpload);
    const albumFields = Object.keys(AlbumUpload);
    const artistFields = Object.keys(ArtistUpload);
    const userFields = Object.keys(User);
    const publicityFields = Object.keys(PublicityUpload);
    return {
      currentPage: "song", // 默认显示页面
      pageConfig: {
        song: {
          title: "歌曲",
          fields: songFields,
          page: SongManage
        },
        playlist: {
          title: "歌单",
          fields: playlistFields,
          page: PlaylistManage
        },
        album: {
          title: "专辑",
          fields: albumFields,
          page: AlbumManage
        },
        artist: {
          title: "艺术家",
          fields: artistFields,
          page: ArtistManage
        },
        user: {
          title: "用户",
          fields: userFields,
          page: UserManage
        },
        publicity: {
          title: "公告",
          fields: publicityFields,
          page: PublicityManage
        },
        work: {
          title: "工单",
          fields: ["title", "artist_id", "cover", "release_date"],
        },
        comment: {
          title: "评论",
          fields: ["title", "artist_id", "cover", "release_date"],
        },
      },
      btn: [
        {btnID: nanoid(), btnName: "song", name: "歌曲"},
        {btnID: nanoid(), btnName: "playlist", name: "歌单"},
        {btnID: nanoid(), btnName: "album", name: "专辑"},
        {btnID: nanoid(), btnName: "artist", name: "艺术家"},
        {btnID: nanoid(), btnName: "user", name: "用户"},
        {btnID: nanoid(), btnName: "publicity", name: "公告"},
        {btnID: nanoid(), btnName: "work", name: "工单"},
        {btnID: nanoid(), btnName: "comment", name: "评论"},
      ],
    };
  },
  computed: {},
  methods: {
    handleSubmit(data) {
      console.log("提交数据:", data);
      // this.pageConfig[this.currentPage].items.push(JSON.stringify(data));
    },
    handleEdit(item) {
      console.log("编辑:", item);
    },
    handleDelete(index) {
      console.log("删除索引:", index);
      this.pageConfig[this.currentPage].items.splice(index, 1);
    },
  },
};
</script>

<style scoped>
#adminManage {
  width: 100%;
  height: 100%;
}

.nav {
  display: inline-flex;
  justify-content: start;
  align-items: center;
  gap: 1vw;
  margin-bottom: 1%;
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
  box-shadow: inset 2px 2px 4px #b3b3b3, inset -2px -2px 4px #ffffff !important;
}

.it span {
  color: var(--main-color);
}

.main {
  width: 96%;
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