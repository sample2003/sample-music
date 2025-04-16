<template>
  <div id="Search">
    <div class="exist">
      <div class="title flex">
        <div class="flex">
          <h2 style="margin: 0 10px;">{{ condition }}</h2><span>的搜索结果如下</span>
        </div>
        <div class="nav">
          <button
              class="btn"
              v-for="(b, index) in btn"
              :key="b.fx"
              @click="conditionSearch(b.fx, index)"
              :class="{ 'it': currentBtn === index }"
          ><span>{{ b.name }}</span></button>
        </div>
      </div>
      <div class="list">
        <SongList
            @add="handleAdd"
            :songs="songsBySearch"
            :operator="'album'"></SongList>
      </div>
      <PageNation
          class="page" v-if="!isExistC && !isExistS"
          :totalItems="songsBySearch.total || 0"
          :pageSize="itemsPerPage"
          :pageSizeOptions="pageSizeOptions"
          :currentPage.sync="currentPage"
          @update:pageSize="handlePageSizeChange"
          @update:currentPage="fetchData"
      />
    </div>
    <div class="e1" v-if="!isExistC && isExistS">
      <h1>暂未搜索到歌曲</h1>
    </div>
    <div class="e1" v-else-if="isExistC && isExistS">
      <h1>暂未搜索歌曲</h1>
    </div>
  </div>
</template>

<script>
import {mapState} from 'vuex'
import PageNation from "@/components/PageNation.vue";
import SongService from "@/api/service/SongService";
import SongList from "@/pages/music/SongList.vue";
import RelateService from "@/api/service/RelateService";
import PlaylistService from "@/api/service/PlaylistService";
import Icon from "@/util/common/Icon";

export default {
  name: "MusicMainSearch",
  components: {
    SongList,
    PageNation
  },
  data() {
    return {
      itemsPerPage: 10,
      currentPage: 1,
      pageSizeOptions: [10, 20, 50, 100],
      currentBtn: 0,
      btn: [
        {name: "综合", fx: "all"},
        {name: "单曲", fx: "title"},
        {name: "歌手", fx: "artist"},
        {name: "专辑", fx: "album"},
        {name: "风格", fx: "style"},
        {name: "标签", fx: "tag"}]
    };
  },
  props: {
    type: {  // 搜索的
      type: String,
      default: 'title'
    },
    keyword: {  // 新增路由参数prop
      type: String,
      default: ''
    }
  },
  watch: {
    currentPage() {
      this.fetchData();
    },
    itemsPerPage() {
      this.fetchData();
    },
    // 监听路由参数变化
    '$route.params': {
      handler(newParams) {
        this.handleRouteChange(newParams)
      },
      immediate: true
    }
  },
  computed: {
    Icon() {
      return Icon
    },
    condition() {  // 改为从路由参数获取
      return this.$route.params.keyword || ''
    },
    isExistC() {
      return this.condition === null || this.condition === '';
    },
    isExistS() {
      return Object.keys(this.songsBySearch).length === 0 || this.songsBySearch == null || this.songsBySearch.items.length === 0;
    },
    ...mapState({
      songs: state => state.music.songsBySearch
    })
  },
  methods: {
    // 获取歌曲
    ha() {
      let aa = SongService.conditionAndPaged(this.searchParams)

    },






    async handleRouteChange(params) {
      // 同步参数到搜索条件
      this.currentBtn = this.btn.findIndex(b => b.fx === (params.type || 'title'))
      await this.fetchData()
    },
    async conditionSearch(fx, index) {
      // 修改为更新路由而非直接请求
      this.$router.push({
        name: 'MusicSearch',
        params: {
          type: fx,
          keyword: this.condition
        }
      })
    },
    // 加载页面时，根据参数
    async fetchData() {
      const res = await SongService.conditionAndPaged(
          this.condition,
          this.$route.params.type || 'title', // 使用路由参数
          null,
          this.currentPage,
          this.itemsPerPage
      )
      await this.setSongsBySearch(res)
    },
    handlePageSizeChange(newSize) {
      this.itemsPerPage = newSize; // 更新每页显示的条目数
      this.currentPage = 1; // 重置当前页为1
      this.fetchData(); // 调用fetchData来获取新页的数据
    },
    // 增加歌曲至歌单
    async handleAdd(ids) {
      const playlists = await PlaylistService.UserPagedQuery("false", null, 1, 100);
      this.$change("添加歌曲到歌单", playlists.data.items, (confirmed, targetId) => {
        if (confirmed) {
          RelateService.addSongsToPlaylist(targetId, ids)
        }
      })
    },
    saveFileName(icon, event) {
      icon.name = event.target.value;
      icon.editable = false;
    },
  },
  mounted() {
    this.fetchData();
  }
};
</script>

<style scoped>
#Search {
  width: 100%;
  height: 100%;
}

.exist {
  width: 100%;
  height: 89%;
  font-size: var(--fontSize);
  overflow: auto;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
}

.title {
  height: 10%;
}

.list {
  width: 95%;
  height: 85%;
}

.page {
  width: 60%;
  height: 5%;
}

.btn {
  border: 1px solid #0000;
  padding: 5px;
  margin: 0 5px;
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.2s;
  background-color: var(--second-color);
  box-shadow: 2px 2px 4px #b3b3b3, -2px -2px 4px #ffffff;
}

.btn:hover {
  box-shadow: none;
  border: 1px solid var(--fourth-color);
}

.it {
  box-shadow: inset 2px 2px 4px #b3b3b3, inset -2px -2px 4px #ffffff;
}

.it span {
  color: var(--main-color);
}
</style>