<template>
  <div id="Play" class="flex">
    <!-- 左边部分 -->
    <div class="play_left">
      <transition name="fade" mode="out-in">
        <!-- 旋转封面 -->
        <div v-if="!isShowComment && !isShowDetail" key="cover" class="pl_effect">
          <div class="play_style" :class="isPlay? 'songRunning':'songPaused'">
            <div class="pic">
              <img :src="songPlaying.cover" alt="">
            </div>
            <div class="pic">
              <img :src="Icon.diskPng" alt="">
            </div>
          </div>
          <img :src="Icon.stylusPng" alt="" :class="isPlay? 'sr':'sp'" class="stylus">
        </div>
        <!-- 评论区 -->
        <div v-else-if="isShowComment && !isShowDetail" key="comment" class="pl_comment">
          <!-- 用户评论 -->
          <UserComment detail-type="song" :targetId="songPlaying.id"></UserComment>
        </div>
        <!-- 歌曲详情 -->
        <div v-else-if="isShowDetail && !isShowComment" key="detail" class="pl_detail">
          <div class="detail">
            <span>歌名：{{ this.songPlaying.title }}</span>
            <img :src="this.songPlaying.cover" alt="">
          </div>
          <div class="detail">
            <span>艺术家：{{ this.songPlaying.artist }}</span>
            <img :src="Icon.peopleIcon" alt="">
          </div>
          <div class="detail">
            <span>专辑：{{ this.songPlaying.album }}</span>
            <img :src="Icon.peopleIcon" alt="">
          </div>
          <div class="detail">
            <span>收听人数：{{ this.songPlaying.listeners }}</span>
          </div>
          <div class="detail">
            <span>风格：{{ this.songPlaying.style }}</span>
          </div>
          <div class="detail">
            <span>发布时间：{{ this.songPlaying.releaseDate }}</span>
          </div>
        </div>
      </transition>
      <div class="pl_control">
        <ScrollText :max-width="'100%'" :font-size="'2em'" style="margin: 10px;"></ScrollText>
        <div class="control-button flex">
          <img :src="returnPlayIcon" alt="" @click="togglePlayMode">
          <img :src="Icon.backward10sIcon" alt="" @click="backward10s">
          <img :src="Icon.lastIcon" alt="" @click="playPrevious">
          <img class="anm" v-show="isPlay" :src="Icon.pauseIcon" alt="" @click="PlayOrPause">
          <img class="anm" v-show="!isPlay" :src="Icon.playIcon" alt="" @click="PlayOrPause">
          <img :src="Icon.nextIcon" alt="" @click="playNext">
          <img :src="Icon.forward10sIcon" alt="" @click="forward10s">
          <img :src="songMode === 'mp3' ? Icon.mp3Icon : Icon.flacIcon" alt="">
          <img :src="Icon.commentIcon" alt="" :class="{'bac': this.isShowComment}" @click="showComment">
          <img :src="Icon.fileIcon" alt="" :class="{'bac': this.isShowDetail}" @click="showDetail">
        </div>
        <div class="progress-container flex">
          <span style="padding: 1%;">{{ formattedTime(currentTime) }}</span>
          <input type="range" class="level" min="0" :max="songDuration" v-model="timer" @input="changeProgress">
          <span style="padding: 1%;">{{ formattedTime(songDuration ?? 0) }}</span>
        </div>
        <div class="progress-container progress_volume flex">
          <img style="padding-right: 5px;" :src="volume > 0 ? Icon.soundOnIcon : Icon.soundOnIcon" alt=""/>
          <!-- 音量控制条 -->
          <input type="range" class="level" min="0" max="100" v-model="volume" @input="changeVolume">
          <div style="width: 20%;"></div>
          <!--                    <img :src="volume > 0 ? Icon.soundOnIcon : Icon.soundOnIcon" alt=""/>-->
        </div>
      </div>
    </div>
    <!-- 右边部分 -->
    <div class="play_right">
      <!-- 歌词部分 -->
      <div @scroll="stopScrollingLyrics" v-if="songLyric.length > 0" class="lyricsContainer" ref="lyricsContainer">
        <div class="lyricSpacer"></div>
        <span
            class="lyric"
            :class="{ current: isCurrentLyric(index) }"
            v-for="(lyric, index) in songLyric"
            :key="index"
        >
                    {{ lyric }}
                </span>
        <div class="lyricSpacer"></div>
      </div>
      <div v-else>
        <h3>暂无歌词</h3>
      </div>
      <!-- 选项 -->
