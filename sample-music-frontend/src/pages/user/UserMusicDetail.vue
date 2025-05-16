<template>
  <div id="musicDetail">
    <div v-if="detailType === 'album'" class="detail">
      <div class="detail-top">
        <img :src="albumDetail.cover" alt="">
        <img :src="Icon.diskPng" alt="">
        <div class="detail-message">
          <div class="dt d1">
            <span>{{ classify }}</span>
            <h2>{{ this.albumDetail.title }}</h2>
          </div>

          <div class="dt d2">
            <img :src="this.albumDetail.artistAvatar" alt="">
            <span>{{ this.albumDetail.artist }}</span>
            <span style="color: #888888">{{ this.albumDetail.releaseDate }}发布</span>
          </div>

          <div class="dt d3">
            <img :src="Icon.headsetIcon" alt="" style="border-left: 2px solid var(--main-color)">
            <p>收听数：<span class="nsp">{{ this.albumDetail.listeners }}</span></p>
            <img :src="Icon.notLoveIcon" alt="" style="border-left: 2px solid var(--main-color)">
            <p>收藏人数：<span class="nsp">{{ this.albumDetail.likes }}</span></p>
          </div>

          <div class="dt d3">
            <div class="select-button" @click="playAllSongs('album', albumDetail)">
              <img :src="Icon.playWhiteIcon" alt="">
              <p>播放全部</p>
            </div>
            <div class="select-button" @click="changeFavoritesAlbum(albumDetail.id)">
              <img :src="isFavorite ? Icon.loveColorIcon : Icon.notLoveIcon" alt="">
              <p>{{ isFavorite ? '取消收藏' : `收藏${this.classify}` }}</p>
            </div>
          </div>
        </div>
        <div class="detail-description">
          <span class="sp">{{ classify }}介绍</span>
          <span>{{ this.albumDetail.description }}</span>
        </div>
        <div class="change-button">
          <div @click="showSongList" :class="{'it' : isShowSongList}">
            <span>歌曲<span class="nsp">{{ this.albumDetail?.songs?.length || 0 }}</span></span>
          </div>
          <div @click="showComment" :class="{'it' : isShowComment}">
            <span>评论<span class="nsp">{{ user_comment?.length || 0}}</span></span>
          </div>
        </div>
      </div>
      <div v-if="isShowSongList" class="detail-bottom dt-songList">
        <SongList
            :songs="songs(this.albumDetail.songs)"
            :operator="'album'"
            @add="handleAdd"
            @delete="handleDelete"
        ></SongList>
      </div>
      <div v-else-if="isShowComment" class="detail-bottom dt-comment">
        <UserComment :detailType="detailType" :targetId="targetId"></UserComment>
      </div>
    </div>

    <div v-else-if="detailType === 'playlist'" class="detail">
      <div class="detail-left">
        <div class="detail-cover">
          <img :src="playlistWithSongs?.cover ?? Icon.musicColorIcon" alt="">
          <img :src="Icon.diskPng" alt="">
        </div>
        <div class="detail-message">
          <div class="dt dt1">
            <div class="box">
              <p class="cl">{{ classify }}</p>
              <h3 style="text-align: start;overflow: hidden;text-overflow: ellipsis">{{ playlistWithSongs?.title }}</h3>
            </div>
          </div>

          <div class="dt dt2">
            <div class="box">
              <img :src="playlistWithSongs?.userAvatar" alt="">
              <p style="padding: 10px">{{ playlistWithSongs?.username }}</p>
            </div>
          </div>

          <div class="kick flex">
            <p>发布日期</p>
            <span class="tag">
              <img :src="Icon.calendarIcon" alt="">
              {{ playlistWithSongs?.createTime }}
            </span>
          </div>

          <div class="kick flex">
            <p>收听数</p>
            <span class="tag">
              <img :src="Icon.headsetIcon" alt="">
              {{ playlistWithSongs?.listeners }}
            </span>
          </div>

          <div class="kick flex">
            <p>收藏人数</p>
            <span class="tag">
              <img :src="Icon.notLoveIcon" alt="">
              {{ playlistWithSongs?.likes }}
            </span>
          </div>

          <div class="kick flex" v-show="playlistWithSongs?.tags">
            <p>标签</p>
            <span class="tag" v-for="t in playlistWithSongs?.tags" :key="t">{{ t }}</span>
          </div>

          <div class="dt d4">
            <div class="change-button flex" @click="showSongList" :class="{'it' : isShowSongList}">
              <span style="white-space: nowrap;">歌曲<span style="margin-left: 5px;">{{
                  playlistWithSongs?.songs?.length || 0
                }}</span></span>
            </div>
            <div class="change-button flex" @click="showComment" :class="{'it' : isShowComment}">
              <span style="white-space: nowrap;">评论<span style="margin-left: 5px;">{{
                  user_comment?.length || 0
                }}</span></span>
            </div>
          </div>

          <div class="kick flex">
            <p class="sp">{{ classify }}介绍</p>
            <span class="tag">{{ playlistWithSongs?.description ? playlistWithSongs?.description : "暂无介绍" }}</span>
          </div>

        </div>
      </div>
      <div v-if="isShowSongList" class="detail-right ">
        <div class="dt-top">
          <TextInput class="dt-text-input" message="搜索歌单中的歌曲" v-if="playlistWithSongs?.songs.length > 0"></TextInput>
          <div class="change-button" @click="playAllSongs('playlist', playlistWithSongs)" v-if="playlistWithSongs?.songs.length > 0">
            <img :src="Icon.playIcon" alt="">
            <p>播放全部</p>
          </div>

          <div class="change-button" @click="changeFavoritesPlaylist(playlistWithSongs.id)">
            <img :src="isFavorite ? Icon.loveColorIcon : Icon.notLoveIcon" alt="">
            <p>{{ isFavorite ? '取消收藏' : `收藏${this.classify}` }}</p>
          </div>

          <div class="change-button" @click="updatePlaylist(playlistWithSongs.id)">
            <img :src="Icon.updateIcon" alt="">
            <p>修改歌单</p>
          </div>

          <div class="change-button" @click="deletePlaylist(playlistWithSongs.id, playlistWithSongs.title)">
            <img :src="Icon.deleteIcon" alt="">
            <p>删除歌单</p>
          </div>
        </div>
        <SongList
            class="dt-song-list"
            :songs="songs(playlistWithSongs?.songs)"
            :operator="'playlist'"
            @add="handleAdd"
            @delete="handleDelete"
        ></SongList>
        <PageNation
          class="dt-page-nation"
          :totalItems="playlistWithSongs?.songs?.length || 0"
          :pageSize.sync="pageSize"
          :currentPage.sync="pageNum"
          @update:pageSize="handlePageSizeChange"
          @update:currentPage="handlePageChange">
        </PageNation>
      </div>
      <div v-else class="detail-right">
        <UserComment class="dt-comment" :detailType="detailType" :targetId="targetId"></UserComment>
      </div>
    </div>
  </div>
