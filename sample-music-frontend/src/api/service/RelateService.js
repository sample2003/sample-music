import service from "@/api/request";

const RelateService = {
    // 将歌曲添加进歌单
    addSongsToPlaylist: function (playlistId, ids) {
        return service({
            method: 'POST',
            url: '/api/relate/addSongsToPlaylist?playlistId=' + playlistId,
            data: ids,
        }).then(response => {
            return response.data;
        }).catch(error => {
            throw error;
        })
    },
    // 将歌曲移除歌单
    deleteSongsByPlaylist: function (playlistId, ids) {
        return service({
            method: 'DELETE',
            url: '/api/relate/deleteSongsByPlaylist?playlistId=' + playlistId,
            data: ids,
        }).then(response => {
            return response.data;
        }).catch(error => {
            throw error;
        })
    }

}
export default RelateService;