<!--      <div class="other">
        <div class="o1 flex">
          <img :src="Icon.commentIcon" alt="" :class="{'bac': this.isShowComment}" @click="showComment">
          <span>{{ this.user_comment.length }}</span>
        </div>
        <div class="o2">
          <img :src="Icon.addIcon" alt="">
        </div>
        <div class="o3">
          <img :src="Icon.uploadIcon" alt="">
        </div>
        <div class="o4">
          <img :src="Icon.fileIcon" alt="" :class="{'bac': this.isShowDetail}" @click="showDetail">
        </div>
      </div>-->
    </div>
  </div>
</template>

<script>
import SongService from "@/api/service/SongService";
import FavoriteService from "@/api/service/FavoriteService";
import UserComment from "@/pages/common/UserComment.vue";
import Icon from "@/util/common/Icon";
import ScrollText from "@/pages/common/ScrollText.vue";

export default {
  name: "MusicMainPlay",
  components: {ScrollText, UserComment},
  data() {
    return {
      user_comment: [],
      currentLyricIndex: -1,
      scrollInterval: null,
      userScrollTimer: null,
      isShowComment: false,
      isShowDetail: false,
      // 音频可视化
      isGoodArray: [], // 存储每个评论的点赞状态
      animation: null
    }
  },
  computed: {
    Icon() {
      return Icon
    },
    timer: {
      get() {
        return this.currentTime;
      },
      set(value) {
        this.setCurrentTime(value);
      }
    },
  },
  methods: {
    // 格式化时间格式
    formattedTime(time) {
      const minutes = Math.floor(time / 60);
      const seconds = Math.floor(time % 60);
      return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
    },

    // 改变进度
    changeProgress() {
      this.audio.currentTime = this.timer
    },

    // 快进十秒
    forward10s() {
      this.audio.currentTime = this.timer + 10
    },

    // 后退十秒
    backward10s() {
      this.audio.currentTime = this.timer - 10
    },

    // 判断是否点赞评论
    async isGoodComment(id) {
      const favoriteComment = await FavoriteService.queryFavoriteComment();
      return favoriteComment.some(favorite => favorite.id === id);
    },

    // 点赞评论
    async changeFavoritesComment(id) {
      const isGood = await this.isGoodComment(id);
      if (isGood) {
        await FavoriteService.removeByFavorites("comment", id);
      } else {
        await FavoriteService.addToFavorites("comment", id);
      }
    },

    // 判断当前歌词
    isCurrentLyric(index) {
      // 从时间戳数组后面开始寻找，
      for (let i = this.Timestamp.length - 1; i >= 0; i--) {
        // 当前传入的歌词索引对应的时间戳索引，
        if (this.Timestamp[i] <= this.currentTime) {
          this.currentLyricIndex = i;
          break;
        }
      }
      // 如果找到了合适的时间戳索引
      if (this.currentLyricIndex !== null) {
        // 检查传入的索引是否与当前歌词的索引相匹配
        return index === this.currentLyricIndex;
      }
      return false;
    },

    // 滚动歌词
    startScrollingLyrics() {
      this.scrollInterval = setInterval(() => {
        // 找到当前播放时间之前的一个时间戳的索引
        for (let i = this.Timestamp.length - 1; i >= 0; i--) {
          if (this.Timestamp[i] <= this.currentTime) {
            this.currentLyricIndex = i;
            break;
          }
        }
        if (this.currentLyricIndex !== -1) {
          // 获取当前歌词元素
          this.$nextTick(() => {
            const container = this.$refs.lyricsContainer;
            if (container && container.children && this.currentLyricIndex >= 0 && this.currentLyricIndex < container.children.length) {
              const currentLyricElement = container.children[this.currentLyricIndex];
              if (currentLyricElement) {
                // 计算滚动的位置，使当前歌词居中显示
                const containerHeight = container.clientHeight;
                const lyricTop = currentLyricElement.offsetTop - container.offsetTop;
                const scrollPosition = lyricTop - (containerHeight / 2.5) + (currentLyricElement.clientHeight / 2.5);
                // 滚动歌词容器
                container.scrollTo({
                  top: scrollPosition,
                  behavior: 'smooth'
                });
              }
            }
          })
        }
      }, 100); // 每隔500毫秒检查一次当前播放时间
    },

    // 停止滚动歌词
    stopScrollingLyrics() {
      clearInterval(this.scrollInterval);
    },

    handleLyricScroll() {
      clearTimeout(this.userScrollTimer);
      // 用户滚动时，停止自动滚动
      this.stopScrollingLyrics();

      // 设置一个定时器，如果在一定时间内没有滚动事件发生，则恢复自动滚动
      this.userScrollTimer = setTimeout(() => {
        this.startScrollingLyrics();
      }, 2000); // 2秒内无滚动操作则恢复自动滚动
    },

    // 显示详情
    showDetail() {
      if (this.isShowDetail === false) {
        this.isShowComment = false;
        this.isShowDetail = true;
      } else {
        this.isShowComment = false;
        this.isShowDetail = false;
      }
    },

    // 显示评论区
    showComment() {
      if (this.isShowComment === false) {
        this.isShowDetail = false;
        this.isShowComment = true;
      } else {
        this.isShowDetail = false;
        this.isShowComment = false;
      }
    },

    // 评论歌曲
    async insertComment(comment) {
      await SongService.insertSongComment(comment, this.songPlaying.id);
      await this.querySongComment()
    },

    // 查询评论并判断用户是否点赞
    async querySongComment() {
      const comment = await SongService.queryCommentPaged(this.songPlaying.id, 1, 100);
      if (comment && comment.items) {
        this.user_comment = comment.items;
        for (const c of comment.items) {
          const isGood = await this.isGoodComment(c.id);
          if (isGood) {
            this.isGoodArray.push(c.id)
          }
        }
      }
    },
  },
  mounted() {
    this.querySongComment();
    this.startScrollingLyrics();
    // 添加滚动事件监听器
    if (this.$refs.lyricsContainer) {
      this.$refs.lyricsContainer.addEventListener('scroll', this.handleLyricScroll);
    }
  },
  beforeDestroy() {
    this.stopScrollingLyrics();

    // 移除滚动事件监听器
    if (this.$refs.lyricsContainer) {
      this.$refs.lyricsContainer.removeEventListener('scroll', this.handleLyricScroll);
    }
    clearTimeout(this.userScrollTimer); // 清除定时器
  }
}
</script>