</template>

<script>
import AlbumService from "@/api/service/AlbumService";
import SongList from "@/components/SongList.vue";
import PlaylistService from "@/api/service/PlaylistService";
import RelateService from "@/api/service/RelateService";
import FavoriteService from "@/api/service/FavoriteService";
import UserComment from "@/components/UserComment.vue";
import Icon from "@/util/common/Icon";
import PageNation from "@/components/PageNation.vue";
import TextInput from "@/components/TextInput.vue";

export default {
  name: "UserMusicDetail",
  components: {TextInput, PageNation, UserComment, SongList},
  data() {
    return {
      playlistWithSongs: {},
      detailType: "",
      isShowSongList: true,
      isShowComment: false,
      targetId: null,
      user_comment: {},
      isFavorite: false,
    };
  },
  computed: {
    Icon() {
      return Icon
    },
    classify() {
      if (this.detailType === "album") return "专辑";
      else if (this.detailType === "playlist") return "歌单";
      else if (this.detailType === "artist") return "歌手";
      else return "暂无";
    },
  },
  methods: {
    updatePlaylist(id) {
      this.$router.push({path: `/music/list/manage/playlist/${id}`});
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
    // 展示歌曲
    showSongList() {
      this.isShowComment = false;
      this.isShowSongList = true;
    },
    // 展示评论区
    showComment() {
      this.isShowSongList = false;
      this.isShowComment = true;
    },
    // 将专辑添加或移除收藏
    changeFavoritesAlbum(id) {
      this.isFavoriteAlbum().then(async isFavorite => {
        if (isFavorite) {
          await FavoriteService.removeByFavorites("album", id);
          this.isFavorite = false;
        } else {
          await FavoriteService.addToFavorites("album", id);
          this.isFavorite = true;
        }
      });
    },
    // 查询是否收藏专辑
    async isFavoriteAlbum() {
      const favoriteAlbum = await FavoriteService.queryFavoriteAlbum();
      if (this.albumDetail && favoriteAlbum) {
        if (favoriteAlbum.some(c => c.id === this.albumDetail.id)) {
          this.isFavorite = true;
          return true;
        } else {
          this.isFavorite = false;
          return false;
        }
      } else {
        return false;
      }
    },
    // 查询是否收藏歌单
    async isFavoritePlaylist() {
      const favoritePlaylist = await FavoriteService.queryFavoritePlaylist();
      if (this.playlistWithSongs && favoritePlaylist) {
        if (favoritePlaylist.some(c => c.id === this.playlistWithSongs.id)) {
          this.isFavorite = true;
          return true;
        } else {
          this.isFavorite = false;
          return false;
        }
      } else {
        return false;
      }
    },
    // 将歌单添加或移除收藏
    changeFavoritesPlaylist(id) {
      this.isFavoritePlaylist().then(async isFavorite => {
        if (isFavorite) {
          await FavoriteService.removeByFavorites("playlist", id);
          this.isFavorite = false;
        } else {
          await FavoriteService.addToFavorites("playlist", id);
          this.isFavorite = true;
        }
      });
    },
    // 格式化
    songs(songs) {
      return {
        total: songs.length,
        items: songs,
      }
    },
    // 获取专辑信息
    async fetchAlbumData(albumId) {
      const albumWithSongs = await AlbumService.albumWithSongs(albumId);
      this.setAlbumDetail(albumWithSongs);
      const userComment = await AlbumService.queryCommentPaged(albumId);
      this.user_comment = userComment.items; // 直接赋值
      await this.isFavoriteAlbum();
    },
    // 获取歌单信息
    async fetchPlaylistData(playlistId) {
      const flag = await PlaylistService.checkBelongTo(playlistId);
      if (flag) {
        this.playlistWithSongs = await PlaylistService.playlistWithSongs(playlistId);
        const userComment = await PlaylistService.queryCommentPaged(playlistId);
        this.user_comment = userComment?.items || []; // 直接赋值
        await this.isFavoritePlaylist();
      }else {
        this.$message("该歌单不属于你")
        this.$router.go(-1);
      }
    },
    // 增加歌曲至歌单
    async handleAdd(ids) {
      const playlists = await PlaylistService.UserPagedQuery("false", null, 1, 100);
      this.$change("添加歌曲到歌单", playlists.data.items, (confirmed, targetId) => {
        if (confirmed) {
          RelateService.addSongsToPlaylist(targetId, ids)
        }
      })
    },
    // 从歌单移除歌曲
    async handleDelete(ids) {
      this.$confirm(`是否从歌单移除选中歌曲？`, async (confirmed) => {
        if (confirmed) {
          await RelateService.deleteSongsByPlaylist(this.playlistWithSongs.id, ids)
          await this.queryPlaylistDetail(this.playlistWithSongs)
        }
      })
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
    // 初始化跳转参数
    initParams() {
      if (this.detailType === "album") {
        this.targetId = this.$route.params.id || this.targetId;
        this.fetchAlbumData(this.targetId);
        this.showSongList();
        this.$emit('qc');
      } else if (this.detailType === "playlist") {
        this.targetId = this.$route.params.id ?? this.targetId;
        this.fetchPlaylistData(this.targetId);
        this.showSongList();
        this.$emit('qc');
      } else {
        this.detailType = "";
      }
    },
  },
  mounted() {
    this.detailType = this.$route.params.detailType || this.detailType;
    this.initParams();
  },
  watch: {
    '$route'(to) {
      this.detailType = to.params.detailType || this.detailType;
      if (this.detailType === "album") {
        this.targetId = to.params.id || this.targetId;
        this.showSongList();
        this.fetchAlbumData(this.targetId);
        this.$emit('qc');
      } else {
        this.targetId = to.params.id || this.targetId;
        this.fetchPlaylistData(this.targetId);
        this.showSongList();
        this.$emit('qc');
      }
    }
  },
};
</script>

<style scoped>
#musicDetail {
  width: 98%;
  height: 99%;
  margin-top: 1%;
  margin-left: 1%;
}

.detail {
  width: 100%;
  height: 100%;
  display: flex;
}

.detail-left {
  width: 20%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.detail-cover {
  width: 100%;
  height: 30%;
  position: relative;
}

.detail-cover > img {
  position: absolute;
  top: 0;
  left: 0;
  height: 85%;
  z-index: 1;
  width: auto; /* 先设置高度为自动，后续根据宽度来等比例调整高度 */
  aspect-ratio: 1 / 1; /* 设置宽高比为1:1，确保图片为正方形 */
  object-fit: contain; /* 使用 contain 属性，让图片在保持宽高比的前提下，尽可能填满容器，同时不会变形 */
  border-radius: 5px;
}

.detail-cover > img:nth-of-type(1) {
  z-index: 2;
  background-color: var(--fourth-color);
}

.detail-cover > img:nth-of-type(2) {
  left: 15%;
}

.detail-message {
  max-width: 100%;
  height: 30%;
  padding: 1%;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  gap: 10px;
  box-sizing: border-box;
  justify-content: start;
  align-items: start;
  user-select: none;
  border-radius: 5px;
}

.dt {
  width: 100%;
  display: flex;
}

.box {
  display: flex;
  justify-content: start;
  align-items: center;
  max-width: 100%;
  border-radius: 8px;
  padding: 5px;
  box-sizing: border-box;
  box-shadow: var(--boxShadow);
}

.cl {
  padding: 5px 8px;
  background-color: var(--main-color);
  border-radius: 8px;
  color: var(--second-color);
  white-space: nowrap;
  align-self: center;
}

.dt2 {
  max-width: 100%;
}

.dt2 img{
  height: 40px;
  border-radius: 8px;
}

.kick {
  margin-right: 5px;
  border-radius: 5px;
  box-shadow: 2px 2px 4px #b3b3b3, -2px -2px 4px #ffffff;
}

.kick p {
  padding: 5px;
  align-self: flex-start; /* 确保顶部对齐 */
  white-space: nowrap; /* 禁止换行 */
}

.tag {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2px;
  margin: 2px;
  font-size: 12px;
  text-align: left;
  border: 1px solid var(--main-color);
  border-radius: 5px;
  background-color: #8b80af80;
  cursor: pointer;
}

.tag img {
  padding: 1px;
  width: 15px;
}

.change-button {
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  border-radius: 10px;
  margin: 0 5px;
  padding: 10px;
  transition: all 0.2s ease;
  box-sizing: border-box;
  border: 1px solid #0000;
  box-shadow: 2px 2px 4px #b3b3b3, -2px -2px 4px #ffffff;
}

.change-button:hover {
  box-shadow: none;
  border: 1px solid var(--fourth-color);
}

.change-button span {
  overflow: hidden;
  text-overflow: ellipsis;
}

.change-button img{
  width: 20px;
}


.it {
  box-shadow: inset 2px 2px 4px #b3b3b3, inset -2px -2px 4px #ffffff;
}

.it span {
  color: var(--main-color);
}

.detail-description {
  width: 30%;
  height: 50%;
  display: flex;
  flex-direction: column;
  justify-content: start;
  align-items: center;
  overflow: auto;
}

.detail-right {
  flex-grow: 1;
  height: 100%;
}

.dt-top {
  width: 100%;
  height: 5%;
  margin: 1%;
  display: flex;
}

.dt-text-input {
  width: 20% !important;
  height: 100% !important;
  margin: 0 !important;
}

.dt-song-list {
  height: 85% !important;
}

.dt-page-nation {
  height: 5% !important;
}
</style>