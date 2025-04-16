<template>
  <div id="songManagement">
    <div class="top">
      <TextInput></TextInput>
    </div>
    <!-- 数据表格 -->
    <div class="table-container">
      <table>
        <thead>
        <tr>
          <th v-for="cc in currentColumns" :key="cc.key">
            {{cc.key}}
          </th>
          <th>操作</th>
        </tr>
        </thead>

        <tbody>
        <tr v-for="cd in currentData" :key="`cd-${cd.id}`">
          <td v-for="(c, index) in cd" :key="`c-${index}`">
            {{ c }}
          </td>
          <td>
            <button  @click="handleEdit(item)">编辑</button>
            <button @click="handleDelete(item.id)">删除</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <div class="foot">
      <page-nation total-items=""></page-nation>
    </div>
  </div>
</template>

<script>
import TextInput from "@/components/TextInput.vue";
import PageNation from "@/components/PageNation.vue";
import SongService from "@/api/service/SongService";

export default {
  components: {PageNation, TextInput},
  props: {},
  data() {
    return {
      total: 0,
      currentData: [],
    }
  },
  computed: {
    currentColumns() {
      if (this.currentData.length === 0) return []

      // 从第一条数据提取所有字段
      const fields = Object.keys(this.currentData[0])

      return fields.map(key => ({
        key: key,
        title: this.currentData[key]
      }))
    }
  },
  methods: {
    async fetchData() {
      const data = await SongService.conditionAndPaged();
      this.currentData = data.items;
      this.total = data.total;
      console.log(this.currentData)
    },
    handleEdit(item) {
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
#songManagement {
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
  height: 88%;

  overflow-x: auto;
}

::-webkit-scrollbar {
  width: 10px;
  height: 10px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  padding: 12px 15px;
  border: 1px solid #e0e0e0;
  text-align: left;
}

th {
  background-color: #f8f9fa;
  font-weight: 600;
}

tr:hover {
  background-color: #f5f5f5;
}

.foot {
  height: 5%;
}
</style>