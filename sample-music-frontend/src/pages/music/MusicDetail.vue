<template>
  <div id="musicDetail">
    <div v-if="detailType === 'album'" class="detail">
      <div class="detail-left">
        <div class="detail-cover">
          <img :src="albumDetail?.cover ?? Icon.musicColorIcon" alt="" class="blurry-image">
          <img :src="Icon.diskPng" alt="">
        </div>
        <div class="detail-message">
          <div class="dt dt1">
            <div class="box">
              <p class="cl">{{ this.classify }}</p>
              <h3 style="text-align: start;overflow: hidden;text-overflow: ellipsis">{{ albumDetail?.title }}</h3>
            </div>
          </div>

          <div class="dt dt2">
            <div class="box">
              <img :src="albumDetail?.artistAvatar" alt="">
              <p style="padding: 10px">{{ albumDetail?.artist }}</p>
            </div>
          </div>

          <div class="kick flex">
            <p>发布日期</p>
            <span class="tag">
              <img :src="Icon.calendarIcon" alt="">
              {{ albumDetail?.releaseDate }}
            </span>
          </div>

          <div class="kick flex">
            <p>收听数</p>
            <span class="tag">
              <img :src="Icon.headsetIcon" alt="">
              {{ albumDetail?.listeners }}
            </span>
          </div>

          <div class="kick flex">
            <p>收藏人数</p>
            <span class="tag">
              <img :src="Icon.notLoveIcon" alt="">
              {{ albumDetail?.likes }}
            </span>
          </div>

          <div class="kick flex" v-show="albumDetail?.tags">
            <p>标签</p>
            <span class="tag" v-for="t in albumDetail?.tags" :key="t">{{ t }}</span>
          </div>

          <div class="dt d4">
            <div class="change-button flex" @click="showSongList" :class="{'it' : this.isShowSongList}">
              <span style="white-space: nowrap;">歌曲<span style="margin-left: 5px;">{{
                  albumDetail?.songs?.length || 0
                }}</span></span>
            </div>
            <div class="change-button flex" @click="showComment" :class="{'it' : this.isShowComment}">
              <span style="white-space: nowrap;">评论<span style="margin-left: 5px;">{{
                  user_comment?.length || 0
                }}</span></span>
            </div>
          </div>

          <div class="kick flex">
            <p class="sp">{{ classify }}介绍</p>
            <span class="tag">{{ albumDetail?.description ? albumDetail?.description : "暂无介绍" }}</span>
          </div>

        </div>
      </div>
      <div v-if="isShowSongList && albumDetail?.songs?.length > 0" class="detail-right ">
        <div class="dt-top">
          <TextInput class="dt-text-input" message="搜索专辑中的歌曲"></TextInput>
          <div class="change-button" @click="playAllSongs('playlist', albumDetail)">
            <img :src="Icon.playIcon" alt="">
            <p>播放全部</p>
          </div>

          <div class="change-button" @click="changeFavoritesPlaylist(albumDetail.id)">
            <img :src="isFavorite ? Icon.loveColorIcon : Icon.notLoveIcon" alt="">
            <p>{{ isFavorite ? '取消收藏' : `收藏${this.classify}` }}</p>
          </div>
        </div>
        <SongList
            class="dt-song-list"
            :songs="songs(albumDetail?.songs)"
            :operator="'playlist'"
            @add="handleAdd"
            @delete="handleDelete"
        ></SongList>
        <PageNation
            class="dt-page-nation"
            :totalItems="albumDetail?.songs?.length || 0"
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

    <div v-else-if="detailType === 'playlist'" class="detail">
      <div class="detail-left">
        <div class="detail-cover">
          <img :src="this.playlist?.cover ?? Icon.musicColorIcon" alt="">
          <img :src="Icon.diskPng" alt="">
        </div>
        <div class="detail-message">
          <div class="dt dt1">
            <div class="box">
              <p class="cl">{{ this.classify }}</p>
              <h3 style="text-align: start;overflow: hidden;text-overflow: ellipsis">{{ this.al?.title }}</h3>
            </div>
          </div>

          <div class="dt dt2">
            <div class="box">
              <img :src="this.playlist?.userAvatar" alt="">
              <p style="padding: 10px">{{ this.playlist?.username }}</p>
            </div>
          </div>

          <div class="kick flex">
            <p>发布日期</p>
            <span class="tag">
              <img :src="Icon.calendarIcon" alt="">
              {{ this.playlist?.createTime }}
            </span>
          </div>

          <div class="kick flex">
            <p>收听数</p>
            <span class="tag">
              <img :src="Icon.headsetIcon" alt="">
              {{ this.playlist?.listeners }}
            </span>
          </div>

          <div class="kick flex">
            <p>收藏人数</p>
            <span class="tag">
              <img :src="Icon.notLoveIcon" alt="">
              {{ this.playlist?.likes }}
            </span>
          </div>

          <div class="kick flex" v-show="this.playlist?.tags">
            <p>标签</p>
            <span class="tag" v-for="t in this.playlist?.tags" :key="t">{{ t }}</span>
          </div>

          <div class="dt d4">
            <div class="change-button flex" @click="showSongList" :class="{'it' : this.isShowSongList}">
              <span style="white-space: nowrap;">歌曲<span style="margin-left: 5px;">{{
                  this.playlist?.songs?.length || 0
                }}</span></span>
            </div>
            <div class="change-button flex" @click="showComment" :class="{'it' : this.isShowComment}">
              <span style="white-space: nowrap;">评论<span style="margin-left: 5px;">{{
                  user_comment?.length || 0
                }}</span></span>
            </div>
          </div>

          <div class="kick flex">
            <p class="sp">{{ classify }}介绍</p>
            <span class="tag">{{ this.playlist?.description ? this.playlist?.description : "暂无介绍" }}</span>
          </div>

        </div>
      </div>
      <div v-if="isShowSongList && this.playlist?.songs?.length > 0" class="detail-right ">
        <div class="dt-top">
          <TextInput class="dt-text-input" message="搜索歌单中的歌曲"></TextInput>
          <div class="change-button" @click="playAllSongs('playlist', this.playlist)">
            <img :src="Icon.playIcon" alt="">
            <p>播放全部</p>
          </div>

          <div class="change-button" @click="changeFavoritesPlaylist(this.playlist.id)">
            <img :src="isFavorite ? Icon.loveColorIcon : Icon.notLoveIcon" alt="">
            <p>{{ isFavorite ? '取消收藏' : `收藏${this.classify}` }}</p>
          </div>
        </div>
        <SongList
            class="dt-song-list"
            :songs="songs(this.playlist?.songs)"
            :operator="'playlist'"
            @add="handleAdd"
            @delete="handleDelete"
        ></SongList>
        <PageNation
            class="dt-page-nation"
            :totalItems="this.playlist?.songs?.length || 0"
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

    <div v-else-if="detailType === 'artist'" class="detail">
      <div class="detail-top">
        <img :src="this.artist_detail.avatar" alt="">
        <img :src="Icon.peopleIcon" alt="">
        <div class="detail-message">
          <div class="dt d1">
            <div class="title1">
              <span>{{ this.classify }}</span>
              <h1>{{ this.artist_detail.name }}</h1>
            </div>
            <img :src="sex(this.artist_detail.sex)" alt="">
          </div>

          <div class="dt d2">

          </div>

          <div class="dt d3">
            <div class="kick flex">
              <p>收听数</p>
              <span class="tag"><img :src="Icon.headsetIcon" alt="">
                                {{ albumDetail.listeners }}
                            </span>
            </div>
            <div class="kick flex">
              <p>收藏人数</p>
              <span class="tag"><img :src="Icon.notLoveIcon" alt="">
                                {{ albumDetail.likes }}
                            </span>
            </div>
            <div class="kick flex">
              <p>标签</p>
              <span class="tag" v-for="t in tags" :key="t">{{ t }}</span>
            </div>
          </div>

          <div class="dt d4">
            <div class="select-button" @click="playAllSongs('playlist', playlistDetail)">
              <img :src="Icon.playIcon" alt="">
              <p>播放全部</p>
            </div>
            <div class="select-button" @click="changeFavoritesPlaylist(playlistDetail.id)">
              <img :src="isFavorite ? Icon.loveColorIcon : Icon.notLoveIcon" alt="">
              <p>{{ isFavorite ? '取消关注' : `关注${this.classify}` }}</p>
            </div>
            <div class="select-button" @click="showSongList" :class="{'it' : this.isShowSongList}">
              <span>歌曲<span class="nsp">{{ albumDetail.songs.length }}</span></span>
            </div>
            <div class="select-button" @click="showComment" :class="{'it' : this.isShowComment}">
              <span>评论<span class="nsp">{{ user_comment.length }}</span></span>
            </div>
          </div>
        </div>
        <div class="detail-description">
          <span class="sp">{{ classify }}介绍</span>
          <span>{{ this.artist_detail.description }}</span>
        </div>
      </div>
      <div v-if="isShowSongList" class="detail-bottom dt-songList">
        <SongList
            :songs="songs(this.playlistDetail.songs)"
            :operator="'album'"
            @add="handleAdd"
            @delete="handleDelete"
        ></SongList>
      </div>
      <div v-else-if="isShowComment" class="detail-bottom dt-comment">
        <UserComment
            :detailType="detailType"
            :targetId="targetId"
        ></UserComment>
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
import ArtistService from "@/api/service/ArtistService";
import Icon from "@/util/common/Icon";
import TextInput from "@/components/TextInput.vue";
import PageNation from "@/components/PageNation.vue";

