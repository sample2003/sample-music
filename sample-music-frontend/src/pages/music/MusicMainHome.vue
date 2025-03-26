<template>
  <div id="Home">
    <div class="first flex">
      <div class="recommend">
        <h3 class="headSpan">Hi，{{ this.userDetail?.username ?? '你好' }}，为你推荐歌曲</h3>
        <div class="res">
          <div class="re" v-for="r in this.albumDetail.songs?.slice(0,5) || []" :key="r.id" @click="playBySong(r)">
            <img :src="r.cover" alt="">
            <img :src="Icon.playWhiteIcon" alt="">
            <p>{{ r.title }}</p>
          </div>
        </div>
      </div>
      <!-- 公告滚动框 -->
      <div class="notice">
        <div class="notice-slides">
          <div class="notice-slide" v-for="n in notice" :key="n.id"
               @mouseenter="clear('notice')"
               @mouseleave="playSlides('notice')"
               :class="{'active': noticeIndex === notice.indexOf(n)}">
            <div class="back" :style="{ 'background-image': `url(${n.cover})` }"></div>
            <span>{{ n.title }}</span>
          </div>
        </div>
        <!-- 指示器 -->
        <div class="notice-indicators">
          <span
              v-for="(n, idx) in notice"
              :key="idx"
              class="indicator-dot"
              :class="{ 'active': idx === noticeIndex }"
              @click="setActiveIndex(idx, 'notice')">
          </span>
        </div>
      </div>
      <!-- 广告滚动框 -->
      <div class="notice">
        <div class="notice-slides">
          <div class="notice-slide" v-for="n in notice" :key="n.id"
               :class="{'active': otherIndex === notice.indexOf(n)}"
               @mouseenter="clear('other')"
               @mouseleave="playSlides('other')">
            <div class="back" :style="{ 'background-image': `url(${n.cover})` }"></div>
            <span>{{ n.title }}</span>
          </div>
        </div>
        <!-- 指示器 -->
        <div class="notice-indicators">
          <span
              v-for="(n, idx) in notice"
              :key="idx"
              class="indicator-dot"
              :class="{ 'active': idx === otherIndex }"
              @click="setActiveIndex(idx, 'other')">
          </span>
        </div>
      </div>
    </div>

    <div class="recommend-cards">
      <h3 class="headSpan">推荐专辑</h3>
      <div class="cards">
        <div class="card" v-for="al in albumsAndSongs" :key="al.id" @click="jumpAlbumDetail(al.id)">
          <img :src="al.cover" alt="">
          <img v-once :src="Icon.detailWhiteIcon" alt="">
          <span>{{ al.title }}</span>
          <div class="kik flex">
            <img v-once :src="Icon.peopleIcon" alt="">
            <span>{{ al.artist }}</span>
          </div>
          <div class="kik flex">
            <img v-once :src="Icon.headsetIcon" alt="">
            <span>{{ al.listeners }}</span>
          </div>
          <span>{{ al.description }}</span>
          <span style="display: inline-flex;"><img :src="Icon.calendarIcon" alt=""
                                                   style="width: 10%;padding-left: 5px;">{{ al.releaseDate }}</span>
        </div>
      </div>
    </div>

    <div class="recommend-cards">
      <h3 class="headSpan">推荐歌单</h3>
      <div class="cards">
        <div class="card" v-for="pl in playlistsAndSongs" :key="pl.id" @click="jumpPlaylistDetail(pl.id)">
          <img class="c1" :src="pl.cover" alt="">
          <img v-once :src="Icon.detailWhiteIcon" alt="">
          <span>{{ pl.title }}</span>
          <div class="kik flex">
            <img v-once :src="Icon.peopleIcon" alt="">
            <span>{{ pl.username || '未知' }}</span>
          </div>
          <div class="kik flex">
            <img v-once :src="Icon.headsetIcon" alt="">
            <span>{{ pl.listeners }}</span>
          </div>
          <span>{{ pl.description || '暂无描述' }}</span>
          <span style="display: inline-flex;white-space: nowrap;"><img :src="Icon.calendarIcon" alt=""
                                                                       style="width: 10%;padding-left: 5px;">{{
              pl.createTime
            }}</span>
        </div>
      </div>
    </div>

    <div class="second flex">
      <h3 class="headSpan">分类</h3>
      <div class="classifys">
        <div class="classify flex" v-for="cl in classify" :key="cl.id">
          <img :src="cl.img" alt="">
          <span>{{ cl.msg }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import AlbumService from "@/api/service/AlbumService";
import PlaylistService from "@/api/service/PlaylistService";
import PublicityService from "@/api/service/PublicityService";
import Icon from "@/util/common/Icon";

export default {
  name: "MusicMainHome",
  data() {
    return {
      classify: [
        {id: "11", img: Icon.addIcon, msg: "流行"},
        {id: "22", img: Icon.addIcon, msg: "摇滚"},
        {id: "33", img: Icon.addIcon, msg: "伤感"},
        {id: "44", img: Icon.addIcon, msg: "r&b"},
        {id: "55", img: Icon.addIcon, msg: "日推"},
        {id: "66", img: Icon.addIcon, msg: "动漫"},
      ],
      playlists: [],
      albumsAndSongs: [],
      playlistsAndSongs: [],
      detailType: "", // 初始化 detailType
      notice: [],
      noticeIndex: 0, // 当前公告轮播的索引
      otherIndex: 0, // 当前广告轮播的索引
    }
  },
  methods: {
    // 清除自动播放定时器
    clear(type) {
      if (type === 'notice') clearInterval(this.intervalNotice);
      else if (type === 'other') clearInterval(this.intervalOther);
      else {
        clearInterval(this.intervalNotice);
        clearInterval(this.intervalOther);
      }
    },
    // 滑动公告框和广告框
    playSlides(type) {
      this.clear(type);
      if (type === 'notice') {
        this.intervalNotice = setInterval(() => {
          this.noticeIndex = (this.noticeIndex + 1) % this.notice.length;
        }, 3000); // 每3秒自动播放下一张
      } else if (type === 'other') {
        this.intervalOther = setInterval(() => {
          this.otherIndex = (this.otherIndex + 1) % this.notice.length;
        }, 3000); // 每3秒自动播放下一张
      } else {
        this.intervalNotice = setInterval(() => {
          this.noticeIndex = (this.noticeIndex + 1) % this.notice.length;
        }, 3000); // 每3秒自动播放下一张
        this.intervalOther = setInterval(() => {
          this.otherIndex = (this.otherIndex + 1) % this.notice.length;
        }, 3000); // 每3秒自动播放下一张
      }
    },
    // 设置自动滚动
    setActiveIndex(index, type) {
      this.noticeIndex = index;
      if (type === 'notice') this.playSlides("notice"); // 重新开始自动播放
      else this.playSlides("other");
    },
    // 获取推荐专辑
    async getAlbums() {
      const temp = await AlbumService.conditionAndPaged();
      this.albumsAndSongs = temp.data.items?.slice(0, 6);
    },
    // 获取推荐歌单
    async getPlaylists() {
      // todo
      const data = await PlaylistService.selectPlaylists();
      this.playlistsAndSongs = data.items?.slice(0, 6);
    },
    // 获取公告
    async getNotice() {
      const temp = await PublicityService.conditionAndPaged();
      this.notice = temp.data.items;
    },
    async jumpAlbumDetail(id) {
      await this.$router.push({path: `/music/detail/album/${id}`});
    },
    async jumpPlaylistDetail(id) {
      await this.$router.push({path: `/music/detail/playlist/${id}`});
    },
    async initHome() {
      await this.getAlbums();
      await this.getPlaylists();
      await this.getNotice();
      await this.playSlides();
    }
  },
  computed: {
    Icon() {
      return Icon
    }
  },
  mounted() {
    this.initHome()
  },
  beforeDestroy() {
    this.clear(); // 清除定时器
  },
}
</script>

<style scoped>
#Home {
  width: 100%;
  height: 90%;
  font-size: var(--fontSize);
  overflow: auto;
}

