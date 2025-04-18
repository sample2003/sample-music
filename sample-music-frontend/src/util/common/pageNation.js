export default {
  data() {
    return {
      pageNum: 1,
      pageSize: 10,
    }
  },
  methods: {
    handlePageSizeChange(newSize) {
      this.$router.push({
        name: 'search',
        params: this.getRouteParams({pageSize: newSize, pageNum: 1})
      }).then(r => {})
     },
    handlePageChange(newPage) {
      this.$router.push({
        name: 'search',
        params: this.getRouteParams({ pageNum: newPage })
      }).then(r => {})
    },
  },
}