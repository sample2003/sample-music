import service from "@/api/request";

const PlaylistService = {
    insertPlaylist: function (playlist) {
        return service({
            method: 'POST',
            url: '/api/playlist/insert',
            data: playlist
        }).then(response => {
            return response;
        }).catch(error => {
            return error;
        })
    },
    deletePlaylist: function (playlistId) {
        return service({
            method: 'DELETE',
            url: '/api/playlist/delete/' + playlistId,
        }).then(response => {
            return response;
        }).catch(error => {
            return error;
        })
    },
    updatePlaylist: function (playlist) {
        return service({
            method: 'PUT',
            url: '/api/playlist/update',
            data: playlist
        }).then(response => {
            return response;
        }).catch(error => {
            return error;
        })
    },
    // 收听人数+1
    listenersPlusOne: function (songId) {
        return service({
            method: "PUT",
            url: `/api/playlist/listenersPlusOne/${songId}`,
        }).then(response => {
            return response;
        }).catch(error => {
            throw error;
        })
    },
    // 条件查询并分页
    conditionAndPaged: function (condition, pageNum, pageSize) {
        // 创建一个URLSearchParams对象
        const params = new URLSearchParams();

        // 检查condition是否存在，如果存在则添加到查询参数中
        if (condition) {
            params.append('condition', condition);
        }

        // 检查pageNum是否存在，如果存在则添加到查询参数中
        if (pageNum) {
            params.append('pageNum', pageNum);
        }

        // 检查pageSize是否存在，如果存在则添加到查询参数中
        if (pageSize) {
            params.append('pageSize', pageSize);
        }

        // 构建完整的URL
        const url = `/api/playlist/query/conditionAndPaged?${params}`;
        return service({
            method: "GET",
            url: url,
        }).then(response => {
            return response;
        }).catch(error => {
            throw error;
        })
    },
    // 查询歌单及相关歌曲
    playlistWithSongs: function (id) {
        return service({
            method: "GET",
            url: `/api/playlist/select/${id}`
        }).then(response => {
            return response.data;
        }).catch(error => {
            throw error;
        })
    },
    // 查询歌单及相关歌曲
    selectPlaylists: function () {
        return service({
            method: "GET",
            url: `/api/playlist/select`
        }).then(response => {
            return response.data;
        }).catch(error => {
            throw error;
        })
    },
    // 查询歌单评论
    queryCommentPaged: function (targetId) {
        return service({
            method: "GET",
            url: `/api/playlist/query/comment?targetId=${targetId}`,
        }).then(response => {
            return response.data;
        }).catch(error => {
            throw error;
        })
    },
    UserPagedQuery: function (isPublic, condition, pageNum, pageSize) {
        // 创建一个URLSearchParams对象
        const params = new URLSearchParams();

        // 检查isPublic是否存在，如果存在则添加到查询参数中
        if (isPublic) {
            params.append('isPublic', isPublic);
        }

        // 检查condition是否存在，如果存在则添加到查询参数中
        if (condition) {
            params.append('condition', condition);
        }

        // 检查pageNum是否存在，如果存在则添加到查询参数中
        if (pageNum) {
            params.append('pageNum', pageNum);
        }

        // 检查pageSize是否存在，如果存在则添加到查询参数中
        if (pageSize) {
            params.append('pageSize', pageSize);
        }

        return service({
            url: `/api/playlist/query/UserPagedQuery?${params}`,
            method: "GET"
        }).then(response => {
            return response;
        }).catch(error => {
            throw error;
        })
    },
    // 评论歌单
    insertPlaylistComment: function (content, targetId) {
        return service({
            method: "POST",
            url: `/api/playlist/insert/comment?content=${content}&targetId=${targetId}`,
        }).then(response => {
            return response;
        }).catch(error => {
            throw error;
        })
    },
}
export default PlaylistService;