export default {
  name: "MusicDetail",
  components: {PageNation, TextInput, UserComment, SongList},
  data() {
    return {
      detailType: "",
      isShowSongList: true,
      isShowComment: false,
      isFavorite: false,
      targetId: 1,
      tags: ["流行", "jay", "周杰伦"],
      playlist: {},
      user_comment: {},
      artist_detail: {},
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
    sex(sex) {
      if (sex === '男') return Icon.maleIcon
      else if (sex === '女') return Icon.femaleIcon
      else return Icon.unknowSexIcon
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
      if (this.playlist && favoritePlaylist) {
        if (favoritePlaylist.some(c => c.id === this.playlist.id)) {
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
      if (this.userDetail == null) {
        this.$message("未登录")
        return;
      }
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
      this.user_comment = userComment?.items || []; // 直接赋值
      await this.isFavoriteAlbum();
    },
    // 获取歌单信息
    async fetchPlaylistData(playlistId) {
      this.playlist = await PlaylistService.playlistWithSongs(playlistId);
      const userComment = await PlaylistService.queryCommentPaged(playlistId);
      this.user_comment = userComment?.items || []; // 直接赋值
      await this.isFavoritePlaylist();
    },
    jumpArtist(id) {
      this.$router.push({path: `/music/detail/artist/${id}`})
    },
    jumpUser(id) {
      this.$router.push({path: `/music/detail/artist/${id}`})
    },
    // 获取艺术家信息
    async fetchArtistData(artistId) {
      this.artist_detail = await ArtistService.selectArtistById(artistId);
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
      } else if (this.detailType === "artist") {
        this.targetId = this.$route.params.id || this.targetId;
        this.fetchArtistData(this.targetId);
        this.showSongList();
        // this.$emit('qc');
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
        this.fetchAlbumData(this.targetId);
        this.showSongList();
        this.$emit('qc');
      } else if (this.detailType === "playlist") {
        this.targetId = to.params.id || this.targetId;
        this.fetchPlaylistData(this.targetId);
        this.showSongList();
        this.$emit('qc');
      } else if (this.detailType === "artist") {
        this.targetId = this.$route.params.id || this.targetId;
        this.fetchArtistData(this.targetId);
        this.showSongList();
      } else {
        this.detailType = "";
      }
    }
  },
};
</script>

<style scoped>
#musicDetail {
  width: 98%;
  height: 98%;
  margin-top: 1%;
  margin-left: 1%;
}

@keyframes filter-animate {
  0% {
    filter: blur(10px);
  }
  100% {
    filter: blur(0);
  }
}

.blurry-image {
  transition: filter 1s ease-in-out; /* 过渡效果 */
  animation: filter-animate 1s ease-in-out;
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
  height: 90%;
  z-index: 1;
  width: auto; /* 先设置高度为自动，后续根据宽度来等比例调整高度 */
  aspect-ratio: 1 / 1; /* 设置宽高比为1:1，确保图片为正方形 */
  object-fit: contain; /* 使用 contain 属性，让图片在保持宽高比的前提下，尽可能填满容器，同时不会变形 */
  border-radius: 5px;
}

.detail-cover > img:nth-of-type(1) {
  z-index: 2;
  background-color: var(--main-color);
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