import {mapActions, mapMutations, mapState} from "vuex";

export default {
  computed: {
    ...mapState({
      playlistDetail: state => state.music.playlistDetail,
      albumDetail: state => state.music.albumDetail,
      songsBySearch: state => state.music.songsBySearch,
      songsByRecent: state => state.music.songsByRecent,
      songPlaying: state => state.music.play.songPlaying,
      playlistPlaying: state => state.music.play.playlistPlaying,
      songIndex: state => state.music.play.songIndex,
      playMode: state => state.music.play.playMode,
      songMode: state => state.music.play.songMode,
      audio: state => state.music.play.audio,
      isPlay: state => state.music.play.isPlay,
      isLoading: state => state.music.play.isLoading,
      currentTime: state => state.music.play.currentTime,
      songDuration: state => state.music.play.songDuration,
      currentLyric: state => state.music.play.currentLyric,
      songLyric: state => state.music.play.songLyric,
      Timestamp: state => state.music.play.Timestamp,
    }),
  }, methods: {
    ...mapActions({
      // getPlaylists: 'music/getPlaylists',
      getSongsByCondition: 'music/getSongsByCondition',
    }), ...mapMutations({
      setPlaylistDetail: 'music/setPlaylistDetail',
      setAlbumDetail: 'music/setAlbumDetail',
      setSongsBySearch: 'music/setSongsBySearch',
      setSongsByRecent: 'music/setSongsByRecent',
      setSongPlaying: 'music/setSongPlaying',
      setPlaylistPlaying: 'music/setPlaylistPlaying',
      setSongIndex: 'music/setSongIndex',
      setPlayMode: 'music/setPlayMode',
      setSongMode: 'music/setSongMode',
      setAudio: 'music/setAudio',
      setIsPlay: 'music/setIsPlay',
      setIsLoading: 'music/setIsLoading',
      setCurrentTime: 'music/setCurrentTime',
      setSongDuration: 'music/setSongDuration',
      setCurrentLyric: 'music/setCurrentLyric',
      setSongLyric: 'music/setSongLyric',
      setTimestamp: 'music/setTimestamp',
    }),
  }
}