.first {
  width: 100%;
  height: 20%;
  margin-top: 1%;
  align-items: end;
}

.recommend {
  width: 25%;
  height: 100%;
  border-radius: 10px;
  display: flex;
  justify-content: end;
  flex-direction: column;
}

.recommend h3 {
  width: 100%;
  height: 20%;
  text-align: start;
}

.res {
  width: 100%;
  height: 70%;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}

.re {
  width: 18%;
  height: 100%;
  cursor: pointer;
  position: relative;
  border-radius: 5px;
  transition: all 0.2s ease;
  border-bottom: 2px solid var(--main-color);
}

.re img {
  width: 80%;
  position: absolute;
  left: 10%;
  top: 10%;
  z-index: 1;
  border-radius: 5px;
  transition: 0.2s all ease;
}

.re img:nth-of-type(2) {
  width: 50%;
  padding: 15%;
  left: 10%;
  top: 10%;
  background-color: rgba(34, 34, 34, 0.6);
  opacity: 0;
  z-index: 2;
}

.re:hover {
  box-shadow: none;
}

.re:hover img:nth-of-type(2) {
  opacity: 1;
}

.re p {
  max-width: 100%;
  position: absolute;
  left: 0;
  right: 0;
  bottom: 5px;
  white-space: nowrap;
  overflow: hidden;
  font-weight: bold;
  text-overflow: ellipsis;
  font-size: 0.8em;
}

