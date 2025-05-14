<template>
  <div id="MusicPublicity">

    <div class="top flex">
      <div class="title">
        <p>公告</p>
        <h1>{{ publicity.title }}</h1>
      </div>

      <div class="right flex">
        <div class="kick flex">
          <img :src="publicity.userAvatar" alt="" class="userAvatar" style="width: 30px;">
          <span class="tag">{{ publicity.username }}</span>
        </div>
        <div class="kick flex">
          <img :src="Icon.eyeIcon" alt="">
          <span class="tag">{{ publicity?.viewers }}</span>
        </div>
        <div class="kick flex">
          <img :src="Icon.calendarIcon" alt="">
          <span class="tag">{{ publicity.createTime }}</span>
        </div>
      </div>
    </div>
    <img :src="publicity.cover" alt="" class="mainImg">
    <p>{{ publicity.message }}</p>
  </div>
</template>

<script>
import PublicityService from "@/api/service/PublicityService";
import Icon from "@/util/common/Icon";

export default {
  name: "MusicPublicity",
  computed: {
    Icon() {
      return Icon
    }
  },
  data() {
    return {
      publicityId: null,
      publicity: {}
    }
  },
  methods: {
    async initParams () {
      const data = await PublicityService.query(this.publicityId)
      this.publicity = data.data
    }
  },
  created() {
    this.publicityId = this.$route.params.id || this.publicityId;
    this.initParams();
  },
  watch: {
    '$route'(to) {
      this.publicityId = to.params.id || this.publicityId;
    }
  },
}
</script>

<style scoped>
#MusicPublicity {
  width: 60%;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: start;
  overflow: scroll;
}

.top {
  width: 90%;
  margin-left: 5%;
  justify-content: space-between;
  flex-wrap: wrap;
}

.title {
  display: flex;
  border-radius: 5px;
  padding: 5px;
  margin: 1%;
  flex-grow: 1;
  white-space: wrap;
}

.title p {
  padding: 5px 8px;
  margin-right: 10px;
  background-color: var(--main-color);
  border-radius: 5px;
  color: var(--second-color);
  white-space: nowrap;
  align-self: center;
}

.title h1 {
  white-space: wrap;
}

.right {
  gap: 10px;
}

.mainImg {
  width: 80%;
  margin: 0 10%;
  border-radius: 10px;
}

.kick {
  margin-right: 5px;
  border-radius: 5px;
  box-shadow: 2px 2px 4px #b3b3b3, -2px -2px 4px #ffffff;
}

.kick p {
  padding: 5px;
  align-self: flex-start; /* 确保顶部对齐 */
  white-space: nowrap; /* 禁止换行 */
}

.tag {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2px;
  margin: 2px;
  font-size: 12px;
  text-align: left;
  border: 1px solid var(--main-color);
  border-radius: 5px;
  background-color: #8b80af80;
  cursor: pointer;
  white-space: nowrap; /* 禁止换行 */
}

.kick img {
  padding: 1px;
  width: 20px;
}
</style>