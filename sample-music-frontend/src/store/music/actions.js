import axios from "axios";
import state from "@/store/music/state";
import SongService from "@/api/service/SongService";

export default {
    // 歌曲：查询一首歌曲的数据
    getSongById({commit, state}, song) {
        const songIndex = state.play.playlistPlaying.findIndex(s => s.id === song.id);
        if (songIndex === -1) {
            // 如果歌曲不在播放列表中,则添加到播放列表的末尾
            commit('setPlaylistPlaying', [...state.play.playlistPlaying, song]);
            commit('setSongIndex', state.play.playlistPlaying.length - 1);
        } else {
            // 如果歌曲已在播放列表中,则移动到播放列表的末尾
            const updatedSongList = [
                ...state.play.playlistPlaying.slice(0, songIndex),
                ...state.play.playlistPlaying.slice(songIndex + 1),
                song
            ];
            commit('setPlaylistPlaying', updatedSongList);
            commit('setSongIndex', updatedSongList.length - 1);
        }
        commit('setSongPlaying', song);

    },
    // 歌曲：查询一个歌单中全部歌曲的数据
    getSongsByPlaylist({commit}, id) {
        const requestConfig = {
            method: 'get',
            url: `http://localhost:8080/api/relate/selectSongsByPlayList/${id}`,
            headers: {
                // 假设token存储在localStorage中
                'Authorization': 'Bearer ' + (localStorage.getItem('token') || '')
            }
        };
        axios(requestConfig)
            .then(response => {
                if (response.data) {
                    const songs = response.data.data
                    if (Array.isArray(songs) && songs.length > 0) {
                        const sortedSongs = songs.sort((a, b) => {
                            if (a.id < b.id) return -1;
                            if (a.id > b.id) return 1;
                            return 0;
                        });
                        commit('setSongsInPlaylist', sortedSongs);
                    } else {
                        // 如果 songs 为 null 或者不是数组,可以在这里设置一个默认值
                        commit('setSongsInPlaylist', []);
                    }
                }
            })
            .catch(error => {
                console.error("Error get songs by id: ", error);
            })
    },
    // 歌曲：查询一个专辑里全部歌曲的数据
    // 歌曲：条件查询并默认查询10条数据
    async getSongsByCondition({commit}, params) {
      console.log(params)
      const songs = await SongService.conditionAndPaged(params.condition, params.params, params.sortType, params.pageNum, params.pageSize);
      console.log(songs)
      commit('setSongsBySearch', songs);
    },
    // 歌曲：往歌单中增加一首或多首歌曲
    addSongsToPlaylist(data) {
        axios({
            url: "http://localhost:8080/api/relate/addSongsToPlayList?playlist_id=" + data.id, // 后端的URL路径
            method: 'post',
            data: data.ids, // 传递数组作为请求体
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token'),
                'Content-Type': 'application/json' // 指定内容类型为JSON
            }
        })
    },
    // 歌曲：从歌单中删除一首或多首歌曲
    delSongsByPlaylist(data) {
        axios({
            url: "http://localhost:8088/api/relate/deleteSongsByPlayList?playlist_id=" + data.id, // 后端的URL路径
            method: 'delete',
            data: data.ids, // 传递数组作为请求体
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token'),
                'Content-Type': 'application/json' // 指定内容类型为JSON
            }
        })
    },
    // 歌曲：更新播放歌曲
    updPlayingSong({commit}, index) {
        commit('setSongIndex', index)
        commit('setSongPlaying', state.play.playlistPlaying[index])
    },
    // 歌曲（管理员）：往数据库添加数据


    // 歌单：查询全部歌单的数据
    /*async getPlaylists({commit}) {
        const userPlaylists = await PlaylistService.conditionAndPaged();
        commit('setPlaylists', userPlaylists)
    },*/
    // 歌单：新增一个歌单
    // eslint-disable-next-line no-unused-vars
    async addPlaylist({commit}, title) {
        try {
            let formData = new FormData();
            formData.append('title', title);
            const response = await axios({
                url: "http://localhost:8080/api/playlist/insert",
                method: 'post',
                data: formData,
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                }
            });
            // 根据响应判断操作是否成功
            return response.data.code === 200;
        } catch (error) {
            // 处理错误情况
            console.error("添加歌单失败：", error);
            return false;
        }
    },
    // 歌单：删除一个歌单
    delPlaylist(id) {
        axios({
            url: `http://localhost:8080/api/playlist/delete/${id}`, // 后端的URL路径
            method: 'delete',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token'),
                'Content-Type': 'application/json' // 指定内容类型为JSON
            }
        })
    },
    // 歌单：更新歌单
    updPlaylist(songList) {
        axios({
            url: "http://localhost:8080/api/playlist/update", // 后端的URL路径
            method: 'put',
            data: songList, // 传递数组作为请求体
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token'),
                'Content-Type': 'application/json' // 指定内容类型为JSON
            }
        })
    },

    // 专辑：查询多个专辑

    // 专辑：查询一个专辑的数据
    /*// 专辑：查询全部专辑的数据
    getAlbums({commit}) {
        const requestConfig = {
            method: 'get',
            url: "http://localhost:8080/api/album/select",
            headers: {
                // 假设token存储在localStorage中
                'Authorization': 'Bearer ' + (localStorage.getItem('token') || '')
            }
        };
        axios(requestConfig)
            .then(response => {
                if (response.data) {
                    commit('setAlbums', response.data.data);
                }
            })
            .catch(error => {
                console.error("Error get albums: ", error);
            })
    },*/
}