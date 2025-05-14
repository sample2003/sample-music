<template>
  <div id="Search">
    <div class="exist">
      <div class="exist-top flex">
        <div class="flex">
          <h2 style="margin: 0 10px;box-sizing: border-box">{{ condition ? condition : "综合"}}</h2><p>搜索结果如下</p>
        </div>
        <div class="nav">
          <ButtonSelect
              :button-list="btn"
              :is-active="(item) => currentBtn === item.fx"
              :click-param-field="'fx'"
              :key-field="(item) => item.fx"
              @button-click="handleButtonChange"
          ></ButtonSelect>
        </div>
      </div>
      <div class="exist-bottom flex" v-if="songsBySearch.items.length > 0">
        <div class="list">
          <SongList
              @add="handleAdd"
              :songs="songsBySearch"
              :operator="'album'" />
        </div>
        <PageNation
            class="page"
            :totalItems="songsBySearch.total || 0"
            :pageSize.sync="localPageSize"
            :currentPage.sync="localPageNum"
            @update:pageSize="handlePageSizeChange"
            @update:currentPage="handlePageChange"
        />
      </div>
      <h2 v-else style="margin: 5%;">未搜索出歌曲</h2>
    </div>
<!--    <div class="e1" v-if="!isExistC && isExistS">
      <h1>未搜索到歌曲</h1>
    </div>
    <div class="e1" v-else-if="isExistC && isExistS">
      <h1>暂未搜索歌曲</h1>
    </div>-->
  </div>
</template>

<script>
import {mapState} from 'vuex'
import PageNation from "@/components/PageNation.vue";
import SongList from "@/components/SongList.vue";
import Icon from "@/util/common/Icon";
import ButtonSelect from "@/components/ButtonSelect.vue";

export default {
  name: "MusicMainSearch",
  components: {ButtonSelect, SongList, PageNation},
  props: {
    show: {},
    condition: { type: String },
    params: { type: String },
    sortType: { type: String, default: 'LISTENERS_DESC' },
    pageNum: { type: Number, default: 1 },
    pageSize: { type: Number, default: 10 }
  },
  data() {
    return {
      currentBtn: 'ALL',
      btn: [
        {name: "综合", fx: "ALL"},
        {name: "单曲", fx: "TITLE"},
        {name: "歌手", fx: "ARTIST"},
        {name: "专辑", fx: "ALBUM"},
        {name: "风格", fx: "STYLE"},
        {name: "标签", fx: "TAG"}],
      localCondition: "",
      localParams: "",
      localSortType: "",
      localPageNum: 1,
      localPageSize: 10,
    };
  },
  watch: {
    $route: {
      immediate: true, // 立即执行一次
      async handler(newRoute) {
        // 同步路由参数到本地状态
        this.localCondition = newRoute.params.condition || ''
        this.localParams = newRoute.params.params || 'ALL'
        this.localPageNum = parseInt(newRoute.params.pageNum) || 1
        this.localPageSize = parseInt(newRoute.params.pageSize) || 10

        // 强制更新按钮状态
        this.currentBtn = this.localParams

        // 重新加载数据
        await this.fetchData();
      }
    },
  },
  computed: {
    Icon() {
      return Icon
    },
    ...mapState({
      songs: state => state.music.songsBySearch
    })
  },
  methods: {
    // 初始化
    initSearchParams() {
      this.localCondition = this.condition;
      this.localParams = this.params;
      this.localSortType = this.sortType;
      this.localPageNum = this.pageNum;
      this.localPageSize = this.pageSize;
      this.currentBtn = this.params;
      this.fetchData();
    },
    async handleButtonChange(newValue) {
      this.currentBtn = newValue;
      this.localParams = newValue;
      this.localPageNum = 1;
      await this.fetchData()
    },
    async handlePageChange(newPage) {
      this.localPageNum = newPage;
      await this.fetchData()
    },
    async handlePageSizeChange(newSize) {
      this.localPageSize = newSize;
      this.localPageNum = 1;
      await this.fetchData()
    },
    async fetchData() {
      try {
        const params = {
          condition: this.localCondition,
          params: this.localParams,
          sortType: this.localSortType,
          pageNum: this.localPageNum,
          pageSize: this.localPageSize
        }
        await this.getSongsByCondition(params)
      } catch (error) {
        console.error('搜索失败:', error)
      }
    },
    handleAdd() {

    }
  },
  mounted() {
    this.initSearchParams();
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
  height: 95%;
  margin-top: 1%;
  font-size: var(--fontSize);
  overflow: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.exist-bottom {
  width: 100%;
  height: 95%;
  flex-grow: 1;
  flex-direction: column;
}

.list {
  width: 95%;
  height: 95%;
}

.page {
  width: 100%;
  height: 5%;
}

</style>