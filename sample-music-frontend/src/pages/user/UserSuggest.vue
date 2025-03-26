<template>
  <div id="userSuggest">
    <div class="capsule">
      <div class="endcap endcap-start"></div>
      <div class="body"></div>
      <div class="endcap endcap-end"></div>
      <button @click="test">aaaaaa</button>
    </div>
  </div>
</template>

<script>
import { parseFile } from 'music-metadata';
export default {
  name: "UserSuggest",
  data() {
    return {

    }
  },
  methods: {
    async test() {
      const metadata = await parseFile("D:\\KwDownload\\song");
      console.log(metadata)
    }
  }
}
</script>

<style scoped>
#userSuggest {
}

.capsule {
  display: flex;
  align-items: center;
  overflow: hidden; /* 防止圆头部分溢出 */
}

.endcap, .body {
  height: 50px; /* 胶囊的高度 */
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #8b80af; /* 胶囊的颜色 */
  color: white; /* 文字颜色 */
  font-size: 18px; /* 文字大小 */
}

.endcap-start {
  border-top-left-radius: 25px; /* 左端圆角 */
  border-bottom-left-radius: 25px;
  width: 50px; /* 左端宽度 */
}

.endcap-end {
  border-top-right-radius: 25px; /* 右端圆角 */
  border-bottom-right-radius: 25px;
  width: 50px; /* 右端宽度 */
}

.body {
  flex-grow: 1; /* 主体部分占据剩余空间 */
  border-radius: 0; /* 主体部分不需要圆角 */
  width: 100%; /* 或者其他你需要的长度 */
}
</style>