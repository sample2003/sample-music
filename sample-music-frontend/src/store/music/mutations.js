import state from "@/store/music/state";

export default {
  setPlaylistDetail(state, payload) {
    state.playlistDetail = payload;
    saveToLocalStorage(state);
  },
  setAlbumDetail(state, payload) {
    state.albumDetail = payload;
    saveToLocalStorage(state);
  },
  setSongsBySearch(state, payload) {
    state.songsBySearch = payload;
    saveToLocalStorage(state);
  },
  setSongsByRecent(state, payload) {
    state.songsByRecent = payload;
    saveToLocalStorage(state);
  },
  setCheckedSongs(state, payload) {
    state.checked.checkedSongs = payload;
    saveToLocalStorage(state);
  },
  setCheckedPlaylist(state, payload) {
    state.checked.checkedPlaylist = payload;
    saveToLocalStorage(state);
  },
  setIsCheckedAll(state, payload) {
    state.checked.isCheckedAll = payload;
    saveToLocalStorage(state);
  },
  setSongPlaying(state, payload) {
    state.play.songPlaying = payload;
    saveToLocalStorage(state);
  },
  setPlaylistPlaying(state, payload) {
    state.play.playlistPlaying = payload;
    saveToLocalStorage(state);
  },
  setSongIndex(state, payload) {
    state.play.songIndex = payload;
    saveToLocalStorage(state);
  },
  setPlayMode(state, payload) {
    state.play.playMode = payload;
    saveToLocalStorage(state);
  },
  setSongMode(state, payload) {
    state.play.songMode = payload;
  },
  setAudio(state, payload) {
    state.play.audio = payload;
    saveToLocalStorage(state);
  },
  setIsPlay(state, payload) {
    state.play.isPlay = payload;
    saveToLocalStorage(state);
  },
  setIsLoading(state, payload) {
    state.play.isLoading = payload;
    saveToLocalStorage(state);
  },
  setCurrentTime(state, payload) {
    state.play.currentTime = payload;
    saveToLocalStorage(state);
  },
  setSongDuration(state, payload) {
    state.play.songDuration = payload;
    saveToLocalStorage(state);
  },
  setCurrentLyric(state, payload) {
    state.play.currentLyric = payload;
    saveToLocalStorage(state);
  },
  setSongLyric(state, payload) {
    state.play.songLyric = payload;
    saveToLocalStorage(state);
  },
  setTimestamp(state, payload) {
    state.play.Timestamp = payload;
    saveToLocalStorage(state);
  },
}

// 辅助函数，用于将 state 保存到 localStorage
function saveToLocalStorage() {
  const musicState = {
    playlistDetail: state.playlistDetail,
    albumDetail: state.albumDetail,
    songsBySearch: state.songsBySearch,
    checked: state.checked,
    play: state.play,
  };
  window.localStorage.setItem('sampleMusic', JSON.stringify(musicState));
}