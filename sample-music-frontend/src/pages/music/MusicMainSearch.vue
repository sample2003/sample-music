<template>
  <div id="Search">
    <div class="exist">
      <div class="title flex">
        <div class="flex">
          <h2 style="margin: 0 10px;">{{ searchParams.condition }}</h2><span>的搜索结果如下</span>
        </div>
        <div class="nav">
          <button
              class="btn"
              v-for="(b, index) in btn"
              :key="b.fx"
              @click="conditionSearch(b.fx)"
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
          :pageSize="searchParams.pageSize"
          :pageSizeOptions="pageSizeOptions"
          :currentPage.sync="searchParams.pageNum"
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
import PageNation from "@/pages/common/PageNation.vue";
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
      pageSizeOptions: [10, 20, 50, 100],
      currentBtn: 0,
      btn: [
        {name: "综合", fx: "ALL"},
        {name: "单曲", fx: "TITLE"},
        {name: "歌手", fx: "ARTIST"},
        {name: "专辑", fx: "ALBUM"},
        {name: "风格", fx: "STYLE"},
        {name: "标签", fx: "TAG"}]
    };
  },
  watch: {
    currentPage() {
      this.fetchData();
    },
    itemsPerPage() {
      this.fetchData();
    }
  },
  computed: {
    Icon() {
      return Icon
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
    // 加载页面时，根据参数
    async fetchData() {
      await this.getSongsByCondition(this.searchParams);
    },
    async handleRouteChange(params) {
      // 同步参数到搜索条件
      this.currentBtn = this.btn.findIndex(b => b.fx === (params.type || 'ALL'))
      await this.fetchData()
    },
    async conditionSearch(fx) {
      this.setSearchParams([...this.searchParams.sortType, fx])
      // 修改为更新路由而非直接请求
      await this.fetchData()
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