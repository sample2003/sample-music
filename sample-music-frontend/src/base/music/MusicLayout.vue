<template>
  <div id="Music">
    <Side id="Side"/>
    <Main id="Main"/>
    <transition name="fade" mode="out-in">
      <Control id="Control" v-if="show" key="control"/>
    </transition>
    <div class="navigate">
      <!-- 左边框 -->
      <div class="n1">

      </div>
      <div class="n2"></div>
      <TextInput
          style="width: 20%;height: 90%;"
          @inputChange="handleInput"
          @fetch="fetchSongs"
          method="fetchSongs"
          :search-icon="true"
          :cancel-icon="true"
          message="搜索歌曲吧">
      </TextInput>
      <!-- 右边框 -->
      <div class="nn1" @click="userDetail ? jumpUserCenter() : jumpUserEnter()">
        <img :src="userDetail?.avatar ?? Icon.notLoginIcon" alt="">
        <span>{{ userDetail?.username ?? accessEnum.NOT_LOGIN }}</span>
      </div>
      <div class="nn2">
        <a href="https://www.kugou.com/" target="_blank"><img :src="Icon.kgyyIcon" alt=""></a>
        <a href="https://y.qq.com/" target="_blank"><img :src="Icon.qqyyIcon" alt=""></a>
        <a href="https://music.163.com/" target="_blank"><img :src="Icon.wyyyyIcon" alt=""></a>
        <a href="https://www.kuwo.cn/" target="_blank"><img :src="Icon.kwyyIcon" alt=""></a>
      </div>
      <div class="nn3" @mouseenter="showWantList" @mouseleave="hideWantList">
        <TextInput message="我想听"></TextInput>
        <div class="want-lists" v-show="isShowWantList">
          <div class="want-list">
            <p>啊</p>
            <img :src="Icon.completeIcon" alt="">
          </div>
          <div class="want-list">
            <p>啊</p>
            <img :src="Icon.completeIcon" alt="">
          </div>
          <div class="want-list">
            <p>啊</p>
            <img :src="Icon.completeIcon" alt="">
          </div>
          <div class="want-list">
            <p>啊</p>
            <img :src="Icon.completeIcon" alt="">
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Side from './MusicSide.vue'
import Control from './MusicControl.vue'
import Main from './MusicMain.vue'
import Icon from "@/util/common/Icon";
import TextInput from "@/components/TextInput.vue";
import accessEnum from "../../util/access/accessEnum";

export default {
  name: 'MusicLayout',
  components: {TextInput, Side, Control, Main},
  data() {
    return {
      value: '',
      wantListener: '',
      isShowWantList: false
    }
  },
  computed: {
    accessEnum() {
      return accessEnum
    },
    Icon() {
      return Icon
    },
    show() {
      const isPlayRoute = this.$route.path.includes('/music/play');
      const isEmpty = this.songPlaying == null || Object.keys(this.songPlaying).length === 0;
      return !isEmpty && !isPlayRoute;
    }
  },
  methods: {
    handleInput(value) {
      this.value = value;
    },
    jumpUserCenter() {
      // 导航到新路由
      this.$router.push({path: `/user/center`}, () => {
      })
    },
    jumpUserEnter() {
      // 导航到新路由
      this.$router.push({path: `/user/enter`}, () => {
      })
    },
    // 展示
    showWantList() {
      this.isShowWantList = true;
    },
    // 隐藏
    hideWantList() {
      this.isShowWantList = false;
    },
    async fetchSongs() {

      this.$router.push({
        name: "search",
        params: {
          condition: this.value,
          params: 'ALL',
          sortType: 'LISTENERS_DESC',
          pageNum: 1,
          pageSize: 10
        }
      }, () => {})
    },
  },
}
</script>

<style scoped>
#Music {
  width: 100%;
  height: 100%;
  display: grid;
  grid-template: 0.5fr 8.5fr 1fr / 1fr 9fr;
  overflow: hidden;
}

#Main {
  min-width: 900px;
  min-height: 540px;
  grid-row: 2 / span 2;
  grid-column: 2 / span 1;
}

#Side {
  grid-row: 1 / span 3;
  grid-column: 1 / span 1;
}

#Control {
  grid-row: 3 / span 1;
  grid-column: 1 / span 2;
}

.navigate {
  grid-row: 1 / span 1;
  grid-column: 2 / span 1;
  width: 98%;
  height: 5vh;
  min-width: 1010px;
  min-height: 40px;
  padding-right: 2%;
  cursor: pointer;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.nn1 {
  width: 10%;
  height: 90%;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 40px;
  user-select: none;
  background-size: 300% 100%;
  animation: changeColor 4s ease infinite running;
  background-image: linear-gradient(315deg, var(--main-color), var(--second-color));
}

.nn1 img {
  height: 80%;
  border-radius: 50%;
  width: auto; /* 先设置高度为自动，后续根据宽度来等比例调整高度 */
  aspect-ratio: 1 / 1; /* 设置宽高比为1:1，确保图片为正方形 */
  object-fit: contain; /* 使用 contain 属性，让图片在保持宽高比的前提下，尽可能填满容器，同时不会变形 */
}

.nn1 span {
  width: 70%;
  overflow: hidden;
  text-overflow: ellipsis;
  transition: 0.2s all ease;
}

.nn1:hover span {
  letter-spacing: 2px;
}

.nn2 {
  width: 15%;
  height: 100%;
  display: flex;
  justify-content: space-around;
  align-items: center;
}

.nn2 a {
  height: 50%;

}

.nn2 img {
  height: 100%;
}

.nn3 {
  width: 10%;
  height: 80%;
  position: relative;
}

.want-lists {
  width: 90%;
  top: 100%;
  position: absolute;
  background-color: var(--second-color);
  border-radius: 5px;
  border: 1px solid var(--fourth-color);
}

.want-list {
  width: 100%;
  height: 35px;
  display: flex;
  justify-content: center;
  align-items: center;
  border: 1px solid var(--fourth-color);
}

.want-list:hover {
  background-color: var(--fourth-color);
}

.want-list img {
  height: 50%;
}

.s1 {
  width: 20%;
  height: 90%;
}

.s2 {
  width: 90%;
}

.n1 {
  width: 10%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.n2 {
  width: 25%;
}

@keyframes changeColor {
  0% {
    background-position: 0;
  }
  50% {
    background-position: 100%;
  }
  100% {
    background-position: 0;
  }
}
</style>