<style scoped>
#Play {
  width: 100%;
  height: 100%;
}

.play_left {
  width: 50%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: relative;
}

.control-button {
  width: 100%;
  margin-top: 10px;
  justify-content: center;
}

.control-button img {
  width: 5%;
  padding: 5px;
  margin: 0 1%;
  border-radius: 5px;
  cursor: pointer;
  background-color: var(--second-color);
  transition: all 0.3s;
  border: 1px solid #0000;
  box-shadow: 2px 2px 4px #b3b3b3, -2px -2px 4px #ffffff;
}

.control-button img:hover {
  box-shadow: none;
  border: 1px solid var(--fourth-color);
}

.progress-container {
  width: 100%;
  margin: 10px;
  justify-content: center;
}

/* 进度条 */
.level {
  width: 60%;
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  height: 10px;
  background: var(--fourth-color);
  overflow: hidden;
  border-radius: 5px;
  -webkit-transition: height 5s;
  -o-transition: height 5s;
  transition: height 5s;
  cursor: pointer;
}

.level::-webkit-slider-thumb {
  -webkit-appearance: none;
  width: 0;
  height: 0;
  -webkit-box-shadow: -2000px 0 0 2000px;
  box-shadow: -1000px 0 5px 1000px var(--main-color),
  -1000px 0 20px 1000px var(--main-color);
}

