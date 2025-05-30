const getters = {
  getSongMode: state => {
    return state.play.songMode;
  },
  getAudio: state => {
    return state.play.audio;
  }
}
export default getters;