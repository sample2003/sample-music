export default  {
  methods: {
    format(songs, size, num) {
      return {
        items: songs.slice(size * (num-1), size * num),
        total: songs.length
      };
    }
  }
}