<template>
  <div id="userCollectMusic">
    <div key="playlist" v-if="type === 'playlist'" class="collect">
      <div class="pl">
        <span>我创建的</span>
        <div
            class="card_detail"
            v-for="playlist in this.playlists"
            :key="playlist.id"
        >
          <div class="card_body">
            <img :src="playlist.cover" alt="">
            <span>{{ playlist.songs.length }}首</span>
            <img :src="Icon.playWhiteIcon" alt=""
                 @click="jumpListPlaylistDetail(playlist.id)">
            <img :src="Icon.headsetIcon" alt="">
            <img :src="Icon.headsetIcon" alt="">
          </div>
          <span>{{ playlist.title }}</span>
          <span>{{ playlist.createTime }}</span>
        </div>
        <div v-show="this.playlists.length === 0">
          <h3>未创建歌单</h3>
        </div>
      </div>
      <div class="pl">
        <span>我收藏的</span>
        <div
            class="card_detail"
            v-for="fp in favoritePlaylist"
            :key="fp.id"
            @click="jumpPlaylistDetail(fp.id)">
          <div class="card_body">
            <img :src="fp.cover" alt="">
            <!--                        <span>{{ this.fp.songs.length }}首</span>-->
            <img :src="Icon.headsetIcon" alt="">
          </div>
          <span>{{ fp.title }}</span>
          <span>{{ fp.createTime }}</span>
        </div>
        <div v-show="this.favoritePlaylist.length === 0">
          <h3>未收藏歌单</h3>
        </div>
      </div>
    </div>
    <div key="album" v-if="type === 'album'" class="collect">
      <div class="pl">
        <span>我收藏的</span>
        <div
            class="card_detail"
            v-for="fa in favoriteAlbum"
            :key="fa.id"
            @click="jumpAlbumDetail(fa.id)"
        >
          <div class="card_body">
            <img :src="fa.cover" alt="">
            <span>500首</span>
            <img :src="Icon.headsetIcon" alt="">
          </div>
          <span>{{ fa.title }}</span>
          <span>{{ fa.releaseDate }}</span>
        </div>
        <div v-show="this.favoriteAlbum.length === 0">
          <h3>未创建专辑</h3>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import FavoriteService from "@/api/service/FavoriteService";
import Icon from "@/util/common/Icon";
import PlaylistService from "@/api/service/PlaylistService";

export default {
  name: "userCollectMusic",
  computed: {
    Icon() {
      return Icon
    }
  },
  data() {
    return {
      type: '',
      favoriteAlbum: [],
      playlists: [],
      favoritePlaylist: [],
    }
  },
  methods: {
    jumpListPlaylistDetail(id) {
      this.$router.push({path: `/music/list/detail/playlist/${id}`});
    },
    jumpPlaylistDetail(id) {
      this.$router.push({path: `/music/detail/playlist/${id}`});
    },
    jumpAlbumDetail(id) {
      this.$router.push({path: `/music/detail/album/${id}`});
    },
    async initParams() {
      if (this.type === "album") {
        this.favoriteAlbum = await FavoriteService.queryFavoriteAlbum();
      } else if (this.type === "playlist") {
        const data = await PlaylistService.UserPagedQuery("false", null, 1, 100);
        this.playlists = data.data.items;
        this.favoritePlaylist = await FavoriteService.queryFavoritePlaylist();
      } else {
        console.log("null");
      }
    }
  },
  watch: {
    '$route'(to) {
      this.type = to.params.type || this.type;
      this.initParams();
    }
  },
  created() {
    this.type = this.$route.params.type || this.type;
    this.initParams();
  },
}
</script>

<style scoped>
#userCollectMusic {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: var(--second-color);
  overflow: scroll;
}

.collect {
  width: 100%;
  height: 100%;
}

.pl {
  width: 100%;
  margin-bottom: 1%;
  display: flex;
  flex-wrap: wrap;
}

.pl > span {
  width: 100%;
  font-size: 1.2em;
  padding: 5px;
  text-align: start;
  font-weight: 600;
  color: var(--main-color);
}

.card_detail {
  width: 12%;
  margin-right: 2%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: start;
  border-radius: 10px;
  box-shadow: 12px 12px 24px #b3b3b3, -2px -2px 4px #ffffff;
}

.card_body {
  width: 100%;
  padding-bottom: 100%;
  position: relative;
  border-radius: 8px;
  top: 0;
  background-color: var(--main-color);
  transition: all 0.2s ease;
  overflow: hidden; /* 防止放大时超出容器 */
}

.card_body:hover {
  top: -1%;
}

.card_body img:first-of-type {
  width: 100%;
  position: absolute;
  border-radius: 8px;
  top: 0;
  left: 0;
  transition: all 0.2s;
}

.card_body:hover img:first-of-type {
  transform: scale(1.2);
}

.card_body img:nth-of-type(2) {
  height: 40%;
  padding: 30%;
  position: absolute;
  bottom: 0;
  right: 0;
  opacity: 0;
  transition: all 0.2s;
  background-color: #0009;
  cursor: pointer;
}

.card_body:hover img:nth-of-type(2) {
  opacity: 1;
}

.card_body img:nth-of-type(3) {
  height: 15%;
  position: absolute;
  top: 0;
  right: 0;
}

.card_body img:nth-of-type(4) {
  height: 15%;
  position: absolute;
  bottom: -15%;
  right: 0;
}

.card_body span {
  position: absolute;
  bottom: 0;
  left: 0;
  padding: 8px;
  border-radius: 8px;
  background-color: rgba(182, 182, 182, 0.6);
}

</style>