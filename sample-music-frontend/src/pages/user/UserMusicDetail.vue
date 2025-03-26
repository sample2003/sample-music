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
            <span>歌曲<span class="nsp">{{ this.albumDetail.songs.length || 0 }}</span></span>
          </div>
          <div @click="showComment" :class="{'it' : isShowComment}">
            <span>评论<span class="nsp">{{ user_comment.length || 0}}</span></span>
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
      <div class="detail-top">
        <img :src="playlistDetail.cover ?? Icon.musicColorIcon" alt="">
        <img :src="Icon.diskPng" alt="">
        <div class="detail-message">
          <div class="dt d1">
            <div class="title1">
              <span>{{ this.classify }}</span>
              <h2>{{ playlistDetail.title }}</h2>
            </div>
            <img :src="Icon.updateIcon" alt="" @click="updatePlaylist(playlistDetail.id)">
          </div>

          <div class="dt d2">
            <div class="artist1 flex">
              <img :src="playlistDetail.userAvatar" alt="">
              <span style="padding: 10px">{{ playlistDetail.username }}</span>
            </div>
            <div class="artist2 flex">
              <img :src="Icon.calendarIcon" alt="">
              <span style="color: #888888">{{ playlistDetail.createTime }}发布</span>
            </div>
          </div>

          <div class="dt d3">
            <div class="kick flex">
              <p>收听数</p>
              <span class="tag"><img :src="Icon.headsetIcon" alt="">
                                {{ playlistDetail.listeners }}
                            </span>
            </div>
            <div class="kick flex">
              <p>收藏人数</p>
              <span class="tag"><img :src="Icon.notLoveIcon" alt="">
                                {{ playlistDetail.likes }}
                            </span>
            </div>
            <div class="kick flex" v-show="playlistDetail.tags">
              <p>标签</p>
              <span class="tag" v-for="t in playlistDetail.tags" :key="t">{{ t }}</span>
            </div>
          </div>

          <div class="dt d4">
            <div class="select-button" @click="playAllSongs('playlist', playlistDetail)">
              <img :src="Icon.playIcon" alt="">
              <p style="white-space: nowrap;">播放全部</p>
            </div>
            <div class="select-button" @click="changeFavoritesPlaylist(playlistDetail.id)">
              <img :src="isFavorite ? Icon.loveColorIcon : Icon.notLoveIcon" alt="">
              <p style="white-space: nowrap;">{{ isFavorite ? '取消收藏' : `收藏${this.classify}` }}</p>
            </div>
            <div class="change-button flex" @click="showSongList" :class="{'itt' : this.isShowSongList}">
              <span style="white-space: nowrap;">歌曲<span style="margin-left: 5px;">{{
                  playlistDetail.songs.length || 0
                }}</span></span>
            </div>
            <div class="change-button flex" @click="showComment" :class="{'itt' : this.isShowComment}">
              <span style="white-space: nowrap;">评论<span style="margin-left: 5px;">{{
                  user_comment.length || 0
                }}</span></span>
            </div>
          </div>
        </div>
        <div class="detail-description">
          <span class="sp">{{ classify }}介绍</span>
          <span>{{ playlistDetail.description }}</span>
        </div>
      </div>
      <div v-if="isShowSongList" class="detail-bottom dt-songList">
        <SongList
            :songs="songs(this.playlistDetail.songs)"
            :operator="'playlist'"
            @add="handleAdd"
            @delete="handleDelete"
        ></SongList>
      </div>
      <div v-else-if="isShowComment" class="detail-bottom dt-comment">
        <UserComment :detailType="detailType" :targetId="targetId"></UserComment>
      </div>
    </div>
  </div>
</template>

<script>
import AlbumService from "@/api/service/AlbumService";
import SongList from "@/pages/music/SongList.vue";
import PlaylistService from "@/api/service/PlaylistService";
import RelateService from "@/api/service/RelateService";
import FavoriteService from "@/api/service/FavoriteService";
import UserComment from "@/pages/common/UserComment.vue";
import tools from "@/util/common/tools";
import {PlaylistUpdate} from "@/api/pojo/update/PlaylistUpdate";
import Icon from "@/util/common/Icon";