.progress_volume {
  justify-content: end;
}

.progress_volume img {
  width: 5%;
}

.pl_effect {
  width: 50%;
  border-radius: 10%;
  position: relative;
  border: 1px solid var(--fourth-color);
  user-select: none;
  box-shadow: 12px 12px 24px #b3b3b3, -12px -12px 24px #ffffff;
}

.stylus {
  /* 重新定位到父容器右上角 */
  width: 15%;
  position: absolute;
  right: 5%;
  top: 0;
  z-index: 10;
  transition: all 0.5s;
  transform-origin: 60% 15%; /* 设置旋转基点为上边缘的中间 */
}

.sp {
  transform: rotate(-12deg);
}

.sr {
  transform: rotate(3deg);
}

.play_style {
  width: 100%;
  padding-top: 100%; /* 设置 padding-top 为宽度的百分比 */
  position: relative;
}

.play_style > img {
  width: 15%;
  position: absolute;
  right: 0;
  top: 0;
  z-index: 10;
  transition: all 0.5s;
}

.pic:nth-child(1) {
  width: 70%;
  top: 15%;
  left: 15%;
  position: absolute;
  border-radius: 50%;
  z-index: 4;
  background-size: cover;
  transition: 0.5s all;
}

.pic img {
  border-radius: 50%;
}

.pic:nth-child(2) {
  width: 94%;
  position: absolute;
  top: 3%;
  left: 3%;
  border-radius: 50%;
  z-index: 3;
}

.pic img {
  width: 100%;
}

.pic:nth-child(3) {
  width: 96%;
  position: absolute;
  top: 2%;
  left: 2%;
  border-radius: 50%;
  background-image: linear-gradient(var(--main-color), var(--second-color));
  z-index: 2;
  padding-top: 95%; /* 设置 padding-top 为宽度的百分比 */
}

.pl_control {
  width: 80%;
}

.songPaused {
  animation: rotate 8s infinite linear paused;
}

.songRunning {
  animation: rotate 8s infinite linear running;
}

.pl_comment {
  width: 100%;
  height: 50%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  font-size: var(--fontSize);
}

.pl_detail {
  width: 100%;
  height: 50%;
  display: flex;
  flex-direction: column;
  justify-content: start;
  align-items: center;
}

.detail {
  width: 50%;
  height: 10%;
  display: flex;
  justify-content: start;
  align-items: end;
  gap: 1vw;
}

.detail img {
  height: 70%;
  border-radius: 8px;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.play_right {
  width: 50%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 1vw;
}

.prd {
  height: 10%;
}

.prd h1 {
  color: var(--main-color);
}

.lyricsContainer {
  max-width: 100%;
  height: 70%;
  overflow-y: auto;
  padding: 5%;
  border-radius: 10px;
  box-sizing: border-box;
}

.lyric {
  display: block;
  line-height: 2em;
  border-radius: 5px;
  transition: 0.2s all ease-in-out;
  cursor: pointer;
  margin-bottom: 10px;
}

.lyric:hover {
  background-color: var(--fourth-color);
}

.current {
  color: var(--main-color);
  font-size: 22px;
  font-weight: 600;
}

.lyricSpacer {
  height: 50%;
}

.other {
  width: 50%;
  height: 8%;
  display: flex;
  justify-content: space-between;
}

.other div {
  width: 20%;
  height: 100%;
  cursor: pointer;
  transition: 0.1s ease;
}

.other div img {
  width: 40%;
  padding: 5%;
  border-radius: 5px;
}

.o1 {
  flex-direction: column;
}

.bac {
  box-shadow: inset 2px 2px 4px #b3b3b3, inset -2px -2px 4px #ffffff;
}

.anm {
  animation: keyframes-fill .5s;
}

@keyframes keyframes-fill {
  0% {
    transform: rotate(-180deg) scale(0);
    opacity: 0;
  }

  50% {
    transform: rotate(-10deg) scale(1.2);
  }
}

</style>