<template>
  <div id="songList">
    <div v-if="songs && songs.length !== 0" class="songs flex">
      <!-- 开始行 -->
      <div class="song flex songNav">
        <input
            type="checkbox"
            class="songCheck"
            v-model="wantChecked"
            @change="toggleAll">
        <div class="message flex">
          <span style="width: 45%;">{{ songs.length }}首歌曲</span>
          <span style="width: 15%;">歌手</span>
          <span style="width: 27%;">专辑</span>
          <span style="width: 10%">时长</span>
        </div>
        <div v-if="selectedSongs.length > 0" class="operator operatorNav flex">
          <img :src="Icon.playIcon" alt="" @click="playAllSongs(operator, songs)">
          <img :src="Icon.addIcon" alt="" @click="handleAdd(selectedSongs)">
          <img v-if="operator === 'playlist'" :key="operator" :src="Icon.deleteIcon" alt=""
               @click="handleDelete(selectedSongs)">
          <img v-else-if="operator === 'album'" :key="operator" :src="Icon.notLoveIcon" alt=""
               @click="handleDelete(selectedSongs)">
        </div>
        <div v-else class="operator">

        </div>
      </div>

      <!-- 歌曲列表 -->
      <div
          class="song flex"
          v-for="s in songs"
          :key="s.id"
          :class="{ 'songPlayed': songPl(s.title) }"
          @dblclick="playBySong(s)"
      >
        <input
            type="checkbox"
            class="songCheck"
            :value="s.id"
            v-model="selectedSongs"
        >
        <div class="message flex">
          <div class="first">
            <img :src="s.cover" alt="">
            <div>
              <span style="margin-bottom: 5px;">{{ s.title }}</span>
              <span v-if="s.permission !== 0" class="tag"> VIP </span>
              <span v-for="(t, index) in s.tags" :key="index" class="tag"> {{ t }}</span>
            </div>
          </div>
          <div style="width: 15%;display: flex;justify-content: start;">
            <span class="artist">{{ s.artist || '-' }}</span>
            <span v-for="(a, index) in s.artists" :key="index">&<span class="artist">{{ a }}</span></span>
          </div>
          <div style="width: 27%;display: flex;justify-content: start;"><span>{{ s.album || '-' }}</span></div>
          <div style="width: 10%;display: flex;justify-content: start;"><span>{{durationInMinutes(s.duration) || '-' }}</span></div>
        </div>
        <div v-if="operator === 'playlist'" class="operator flex">
          <img :src="Icon.playIcon" alt="" @click="playBySong(s)">
          <img :src="Icon.addIcon" alt="" @click="handleAdd([s.id])">
          <img :src="Icon.deleteIcon" alt="" @click="handleDelete([s.id])">
        </div>
        <div v-if="operator === 'album'" class="operator flex">
          <img :src="Icon.playIcon" alt="" @click="playBySong(s)">
          <img :src="Icon.addIcon" alt="" @click="handleAdd([s.id])">
          <img :src="Icon.notLoveIcon" alt="" @click="handleDelete([s.id])">
        </div>
      </div>
    </div>
    <div v-else>
      <h3>暂无歌曲</h3>
    </div>
  </div>
</template>

<script>
import Icon from "@/util/common/Icon";