export default {
  name: "UserMusicDetail",
  components: {UserComment, SongList},
  data() {
    return {
      detailType: "",
      isShowSongList: true,
      isShowComment: false,
      targetId: 1,
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
      const pu = tools.copy(this.playlistDetail, PlaylistUpdate)
      this.$router.push({
        path: `/music/list/manage/playlist/${id}`,
        query: {fields: pu}
      });
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
      if (this.playlistDetail && favoritePlaylist) {
        if (favoritePlaylist.some(c => c.id === this.playlistDetail.id)) {
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
      const playlistWithSongs = await PlaylistService.playlistWithSongs(playlistId);
      // this.setPlaylistDetail(playlistWithSongs);
      const userComment = await PlaylistService.queryCommentPaged(playlistId);
      this.user_comment = userComment?.items || []; // 直接赋值
      await this.isFavoritePlaylist();
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
          await RelateService.deleteSongsByPlaylist(this.playlistDetail.id, ids)
          await this.queryPlaylistDetail(this.playlistDetail)
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
  created() {
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
  flex-direction: column;
  overflow: auto;
}

.detail-top {
  width: 100%;
  padding-top: 15%;
  position: relative;
}

.detail-top > img {
  position: absolute;
  top: 0;
  left: 0;
  width: 15%;
  z-index: 1;
  height: auto; /* 先设置高度为自动，后续根据宽度来等比例调整高度 */
  aspect-ratio: 1 / 1; /* 设置宽高比为1:1，确保图片为正方形 */
  object-fit: contain; /* 使用 contain 属性，让图片在保持宽高比的前提下，尽可能填满容器，同时不会变形 */
  border-radius: 5px;
}

.detail-top > img:nth-of-type(1) {
  left: 5%;
  z-index: 2;
  box-shadow: 4px 4px 8px #b3b3b3;
  background-color: var(--main-color);
}

.detail-message {
  max-width: 40%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 22%;
  padding: 1%;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: start;
  user-select: none;
  border-radius: 5px;
  box-shadow: inset 2px 2px 4px #b3b3b3, inset -2px -2px 4px #ffffff;
}

.dt {
  width: 100%;
  height: 20%;
  display: flex;
  justify-content: start;
  align-items: center;
}

.title1 {
  display: flex;
  border-radius: 8px;
  padding: 5px;
  box-shadow: 2px 2px 4px #b3b3b3, -2px -2px 4px #ffffff;
}

.title1 > span:nth-of-type(1) {
  padding: 5px 8px;
  background-color: var(--main-color);
  border-radius: 8px;
  color: var(--second-color);
  white-space: nowrap;
  align-self: center;
}

.title1 h1 {
  max-width: 80%;
  max-height: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  text-align: start;
  white-space: nowrap;
}

.d1 > img {
  width: 6%;
  cursor: pointer;
}

.d2 > span img {
  height: 6%;
}

.artist1 {
  height: 100%;
  cursor: pointer;
  border-radius: 50px;
  margin-right: 10px;
  transition: all 0.2s;
  box-shadow: 2px 2px 4px #b3b3b3, -2px -2px 4px #ffffff;
}

.artist1:hover {
  box-shadow: none;
}

.artist1 img {
  height: 100%;
  border-radius: 50%;
}

.artist2 {
  height: 80%;
}

.artist2 img {
  height: 70%;
}

.d3 {
  white-space: nowrap;
}

.kick {
  margin-right: 5px;
  border-radius: 5px;
  box-shadow: 2px 2px 4px #b3b3b3, -2px -2px 4px #ffffff;
}

.kick p {
  padding: 5px;
}

.tag {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2px;
  margin: 2px;
  font-size: 12px;
  border: 1px solid var(--main-color);
  border-radius: 5px;
  background-color: #8b80af80;
  cursor: pointer;
}

.tag img {
  padding: 1px;
  width: 15px;
}

.d4 img {
  height: 60%;
}

.select-button {
  height: 80%;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0 2%;
  margin-right: 1%;
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.2s;
  border: 1px solid var(--main-color);
  box-shadow: 2px 2px 4px #b3b3b3, -2px -2px 4px #ffffff;
}

.select-button:hover {
  box-shadow: none;
}

.change-button {
  height: 80%;
  padding: 0 2%;
  cursor: pointer;
  position: relative;
  justify-content: center;
  border-radius: 5px;
  transition: all 0.2s;
  box-shadow: none;
}

.change-button::after {
  content: '';
  background: var(--main-color);
  width: 0;
  height: 2px;
  position: absolute;
  bottom: 0;
  left: 50%;
  transition: all 0.2s;
}

.change-button:hover::after {
  width: 100%;
  left: 0;
}

.itt {
  background: var(--main-color);
  box-shadow: none;
}

.detail-description {
  width: 30%;
  height: 100%;
  position: absolute;
  top: 0;
  right: 0;
  display: flex;
  flex-direction: column;
  justify-content: start;
  align-items: center;
  overflow: auto;
}

.detail-bottom {
  width: 100%;
  height: 65%;
  margin-top: 1%;
}
</style>