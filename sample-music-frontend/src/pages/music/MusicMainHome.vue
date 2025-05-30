<template>
  <div id="Home">
    <div class="first flex">
      <div class="recommend">
        <h3 class="headSpan">
          <!-- 完整文本（默认显示） -->
          <span class="full-text">Hi，{{ userDetail?.username ?? '你好' }}，为你推荐歌曲</span>
          <span class="short-text">为你推荐</span>
        </h3>
        <div class="res">
          <div class="re" v-for="r in albumDetail.songs?.slice(0,10) || []"
               :key="r.id"
               @click="playBySong(r)">
            <div class="img-container">
              <img :src="r.cover" alt="专辑封面" class="cover">
              <img :src="Icon.playWhiteIcon" alt="播放" class="play-icon">
            </div>
            <p class="song-title">{{ r.title }}</p>
          </div>
        </div>
      </div>
      <!-- 公告滚动框 -->
      <div class="notice">
        <div class="notice-slides">
          <div class="notice-slide" v-for="n in notice" :key="n.id"
               @click="jumpPublicity(n.id)"
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
          <div class="notice-slide" v-for="a in advert" :key="`advert-${a.id}`"
               :class="{'active': advertIndex === advert.indexOf(a)}"
               @click="jumpPublicity(a.id)"
               @mouseenter="clear('advert')"
               @mouseleave="playSlides('advert')">
            <div class="back" :style="{ 'background-image': `url(${a.cover})` }"></div>
            <span>{{ a.title }}</span>
          </div>
        </div>
        <!-- 指示器 -->
        <div class="notice-indicators">
          <span
              v-for="(a, idx) in advert"
              :key="idx"
              class="indicator-dot"
              :class="{ 'active': idx === advertIndex }"
              @click="setActiveIndex(idx, 'advert')">
          </span>
        </div>
      </div>
      <!-- 广告滚动框 -->
      <div class="notice">
        <div class="notice-slides">
          <div class="notice-slide" v-for="n in notice" :key="n.id"
               :class="{'active': noticeIndex === notice.indexOf(n)}"
               @click="jumpPublicity(a.id)"
               @mouseenter="clear('notice')"
               @mouseleave="playSlides('notice')">
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
    </div>

    <div class="recommend-cards">
      <h3 class="headSpan">推荐专辑</h3>
      <div class="cards">
        <div class="card" v-for="al in albumsAndSongs" :key="al.id" @click="jumpAlbumDetail(al.id)">
          <img :src="al.cover" alt="">
          <img v-once :src="Icon.detailWhiteIcon" alt="">
          <span>{{ al.title }}</span>
          <div class="kik flex">
            <img v-once :src="Icon.headsetIcon" alt="">
            <span>{{ al.listeners }}</span>
          </div>
          <div class="kik flex">
            <img v-once :src="Icon.calendarIcon" alt="">
            <span>{{ al.releaseDate }}</span>
          </div>
          <span>{{ al.description }}</span>
          <!--          <span style="display: inline-flex;"><img :src="Icon.calendarIcon" alt="" style="width: 10%;padding-left: 5px;">{{ al.releaseDate }}</span>-->
          <span style="display: inline-flex">
            <img :src="Icon.diskIcon" alt="" style="width: 8%;padding-left: 5px;">{{ al.title }} - <span
              style="display: inline-flex">{{ al.artist }}</span></span>
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
            <img v-once :src="Icon.headsetIcon" alt="">
            <span>{{ pl.listeners }}</span>
          </div>
          <div class="kik flex">
            <img v-once :src="Icon.calendarIcon" alt="">
            <span>{{ pl.createTime }}</span>
          </div>
          <span>{{ pl.description || '暂无描述' }}</span>
          <span style="display: inline-flex;white-space: nowrap;">
            <img :src="Icon.diskIcon" alt="" style="width: 8%;padding-left: 5px;">{{ pl.title }} - <span
              style="display: inline-flex">{{ pl.username }}</span></span>
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
      playlists: [],
      albumsAndSongs: [],
      playlistsAndSongs: [],
      detailType: "", // 初始化 detailType
      notice: [],
      advert: [],
      noticeIndex: 0, // 当前公告轮播的索引
      advertIndex: 0, // 当前广告轮播的索引
    }
  },
  methods: {
    // 清除自动播放定时器
    clear(type) {
      if (type === 'notice') clearInterval(this.intervalNotice);
      else if (type === 'advert') clearInterval(this.intervalAdvert);
      else if (type === 'other') clearInterval(this.intervalOther);
      else {
        clearInterval(this.intervalNotice);
        clearInterval(this.intervalAdvert);
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
      } else if (type === 'advert') {
        this.intervalAdvert = setInterval(() => {
          this.advertIndex = (this.advertIndex + 1) % this.advert.length;
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
    // 跳转到公告页
    jumpPublicity(id) {
      this.$router.push({path: `/music/publicity/${id}`});
    },
    // 设置自动滚动
    setActiveIndex(index, type) {
      this.noticeIndex = index;
      if (type === 'notice') this.playSlides("notice"); // 重新开始自动播放
      if (type === 'advert') this.playSlides("advert"); // 重新开始自动播放
      else this.playSlides("other");
    },
    // 获取推荐专辑
    async getAlbums() {
      const temp = await AlbumService.conditionAndPaged(null, 1, 20);
      this.albumsAndSongs = temp.data.items?.slice(9, 15);
    },
    // 获取推荐歌单
    async getPlaylists() {
      // todo
      const playlists = await PlaylistService.PagedQuery(null, 1, 10);
      this.playlistsAndSongs = playlists.data.items?.slice(0, 6);
    },
    // 获取公告
    async getNotice() {
      const temp = await PublicityService.conditionAndPaged();
      this.notice = temp.data.items;
    },
    // 获取广告
    async getAdvert() {
      const temp = await PublicityService.conditionAndPaged(null, "infomercial");
      this.advert = temp.data.items;
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
      await this.getAdvert();
      await this.playSlides();
    }
  },
  computed: {
    Icon() {
      return Icon
    },
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
  height: 100%;
  font-size: var(--fontSize);
  overflow: auto;
}

.first {
  width: 95%;
  height: 20%;
  margin: 2%;
  display: flex;
  justify-content: start;
  align-items: end;
}

.short-text {
  display: none;
}

@media (max-width: 1300px) {
  .full-text {
    display: none;
  }
  .short-text {
    display: inline;
  }
}

.recommend {
  width: calc(25% - 50px);
  height: 100%;
  border-radius: 10px;
  display: flex;
  margin-right: 10px;
  justify-content: end;
  flex-direction: column;
}

.res {
  display: flex; /* 改为弹性布局 */
  flex-wrap: wrap; /* 禁止换行 */
  gap: 15px;
  padding-top: 10px;
  padding-left: 20px;
  width: 95%;
  overflow: scroll;
}

.re {
  flex: 0 0 55px; /* 固定宽度 */
  height: 55px;
  position: relative;
  cursor: pointer;
  overflow: hidden;
  border-radius: 8px;
  transition: all 0.2s ease;
}

.img-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 50%; /* 初始只显示上半部分 */
  overflow: hidden;
  transition: all 0.3s ease;
}

.cover {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transform: scale(1.05);
  transition: all 0.3s ease;
}

.play-icon {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 40px;
  height: 40px;
  opacity: 0;
  transition: all 0.3s ease;
}

.song-title {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 50%;
  margin: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--fourth-color);
  color: black;
  font-size: 0.8em;
  text-align: center;
  transition: all 0.3s ease;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* Hover 效果 */
.re:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.re:hover .img-container {
  height: 100%;
}

.re:hover .cover {
  transform: scale(1);
}

.re:hover .play-icon {
  opacity: 1;
}

.re:hover .song-title {
  opacity: 0;
  transform: translateY(100%);
}

.notice {
  width: 24%;
  height: 90%;
  margin-right: 10px;
  border-radius: 10px;
  transition: all 0.2s ease;
  position: relative;
  cursor: default;
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
  box-sizing: border-box;
  border-radius: 5px;
  background-size: cover;
  background-position: center;
  transition: all 0.1s ease;
  align-items: flex-end;
  overflow: hidden;
}


.headSpan {
  width: 100%;
  height: 1.2em;
  padding: 2px 2px 5px;
  border-radius: 10px;
  text-align: start;
  text-indent: 1em;
  cursor: default;
}

.recommend-cards {
  width: 96%;
  margin: 2%;
}

.cards {
  width: 100%;
  height: 9%;
  display: flex;
  justify-content: space-between;
  user-select: none;
}

.card {
  width: 15%;
  padding-top: 16%;
  margin: 1%;
  position: relative;
  border-radius: 10px;
  transition: all 0.2s ease;
  cursor: pointer;
  border: 1px solid #0000;
  box-sizing: border-box;
}

.card:hover {
  box-shadow: none;
  border: 1px solid var(--main-color);
}

.card > img:nth-of-type(1) {
  width: 100%;
  z-index: 10;
  position: absolute;
  top: 0;
  left: 0;
  border-radius: 5px;
  transition: 0.2s all ease;
  height: auto; /* 先设置高度为自动，后续根据宽度来等比例调整高度 */
  aspect-ratio: 1 / 1; /* 设置宽高比为1:1，确保图片为正方形 */
  object-fit: contain; /* 使用 contain 属性，让图片在保持宽高比的前提下，尽可能填满容器，同时不会变形 */
  background-color: var(--fourth-color);
}

.card:hover > img:nth-of-type(1) {
  width: 45%;
  top: 5%;
  left: 5%;
}

.card > img:nth-of-type(2) {
  width: 80%;
  padding: 10%;
  position: absolute;
  top: 0;
  left: 0;
  border-radius: 5px;
  opacity: 0;
  background-color: rgba(34, 34, 34, 0.58);
  transition: all 0.2s ease;
  z-index: 11;
  height: auto; /* 先设置高度为自动，后续根据宽度来等比例调整高度 */
  aspect-ratio: 1 / 1; /* 设置宽高比为1:1，确保图片为正方形 */
  object-fit: contain; /* 使用 contain 属性，让图片在保持宽高比的前提下，尽可能填满容器，同时不会变形 */
}

.card:hover > img:nth-of-type(2) {
  width: 25%;
  opacity: 1;
  top: 5%;
  left: 5%;
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
  bottom: -0.8em;
  left: 0;
  align-items: center;
  font-size: 0.8em;
  color: var(--third-color);
  text-align: start;
  background-color: var(--fourth-color);
  border-radius: 5px;
  text-indent: 5px;
  overflow: hidden;
  text-overflow: ellipsis;
}

.card:hover > span:nth-of-type(3) {
  background-color: var(--main-color);
}
</style>