<template>
  <div id="DataManagement">
    <div class="top">
      <TextInput message="搜索"></TextInput>
    </div>
    <!-- 数据表格 -->
    <div class="table-container">
      <table>
        <thead>
        <tr>
          <th>操作</th>
          <th v-for="cc in currentColumns" :key="cc.key">
            {{ cc.title }}
          </th>
        </tr>
        </thead>

        <tbody>
        <tr v-for="cd in currentData" :key="`cd-${cd.id}`">
          <td>
            <button class="handleButton" @click="handleEdit(cd)">编辑</button>
            <button class="handleButton" @click="handleDelete(cd.id)">删除</button>
          </td>
          <td v-for="cc in currentColumns" :key="cc.key">
            <!-- 图片字段处理 -->
            <img
                v-if="isImageField(cc.key) && cd[cc.key]"
                :src="cd[cc.key]"
                class="thumbnail"
                @click="yulan(cd[cc.key])"
                @error="handleImageError"
             alt="图片">
            <!-- 普通字段显示 -->
            <p v-else
               @click.stop="copyToClipboard(formatValue(cd[cc.key], cc.key))"
               :title="'点击复制 ' + formatValue(cd[cc.key], cc.key)"
               class="copyable"
            >
              {{ formatValue(cd[cc.key], cc.key) }}
            </p>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <div class="foot">
      <page-nation :total-items="total"></page-nation>
    </div>
  </div>
</template>

<script>
import TextInput from "@/pages/common/TextInput.vue";
import PageNation from "@/pages/common/PageNation.vue";
import SongService from "@/api/service/SongService";
import PlaylistService from "@/api/service/PlaylistService";
import FIELD_TRANSLATIONS from "@/api/enum";

export default {
  name: "DataManagement",
  components: {PageNation, TextInput},
  props: {
    param: {type: String, required: true},
  },
  data() {
    return {
      total: 0,
      currentData: [],
    }
  },
  computed: {
    currentColumns() {
      if (this.currentData.length === 0) return []
      return Object.keys(this.currentData[0])
          .filter(key => key !== 'id') // 过滤不需要的字段
          .map(key => ({
            key: key,
            title: FIELD_TRANSLATIONS[this.param]?.[key] || key
          }));
    }
  },
  methods: {
    // 复制到剪贴板
    async copyToClipboard(text) {
      try {
        await navigator.clipboard.writeText(text)
        this.$message('复制成功')
      } catch (err) {
        // 降级方案
        const textarea = document.createElement('textarea')
        textarea.value = text
        document.body.appendChild(textarea)
        textarea.select()
        try {
          document.execCommand('copy')
          this.$message('复制成功')
        } catch (err) {
          this.$message('复制失败，请手动复制')
        }
        document.body.removeChild(textarea)
      }
    },
    async yulan(text) {
      this.$message(text)
    },
    async fetchData() {
      if (this.param === "song") await this.songData()
      else if (this.param === "playlist") await this.playlistData()
    },
    async songData() {
      const data = await SongService.conditionAndPaged();
      this.currentData = data.items;
      this.total = data.total;
      console.log(this.currentData)
    },
    async playlistData() {
      const data = await PlaylistService.selectAll();
      this.currentData = data;
      this.total = data;
      console.log(this.currentData)
    },
    // 判断是否为图片字段
    isImageField(key) {
      return  key.endsWith('avatar') || key.endsWith('Avatar') || key.endsWith('Cover') || key.endsWith('cover');
    },
    handleImageError(e) {
      e.target.style.display = 'none'; // 隐藏损坏图片
    },
    formatValue(value) {
      if (Array.isArray(value)) {
        return value.join(', ');
      }
      return value || '-';
    },
    handleEdit(item) {
      // this.$temp("编辑歌曲", item)
      console.log(item)
    },
    handleDelete(id) {
      console.log(id)
    },
  },
  mounted() {
    this.fetchData();
  }
};
</script>

<style scoped>
#DataManagement {
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.top {
  height: 5%;
}

.top:nth-child(1) {
  width: 10%;
}

.table-container {
  height: 89%;
  overflow: scroll;
}

.table-container::-webkit-scrollbar {
  width: 10px !important;
  height: 10px !important;
  transition: 0.2s all ease;
}

.table-container::-webkit-scrollbar-track {
  background: var(--second-color);
  transition: 0.2s all ease;
}

.table-container::-webkit-scrollbar-thumb {
  background: var(--fourth-color);
  border-radius: 5px;
}

.table-container::-webkit-scrollbar-thumb:hover {
  background: var(--main-color);
}

table {
  width: 100%;
  border-collapse: collapse;
}

tr {
  transition: 0.2s all ease;
}

tr:hover {
  background-color: var(--fourth-color);
}

th, td {
  max-width: 100px;
  padding: 12px 15px;
  border: 1px solid #e0e0e0;
  text-align: left;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

th {
  background-color: var(--fourth-color);
  font-weight: 600;
}

td:hover {
  border-radius: 5px;
  background-color: var(--main-color);
}

.thumbnail {
  max-width: 80px;
}

img {
  cursor: pointer;
}

p {
  cursor: default;
  text-align: left;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.handleButton {
  width: 40%;
  cursor: pointer;
  display: inline-block;
  font-size: 0.8em;
  background: #222;
  color: #fff;
  box-shadow: 0 2px 15px #222;
  border-radius: 5px;
  text-decoration: none;
  border: none;
  font-weight: 700;
  transition: 0.2s;
  padding: 5px 0.1em;
  margin-right: 5px;
}

.handleButton:hover {
  color: #222;
  background: #fff;
  box-shadow: 0 2px 15px #fff;
}

.foot {
  height: 5%;
}
</style>