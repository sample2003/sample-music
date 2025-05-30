<template>
  <div id="MusicPublicity">
    <div class="publicityDetail" v-show="flag">
      <div class="top flex">
        <div class="title">
          <p>公告</p>
          <h1>{{ publicity.title }}</h1>
        </div>

        <div class="right flex">
          <div v-if="userDetail && userDetail.role === ACCESS_ENUM.ADMIN">
            <div class="kick flex" @click="showEdit">
              <img :src="Icon.updateIcon" alt="" class="userAvatar">
              <span class="tag">修改</span>
            </div>
          </div>
          <div class="kick flex">
            <img :src="publicity.userAvatar" alt="" class="userAvatar" style="width: 30px;border-radius: 5px;margin: 2px;">
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
      <div class="markdown-content" v-html="compiledMarkdown"></div>
    </div>

    <div class="publicityEdit" v-show="!flag">
      <MdEditor :content="this.publicity.message" :show-edit="true"></MdEditor>
    </div>
  </div>
</template>

<script>
import PublicityService from "@/api/service/PublicityService";
import Icon from "@/util/common/Icon";
import md from "@/util/markdown";
import ACCESS_ENUM from "@/util/access/accessEnum";
import MdEditor from "@/components/MDEditor.vue";

export default {
  name: "MusicPublicity",
  components: {MdEditor},
  computed: {
    ACCESS_ENUM() {
      return ACCESS_ENUM
    },
    Icon() {
      return Icon
    },
    compiledMarkdown() {
      return md.render(this.publicity.message)
    }
  },
  data() {
    return {
      publicityId: null,
      publicity: {},
      flag: true,
    }
  },
  methods: {
    async initParams () {
      const data = await PublicityService.query(this.publicityId);
      this.publicity = data.data;
      console.log(this.publicity.message)
    },
    showEdit() {
      this.flag = false
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
  height: 100%;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  justify-content: start;
  align-items: start;
  overflow: scroll;
}

.top {
  width: 90%;
  margin-left: 5%;
  justify-content: space-between;
  flex-wrap: wrap;
}

.markdown-content {
  width: 80%;
  margin-left: 10%;
  text-align: start !important;
  font-family: -apple-system, BlinkMacSystemFont, Segoe UI, Helvetica, Arial, sans-serif, Apple Color Emoji, Segoe UI Emoji, Times New Roman;
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

.publicityEdit {
  width: 100%;
  height: 100%;
  display: flex;
}

</style>