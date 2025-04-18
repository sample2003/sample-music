<template>
  <div id="Search">
    <div class="exist">
      <div class="title flex">
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
      <div style="width: 100%; height: 90%;" v-if="songsBySearch.items.length > 0">
        <div class="list">
          <SongList
              @add="handleAdd"
              :songs="songsBySearch"
              :operator="'album'" />
        </div>
        <PageNation
            class="page"
            :totalItems="songsBySearch.total || 0"
            :pageSize.sync="pageSize"
            :currentPage.sync="pageNum"
            @update:pageSize="handlePageSizeChange"
            @update:currentPage="handlePageChange"
        />
      </div>
      <h2 v-else>未搜索出歌曲</h2>
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
  components: {
    ButtonSelect,
    SongList,
    PageNation
  },
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
      param: {},
    };
  },
  watch: {
    // 正确监听路由参数变化
    $route: {
      immediate: true,
      async handler(to) {
        if (!this.needReload(to)) return
        await this.fetchData()
      }
    },
    params() {
      this.fetchData()
    },
    pageNum() {
      this.fetchData()
    },
    pageSize() {
      this.fetchData()
    },
    currentBtn() {
      this.fetchData()
    }
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
    // 判断参数是否改变
    needReload(to) {
      return to.params.condition !== this.condition ||
          to.params.params !== this.params ||
          to.params.sortType !== this.sortType ||
          to.params.pageNum !== this.pageNum ||
          to.params.pageSize !== this.pageSize
    },
    handlePageChange(newPage) {
      this.$router.push({
        name: 'search',
        params: this.getRouteParams({ pageNum: newPage })
      })
    },
    handleButtonChange(newValue) {
      alert(newValue)
      this.currentBtn = newValue
      this.$router.push({
        name: 'search',
        params: this.getRouteParams({ params: newValue, pageNum: 1 })
      })
    },
    handlePageSizeChange(newSize) {
      this.$router.push({
        name: 'search',
        params: this.getRouteParams({ pageSize: newSize, pageNum: 1 })
      })
    },
    getRouteParams(overrides = {}) {
      return {
        condition: this.condition,
        params: this.params,
        sortType: this.sortType,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        ...overrides
      }
    },
    async fetchData() {
      try {
        const params = {
          condition: this.condition,
          params: this.params,
          sortType: this.sortType,
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
        await this.getSongsByCondition(params)
      } catch (error) {
        console.error('搜索失败:', error)
        // 处理错误状态
      }
    },
    handleAdd() {

    }
  },
  mounted() {
    this.currentBtn = this.params
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
  height: 95%;
  font-size: var(--fontSize);
  overflow: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.title {
  height: 10%;
}

.list {
  width: 95%;
  height: 90%;
}

.page {
  width: 100%;
  height: 5%;
}

</style>