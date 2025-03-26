import service from "@/api/request";

const SongService = {
    // 上传歌曲（管理员）
    insertSong: function (song) {
        return service({
            method: "POST",
            url: "/api/song/insert",
            data: song,
        }).then(response => {
            return response;
        }).catch(error => {
            throw error;
        })
    },
    // 分页查询歌曲
    conditionAndPaged: function (condition, param, sortType, pageNum, pageSize) {
        // 创建一个URLSearchParams对象
        const params = new URLSearchParams();

        // 检查condition是否存在，如果存在则添加到查询参数中
        if (condition) {
            params.append('condition', condition);
        }

        // 检查condition是否存在，如果存在则添加到查询参数中
        if (param) {
            params.append('params', param);
        }

        // 检查sortType是否存在，如果存在则添加到查询参数中
        if (sortType) {
            params.append('sortType', sortType);
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
        const url = `/api/song/select/conditionAndPaged?${params}`;
        return service({
            method: "GET",
            url: url,
        }).then(response => {
            return response.data;
        }).catch(error => {
            throw error;
        })
    },
    // 收听人数+1
    listenersPlusOne: function (songId) {
        return service({
            method: "PUT",
            url: `/api/song/listenersPlusOne/${songId}`,
        }).then(response => {
            return response;
        }).catch(error => {
            throw error;
        })
    },
    // 分页查询歌曲评论
    queryCommentPaged: function (targetId, pageNum, pageSize) {
        // 创建一个URLSearchParams对象
        const params = new URLSearchParams();

        // 检查condition是否存在，如果存在则添加到查询参数中
        if (targetId) {
            params.append('targetId', targetId);
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
        const url = `/api/song/query/comment?${params}`;
        return service({
            method: "GET",
            url: url,
        }).then(response => {
            return response.data;
        }).catch(error => {
            throw error;
        })
    },
    // 评论歌曲
    insertSongComment: function (c, targetId) {
        return service({
            method: "POST",
            url: `/api/song/insert/comment?content=${c}&targetId=${targetId}`,
        }).then(response => {
            return response;
        }).catch(error => {
            throw error;
        })
    },
}
export default SongService;