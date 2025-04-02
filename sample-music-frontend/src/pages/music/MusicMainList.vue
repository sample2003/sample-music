<template>
  <div id="List">
    <div id="left">
      <div class="cra flex">
        <h3>创建的歌单</h3>
        <img :src="Icon.addIcon" alt="" @click="insertPlaylist()">
      </div>
      <!-- 输入框 -->
      <TextInput style="height: 5%" method="input" message="查询歌单"></TextInput>
      <!-- 歌单 -->
      <span v-show="this.playlists.length === 0">未创建歌单</span>
      <div
          class="playlist"
          :class="{'it' : pl.id === playlistDetail.id}"
          v-for="pl in this.playlists"
          :key="pl.id"
          @click="queryPlaylistDetail(pl)"
      >
        <img :src="pl.cover ?? Icon.musicColorIcon" alt="">
        <span v-if="!isEdit">{{ pl.title }}</span>
      </div>

    </div>

    <div id="right">
      <keep-alive>
        <router-view></router-view>
      </keep-alive>
    </div>
  </div>
</template>

<script>
import PlaylistService from "@/api/service/PlaylistService";
import playlistUpload from "@/api/pojo/upload/PlaylistUpload";
import RelateService from "@/api/service/RelateService";
import Icon from "@/util/common/Icon";
import TextInput from "@/pages/common/TextInput.vue";

export default {
  name: "MusicMainList",
  components: {TextInput},
  data() {
    return {
      isEdit: false,
      playlists: [],
      playlistUpload: playlistUpload
    }
  },
  methods: {
    // 添加歌单
    insertPlaylist() {
      this.$addPlaylist();
    },
    // 初始化歌单
    async initPlaylist() {
      const res = await PlaylistService.UserPagedQuery("false", null, 1, 100);
      this.playlists = res.data.items;
    },
    // 查询歌单里的歌曲
    async queryPlaylistDetail(pl) {
      const res = await PlaylistService.playlistWithSongs(pl.id);
      this.setPlaylistDetail(res);
      const targetRoute = `/music/list/detail/playlist/${pl.id}`;
      const currentRoute = this.$router.currentRoute.path; // 获取当前路由的路径信息
      if (currentRoute !== targetRoute) {
        await this.$router.push({path: targetRoute});
      }
    },
    songs(songs) {
      return {
        total: songs.length,
        items: songs,
      }
    },
    // 增加歌单
    async insertPlaylistByName() {
      // 创建提交表单
      const formData = new FormData();
      // 遍历获得表单中的值
      formData.append("title", this.playlistUpload.title);
      if (this.playlistUpload.title.length > 15) {
        this.$message("歌单名过长")
        return;
      }
      try {
        await PlaylistService.insertPlaylist(formData);
        this.playlistUpload.title = "";
        await this.initPlaylist(); // 重新加载歌单列表
      } catch (error) {
        this.$message('添加歌单失败');
      }
    },
    // 删除歌单
    deletePlaylist(id, title) {
      this.$confirm(`是否删除歌单：${title}？`, async (confirm) => {
        if (confirm) {
          await PlaylistService.deletePlaylist(id)
          await this.initPlaylist(); // 重新加载歌单列表
        }
      })
    },
    // 从歌单移除歌曲
    handleDelete(ids) {
      this.$confirm(`是否从歌单移除选中歌曲？`, async (confirmed) => {
        if (confirmed) {
          await RelateService.deleteSongsByPlaylist(this.playlistDetail.id, ids)
          await this.queryPlaylistDetail(this.playlistDetail)
        }
      })
    },
    // 从歌单中选中删除歌曲
    deleteCheckSong() {
      this.$confirm("是否删除全部歌曲？", (confirm) => {
        if (confirm) {
          // this.delSongList(id)
          const playlist = JSON.parse(localStorage.getItem('playlist'))
          this.$store.dispatch('delSongsByPlaylist', {
            id: playlist.id,
            ids: this.songsInPlaylist.map(item => item.id),
          })
          this.getSongsByPlaylist(playlist.id)
        }
      })
    },
  },
  computed: {
    Icon() {
      return Icon
    }
  },
  mounted() {
    this.initPlaylist();
  }
}
</script>

<style scoped>
#List {
  width: 100%;
  height: 89%;
  display: flex;
}

#left {
  width: 12%;
  margin-right: 1%;
  overflow: auto;
}

.cra h3 {
  width: 80%;
  padding: 1%;
  border-radius: 5px;
  background-color: var(--main-color);
  backdrop-filter: blur(10px);
  z-index: 5;
}

.cra img {
  width: 11%;
  padding: 2%;
  cursor: pointer;
  border-radius: 50%;
  transition: all 0.2s;
}

.cra img:hover {
  background-color: var(--main-color);
}

/* 搜索框 */
.search {
  width: 96%;
  height: 5%;
  margin: 2% auto;
}

.inputBox {
  width: 100%;
  height: 100%;
  position: relative;
  text-align: center;
}

/* 歌单设计 */
.playlist {
  width: 90%;
  height: 7%;
  min-height: 40px;
  display: flex;
  justify-content: start;
  align-items: center;
  text-align: center;
  user-select: none;
  margin: 5% auto;
  cursor: pointer;
  border-radius: 8px;
  border: 1px solid #00000000;
  transition: 0.2s all ease;
  box-shadow: 2px 2px 4px #b3b3b3, -2px -2px 4px #ffffff;
}

.playlist span {
  max-width: 75%;
  line-height: 100%;
  font-size: 14px;
  text-align: start;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  transition: 0.2s all ease;
}

.playlist img {
  height: 90%;
  margin: 2%;
  border-radius: 6px;
  transition: 0.2s all ease;
  width: auto; /* 先设置高度为自动，后续根据宽度来等比例调整高度 */
  aspect-ratio: 1 / 1; /* 设置宽高比为1:1，确保图片为正方形 */
  object-fit: contain; /* 使用 contain 属性，让图片在保持宽高比的前提下，尽可能填满容器，同时不会变形 */
}

.playlist:hover {
  width: 95%;
  box-shadow: none;
  border: 1px solid var(--fourth-color);
}

.it {
  width: 95%;
  box-shadow: inset 2px 2px 4px #b3b3b3, inset -2px -2px 4px #ffffff;
  border: 1px solid #00000000;
}

.it span {
  font-weight: 600;
  color: var(--main-color);
}

#right {
  width: 86%;
  height: 100%;
  font-size: var(--fontSize);
}
</style>
