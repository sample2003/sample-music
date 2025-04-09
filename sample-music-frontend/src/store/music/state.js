// 从localStorage中获取保存的状态

const savedState = JSON.parse(localStorage.getItem('sampleMusic'));

// 定义默认状态
const defaultState = {
  // 歌单信息
  playlistDetail: {},
  // 专辑信息
  albumDetail: {},
  // 搜索出的歌曲
  songsBySearch: {},
  // 最近播放
  songsByRecent: [],
  // 选中的歌曲或歌单
  checked: {
    // 选中的歌曲
    checkedSongs: [],
    // 选中的歌单
    checkedPlaylist: 0,
    // 是否全选
    isCheckedAll: false,
  },
  // 播放状态
  play: {
    // 正在播放的歌曲
    songPlaying: {
      /*            id: 10,
                  artist: "周杰伦",
                  album: "叶惠美",
                  title: "反方向的钟",
                  duration: 430,
                  mp3Url: "https://sample2003.oss-cn-guangzhou.aliyuncs.com/music/song/%E5%8F%8D%E6%96%B9%E5%90%91%E7%9A%84%E9%92%9F-%E5%91%A8%E6%9D%B0%E4%BC%A6.mp3",
                  flacUrl: "https://sample2003.oss-cn-guangzhou.aliyuncs.com/music/song/%E5%8F%8D%E6%96%B9%E5%90%91%E7%9A%84%E9%92%9F-%E5%91%A8%E6%9D%B0%E4%BC%A6.flac",
                  cover: "https://sample2003.oss-cn-guangzhou.aliyuncs.com/music/image/1jay.jpg",
                  lyric: "https://sample2003.oss-cn-guangzhou.aliyuncs.com/music/lyric/%E5%8F%8D%E6%96%B9%E5%90%91%E7%9A%84%E9%92%9F-%E5%91%A8%E6%9D%B0%E4%BC%A6.txt",
                  style: "流行",
                  tags: "",
                  listeners: 0,
                  releaseDate: "2000-11-7",
                  createTime: "2024-11-31"*/
    },
    // 正在播放的歌单
    playlistPlaying: [{
      id: 10,
      artist: "周杰伦",
      album: "叶惠美",
      title: "反方向的钟",
      duration: 430,
      mp3Url: "https://sample2003.oss-cn-guangzhou.aliyuncs.com/music/song/%E5%8F%8D%E6%96%B9%E5%90%91%E7%9A%84%E9%92%9F-%E5%91%A8%E6%9D%B0%E4%BC%A6.mp3",
      flacUrl: "https://sample2003.oss-cn-guangzhou.aliyuncs.com/music/song/%E5%8F%8D%E6%96%B9%E5%90%91%E7%9A%84%E9%92%9F-%E5%91%A8%E6%9D%B0%E4%BC%A6.flac",
      cover: "https://sample2003.oss-cn-guangzhou.aliyuncs.com/music/image/1jay.jpg",
      lyric: "https://sample2003.oss-cn-guangzhou.aliyuncs.com/music/lyric/%E5%8F%8D%E6%96%B9%E5%90%91%E7%9A%84%E9%92%9F-%E5%91%A8%E6%9D%B0%E4%BC%A6.txt",
      style: "流行",
      tags: "",
      listeners: 0,
      releaseDate: "2000-11-7",
      createTime: "2024-11-31"
    }],
    // 歌曲在歌单的索引
    songIndex: 0,
    // 播放模式
    playMode: 'loop',
    // 歌曲格式
    songMode: 'flac',
    // 音频
    audio: null,
    // 是否正在播放
    isPlay: false,
    // 是否正在加载
    isLoading: false,
    // 当前播放时间
    currentTime: 0,
    // 歌曲时长
    duration: 0,
    // 当前播放歌词
    currentLyric: '',
    // 歌曲歌词
    songLyric: [],
    // 时间戳
    Timestamp: [],
  },
  // 搜索关键词
  condition: ''
};

// 如果savedState存在，并且包含属性，则使用savedState中的值，否则使用defaultState中的值
const state = {
  playlistDetail: savedState?.playlistDetail || defaultState.playlistDetail,
  albumDetail: savedState?.albumDetail || defaultState.albumDetail,
  songsBySearch: savedState?.songsBySearch || defaultState.songsBySearch,
  songsByRecent: savedState?.songsBySearch || defaultState.songsByRecent,
  checked: {
    ...defaultState.checked,
    ...(savedState?.checked || {})
  },
  play: {
    ...defaultState.play,
    ...(savedState?.play || {})
  },
};

// 如果state中有任何数据，则将其保存到localStorage
for (const key in state) {
  if (state[key] !== defaultState[key]) {
    localStorage.setItem('sampleMusic', JSON.stringify(state));
    break; // 只需要保存一次即可
  }
}

export default state;