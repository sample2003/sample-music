<template>
  <div id="userInfo">
    <div class="userDetail">

      <div class="nav">
        <div
            class="btn"
            v-for="b in btn"
            :key="b.btnID "
            @click="currentPage = b.btnName"
        >
          <span>{{ b.name }}</span>
          <button>增加</button>
          <button>删除</button>
          <button>更改</button>
          <button>查询</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {nanoid} from "nanoid";

export default {
  name: "UserInfo",
  data() {
    return {
      btn: [
        {btnID: nanoid(), btnName: "song", name: "歌曲"},
        {btnID: nanoid(), btnName: "playlist", name: "歌单"},
        {btnID: nanoid(), btnName: "album", name: "专辑"},
        {btnID: nanoid(), btnName: "artist", name: "艺术家"},
        {btnID: nanoid(), btnName: "user", name: "用户"},
        {btnID: nanoid(), btnName: "publicity", name: "公告"},
        {btnID: nanoid(), btnName: "work", name: "工单"},
        {btnID: nanoid(), btnName: "comment", name: "评论"},
      ],
    }
  },
  methods: {
    toggleLike() {
      this.isLiked = !this.isLiked;
    },
    existCondition() {
      return this.condition !== null || this.condition !== '';
    },
    notSongs() {
      return Object.keys(this.songsBySearch).length === 0 || this.songsBySearch.total === 0;
    },
  }
}
</script>

<style scoped>
#userInfo {
  width: 100%;
  height: 100%;
}

.nav {
  width: 80%;
  display: flex;
  justify-content: start;
  align-items: center;
}

.btn {
  width: 10%;
  padding-top: 10%;
  position: relative;
  cursor: pointer;
}

.btn img {
  width: 50%;
  position: absolute;
  top: 25%;
  left: 25%;
  z-index: 2;
}

.btn span {
  width: 50%;
  height: 25%;
  padding: 12.5% 0;
  text-align: center;
  color: #fff;
  background-color: var(--main-color);
  position: absolute;
  bottom: 25%;
  left: 25%;
  z-index: 3;
}

button {
  border: none;
  cursor: pointer;
  transition: 0.2s all ease;
  background-color: rgba(255, 255, 255, 0);
  z-index: 1;
}

button:hover {
  color: var(--second-color);
  background-color: var(--main-color);
}

.btn button:nth-of-type(1) {
  width: 25%;
  height: 50%;
  position: absolute;
  left: 25%;
  top: 25%;
  opacity: 0;
  writing-mode: tb-rl;
  /* 从左上开始顺时针 */
  border-radius: 5px 0 0 5px;
  border-left: 2px solid var(--main-color);
}

.btn:hover button:nth-of-type(1) {
  left: 0;
  opacity: 1;
}

.btn button:nth-of-type(2) {
  width: 25%;
  height: 50%;
  position: absolute;
  right: 25%;
  top: 25%;
  writing-mode: tb-rl;
  border-radius: 0 5px 5px 0;
  border-right: 2px solid var(--main-color);
}

.btn:hover button:nth-of-type(2) {
  right: 0;
}

.btn button:nth-of-type(3) {
  width: 50%;
  height: 25%;
  position: absolute;
  left: 25%;
  bottom: 25%;
  border-radius: 0 0 5px 5px;
  border-bottom: 2px solid var(--main-color);
}

.btn:hover button:nth-of-type(3) {
  bottom: 0;
}

.btn button:nth-of-type(4) {
  width: 50%;
  height: 25%;
  position: absolute;
  left: 25%;
  top: 25%;
  border-radius: 5px 5px 0 0;
  border-top: 2px solid var(--main-color);
}

.btn:hover button:nth-of-type(4) {
  top: 0;
}
</style>