export default {
  name: 'SongList',
  props: {
    songs: {
      type: Array,
      default: () => []  // 设置默认值为空数组
    },
    operator: String
  },
  data() {
    return {
      selectedSongs: [],
      wantChecked: false,
      tags: []
    }
  },
  watch: {
    // 查看是否全部选中
    selectedSongs(val) {
      this.wantChecked = val.length === this.songs.length;
    }
  },
  computed: {
    Icon() {
      return Icon
    }
  },
  methods: {
    songPl(title) {
      return this.songPlaying.title === title
    },
    // 全选或全不选
    toggleAll() {
      if (this.wantChecked) {
        this.selectedSongs = this.songs.map(s => s.id);
        console.log(this.selectedSongs)
      } else {
        this.selectedSongs = [];
      }
    },
    // 添加
    handleAdd(ids) {
      if (ids.length > 0) {
        this.$emit("add", ids)
      } else {
        this.$message("未选中歌曲")
      }
    },
    // 移除
    handleDelete(ids) {
      if (ids.length > 0) {
        this.$emit("delete", ids)
      } else {
        this.$message("未选中歌曲")
      }
    },
    crack() {
      alert("功能开发中")
    },
    durationInMinutes(seconds) {
      const minutes = Math.floor(seconds / 60);
      const remainingSeconds = seconds % 60;
      return `${minutes}:${remainingSeconds < 10 ? '0' : ''}${remainingSeconds}`;
    }
  },
  mounted() {
    this.$on("crack", this.crack)
  },
  beforeDestroy() {
    this.$off("crack", this.crack)
  }
}
</script>

<style scoped>
#songList {
  width: 100%;
  height: 100%;
  overflow: auto;
}

/* 歌曲列表 */
.songs {
  width: 100%;
  height: 100%;
  justify-content: start;
  gap: 5px;
  flex-direction: column;
}

.song {
  width: 100%;
  height: 8%;
  min-height: 40px;
  border-radius: 5px;
  background: var(--second-color);
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
}

.song:hover, .songNav {
  background: var(--fourth-color);
  box-shadow: 1px 2px 2px #0000001a;
}

.song span {
  cursor: pointer;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 选择框设计 */
.songCheck {
  appearance: none;
  width: 1.5em;
  height: 1.5em;
  margin: 0 5px;
  position: relative;
  outline: none;
  background: #ffffff80;
  backdrop-filter: blur(10px);
  border: 1px solid #888;
  border-radius: 50%;
  transition: 0.2s all;
  cursor: pointer;
}

.songCheck:hover {
  border: 1px solid var(--main-color);
}

.songCheck:checked {
  background: var(--main-color);
  border: 1px solid var(--main-color);
}

.songCheck::after {
  content: "";
  height: 25%;
  width: 50%;
  border: #fff solid 3px;
  position: absolute;
  top: 4px;
  left: 3px;
  opacity: 0;
  border-top: none;
  border-right: none;
  transform: rotate(-15deg);
  transition: 0.2s;
}

.songCheck:checked::after {
  content: "";
  height: 25%;
  width: 50%;
  border: #fff solid 3px;
  position: absolute;
  top: 4px;
  left: 3px;
  opacity: 1;
  border-top: none;
  border-right: none;
  transform: rotate(-45deg);
}

.message {
  width: 80%;
  height: 100%;
  justify-content: start;
  gap: 1%;
}

.songPlayed {
  background: var(--main-color);
}

.songPlayed:hover {
  background: var(--main-color);
}

.songPlayed .tag {
  border: 1px solid var(--second-color);
  color: var(--second-color);
}

.message span {
  overflow: hidden;
  text-overflow: ellipsis;
  text-align: left;
}

.first {
  width: 45%;
  height: 100%;
  display: flex;
  justify-content: start;
  align-items: center;
  gap: 1%;
}

.first img {
  height: 90%;
  border-radius: 5px;
}

.first div {
  width: 89%;
  display: flex;
  flex-wrap: wrap;
}

.first div span {
  width: auto;
}

.first div span:nth-of-type(1) {
  width: 100%;
}

.tag {
  font-size: 0.6em;
  border-radius: 3px;
  border: 1px solid var(--main-color);
  padding: 1px 2px 2px 2px;
  margin-right: 2px;
  color: #48435a;
}

.artist:hover {
  text-decoration: underline;
}

.operator {
  width: 15%;
  height: 100%;
  justify-content: center;
  gap: 5%;
}

.operator img {
  height: 45%;
  cursor: pointer;
}

.operatorNav {
  position: relative;
}

.operatorNav img {
  display: flex;
  justify-content: space-around;
  align-items: center;
  overflow: hidden;
  cursor: pointer;
}

.operatorNav:hover span {
  opacity: 0;
}

</style>