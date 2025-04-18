<template>
  <div id="musicDetail">
    <div v-if="detailType === 'album'" class="detail">
      <div class="detail-top">
        <img :src="albumDetail.cover ?? Icon.musicColorIcon" alt="" class="blurry-image">
        <img v-once :src="Icon.diskPng" alt="">
        <div class="detail-message">
          <div class="dt d1">
            <div class="title1">
              <span>{{ classify }}</span>
              <h1>{{ albumDetail.title }}</h1>
            </div>
          </div>

          <div class="dt d2">
            <div class="artist1 flex" @click="jumpArtist(albumDetail.artistId)">
              <img :src="albumDetail.artistAvatar ?? Icon.musicColorIcon" alt="">
              <span style="padding: 10px">{{ albumDetail.artist }}</span>
            </div>
            <div class="artist2 flex">
              <img :src="Icon.calendarIcon" alt="">
              <span style="color: #888888">{{ albumDetail.releaseDate }}发布</span>
            </div>
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
            <div class="select-button" @click="playAllSongs('album', albumDetail)">
              <img :src="Icon.playIcon" alt="">
              <p>播放全部</p>
            </div>
            <div class="select-button" @click="changeFavoritesAlbum(albumDetail.id)">
              <img :src="isFavorite ? Icon.loveColorIcon : Icon.notLoveIcon" alt="">
              <p>{{ isFavorite ? '取消收藏' : `收藏${this.classify}` }}</p>
            </div>
            <div class="change-button flex" @click="showSongList" :class="{'itt' : this.isShowSongList}">
              <span>歌曲<span class="nsp">{{ albumDetail.songs.length }}</span></span>
            </div>
            <div class="change-button flex" @click="showComment" :class="{'itt' : this.isShowComment}">
              <span>评论<span class="nsp">{{ user_comment.length }}</span></span>
            </div>
          </div>
        </div>
        <div class="detail-description">
          <span class="sp">{{ classify }}介绍</span>
          <span>{{ albumDetail.description }}</span>
        </div>
      </div>
      <div v-if="isShowSongList" class="detail-bottom dt-songList">
        <SongList
            :songs="songs(albumDetail.songs)"
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
        <img :src="this.playlist.cover ?? Icon.musicColorIcon" alt="">
        <img :src="Icon.diskPng" alt="">
        <div class="detail-message">
          <div class="dt d1">
            <div class="title1">
              <span>{{ this.classify }}</span>
              <h1>{{ this.playlist.title }}</h1>
            </div>
          </div>

          <div class="dt d2">
            <div class="artist1 flex" @click="jumpUser(playlist.userId)">
              <img :src="this.playlist.userAvatar ?? Icon.musicColorIcon" alt="">
              <span style="padding: 10px">{{ this.playlist.username }}</span>
            </div>
            <div class="artist2 flex">
              <img :src="Icon.calendarIcon" alt="">
              <span style="color: #888888">{{ this.playlist.createTime }}发布</span>
            </div>
          </div>

          <div class="dt d3">
            <div class="kick flex">
              <p>收听数</p>
              <span class="tag"><img :src="Icon.headsetIcon" alt="">
                                {{ this.playlist.listeners }}
                            </span>
            </div>
            <div class="kick flex">
              <p>收藏人数</p>
              <span class="tag"><img :src="Icon.notLoveIcon" alt="">
                                {{ this.playlist.likes }}
                            </span>
            </div>
            <div class="kick flex">
              <p>标签</p>
              <span class="tag" v-for="t in tags" :key="t">{{ t }}</span>
            </div>
          </div>

          <div class="dt d4">
            <div class="select-button" @click="playAllSongs('playlist', playlist)">
              <img :src="Icon.playIcon" alt="">
              <p style="white-space: nowrap;">播放全部</p>
            </div>
            <div class="select-button" @click="changeFavoritesPlaylist(playlist.id)">
              <img :src="isFavorite ? Icon.loveColorIcon : Icon.notLoveIcon" alt="">
              <p style="white-space: nowrap;">{{ isFavorite ? '取消收藏' : `收藏${this.classify}` }}</p>
            </div>
            <div class="select-button" @click="showSongList" :class="{'it' : this.isShowSongList}">
              <span style="white-space: nowrap;">歌曲<span class="nsp">{{ albumDetail.songs.length }}</span></span>
            </div>
            <div class="select-button" @click="showComment" :class="{'it' : this.isShowComment}">
              <span style="white-space: nowrap;">评论<span class="nsp">{{ user_comment.length }}</span></span>
            </div>
          </div>
        </div>
        <div class="detail-description">
          <span class="sp">{{ classify }}介绍</span>
          <span>{{ this.playlist.description }}</span>
        </div>
      </div>
      <div v-if="isShowSongList" class="detail-bottom dt-songList">
        <SongList
            :songs="songs(this.playlist.songs)"
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

export default {
  name: "MusicDetail",
  components: {UserComment, SongList},
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
  height: 88%;
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

.blurry-image {
  transition: filter 1s ease-in-out; /* 过渡效果 */
  animation: filter-animate 1s ease-in-out;
}

@keyframes filter-animate {
  0% {
    filter: blur(10px);
  }
  100% {
    filter: blur(0);
  }
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
  border: 1px solid #0000;
  box-shadow: 2px 2px 4px #b3b3b3, -2px -2px 4px #ffffff;
}

.artist1:hover {
  border: 1px solid var(--fourth-color);
  box-shadow: none;
}

.artist1:hover span {
  color: var(--main-color);
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
  padding: 0;
  white-space: nowrap;
}

.kick {
  border-radius: 5px;
  margin-right: 5px;
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
  user-select: none;
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
  width: 90%;
  height: 70%;
  margin-top: 1%;
  margin-left: 5%;
}
</style>