.notice {
  width: 28%;
  height: 90%;
  border-radius: 10px;
  transition: all 0.2s ease;
  position: relative;
  cursor: default;
  box-shadow: 2px 2px 4px #b3b3b3, -2px -2px 4px #ffffff;
}

.notice:hover {
  height: 95%;
  box-shadow: 12px 12px 24px #b3b3b3, -12px -12px 24px #ffffff;
}

.notice-slides {
  display: flex;
  height: 100%;
  position: relative;
}

.notice-slide {
  flex-shrink: 0;
  width: 100%;
  display: none;
}

.notice-slide.active {
  display: block;
}

.notice-slide span {
  width: auto;
  text-align: center;
  font-size: 14px;
  opacity: 1;
  position: absolute;
  bottom: 0;
  right: 0;
  padding: 2px;
  background-color: var(--main-color);
  border-radius: 5px;
  transition: all 0.2s ease;
}

.notice-indicators {
  text-align: center;
  position: absolute;
  bottom: 0;
  left: 0;
}

.indicator-dot {
  display: inline-block;
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background-color: var(--second-color);
  margin: 0 5px;
  cursor: pointer;
}

.indicator-dot.active {
  background-color: var(--main-color);
}

.back {
  width: 100%;
  height: 100%;
  border: 1px solid black;
  box-sizing: border-box;
  border-radius: 5px;
  background-size: cover;
  background-position: center;
  transition: all 0.1s ease;
  align-items: flex-end;
  overflow: hidden;
}

.back:hover {
  transform: scaleY(1.01);
}

.second {
  width: 96%;
  height: 25%;
  margin: 2% 2% 0;
  display: flex;
  flex-wrap: wrap;
  cursor: pointer;
}

.headSpan {
  width: 100%;
  height: 20%;
  padding: 10px;
  text-align: start;
  font-style: italic;
}

.classifys {
  display: flex;
  width: 95%;
  height: 80%;
}

.classify {
  width: 20%;
  flex-direction: column;
  align-items: start;
  font-weight: 600;
}

.classify img {
  width: 35%;
  border-radius: 50%;
  background-clip: border-box;
  border: 5px solid #ffffff00;
  transition: all 0.2s ease;
}

.classify:hover img {
  border: 5px solid var(--main-color);
}

.classify span {
  width: 40%;
}

.classify:hover span {
  color: var(--main-color);
}

.recommend-cards {
  width: 96%;
  margin: 2%;
}

.cards {
  width: 100%;
  height: 9%;
  display: flex;
  justify-content: start;
  gap: 2%;
  align-items: center;
}

.card {
  width: 15%;
  padding-top: 16%;
  overflow: hidden;
  position: relative;
  border-radius: 10px;
  transition: all 0.2s ease;
  cursor: pointer;
  border: 1px solid #00000000;
  box-shadow: 12px 12px 24px #b3b3b3, -2px -2px 4px #ffffff;
}

.card:hover {
  border: 1px solid var(--main-color);
  box-shadow: none;
}

.card > img:nth-of-type(1) {
  width: 45%;
  z-index: 1;
  position: absolute;
  top: 5%;
  left: 5%;
  border-radius: 5px;
}

.card > img:nth-of-type(2) {
  width: 25%;
  padding: 10%;
  position: absolute;
  top: 5%;
  left: 5%;
  border-radius: 5px;
  opacity: 0;
  background-color: rgba(34, 34, 34, 0.58);
  transition: all 0.2s ease;
  z-index: 1;
}

.card > img:nth-of-type(2):hover {
  opacity: 1;
}

.kik {
  width: 45%;
  position: absolute;
  left: 55%;
  justify-content: start;
}

.kik:nth-of-type(1) {
  top: 23%;
}

.kik:nth-of-type(2) {
  top: 38%;
}

.kik img {
  width: 20%;
}

.kik span {
  max-width: 75%;
  font-size: 0.9em;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.card > span {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  position: absolute;
  transition: all 0.2s ease;
}

.card > span:nth-of-type(1) {
  width: 40%;
  padding: 2% 0;
  top: 6%;
  right: 6%;
  font-weight: 600;
  border-radius: 5px;
  box-shadow: 2px 2px 4px #b3b3b3, -2px -2px 4px #ffffff
}

.card:hover > span:nth-of-type(1) {
  color: var(--main-color);
}

.card > span:nth-of-type(2) {
  width: 90%;
  position: absolute;
  top: 50%;
  height: 35%;
  left: 6%;
  color: #888888;
  letter-spacing: 0.1em;
  font-size: 0.8em;
  white-space: wrap;
  overflow: scroll;
  text-align: start;
}

.card > span:nth-of-type(3) {
  width: 100%;
  bottom: 0;
  left: 0;
  align-items: center;
  font-size: 0.8em;
  color: var(--second-color);
  text-align: start;
  background-color: var(--fourth-color);
  text-indent: 5px;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>