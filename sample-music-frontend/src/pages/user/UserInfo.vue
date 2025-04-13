<template>
  <div id="userInfo">
    <div class="left">
      <div class="left-top">
        <div></div>
        <img :src="userDetail.avatar" alt="">
      </div>
      <div class="left-bottom">
        <div>
          <p>简介：{{ userDetail.description }}</p>
        </div>
      </div>
    </div>
    <div class="right-top">
      <p>{{ userDetail.username }}</p>
      <p>性别：{{ userDetail.sex }}</p>
      <p>关注数：{{ userDetail.likes }}</p>

      <p>等级：{{ userDetail.exp }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "UserInfo",
  data() {
    return {
    }
  },
  methods: {
  },
  mounted() {
    console.log(this.userDetail)
  }
}
</script>

<style scoped>
#userInfo {
  width: 100%;
  height: 100%;
  display: flex;
  gap: 1%;
}

.left {
  width: 15%;
  height: 100%;
  display: flex;
  gap: 1%;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}

.left-top {
  width: 80%;
  display: flex;
  align-items: center;
  justify-content: center;
  height: auto; /* 先设置高度为自动，后续根据宽度来等比例调整高度 */
  aspect-ratio: 1 / 1; /* 设置宽高比为1:1，确保图片为正方形 */
  object-fit: contain;
}

.left-top img {
  width: 80%;
  border-radius: 10px;
  height: auto; /* 先设置高度为自动，后续根据宽度来等比例调整高度 */
  aspect-ratio: 1 / 1; /* 设置宽高比为1:1，确保图片为正方形 */
  object-fit: contain;
}

.left-bottom {
  width: 100%;
  flex-grow: 1;
  background-color: #48435a;
}

.right-top {
  background-color: #48435a;
}
</style>