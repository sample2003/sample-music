import service from "@/api/request";

const AlbumService = {
    // 上传专辑
    insertAlbum: function (album) {
        return service({
            method: 'POST',
            url: '/api/album/insert',
            data: album
        }).then(response => {
            return response;
        }).catch(error => {
            return error;
        })
    },
    // 分页查询
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
        const url = `/api/album/query/conditionAndPaged?${params}`;
        return service({
            method: "GET",
            url: url,
        }).then(response => {
            return response;
        }).catch(error => {
            throw error;
        })
    },
    // 收听人数+1
    listenersPlusOne: function (songId) {
        return service({
            method: "PUT",
            url: `/api/album/listenersPlusOne/${songId}`,
        }).then(response => {
            return response;
        }).catch(error => {
            throw error;
        })
    },
    // 查询专辑与相关歌曲
    albumWithSongs: function (id) {
        return service({
            method: "GET",
            url: `/api/album/select/albumWithSongs/${id}`
        }).then(response => {
            return response.data;
        }).catch(error => {
            throw error;
        })


    },
    // 查询评论
    queryCommentPaged: function (targetId) {
        return service({
            method: "GET",
            url: `/api/album/query/comment?targetId=${targetId}`,
        }).then(response => {
            console.log('准备发送请求，内容:', response); // 添加请求
            return response.data;
        }).catch(error => {
            throw error;
        })
    },
    // 评论专辑
    insertAlbumComment: function (content, targetId) {
        return service({
            method: "POST",
            url: `/api/album/insert/comment?content=${content}&targetId=${targetId}`,
        }).then(response => {
          console.log('内容:', response); // 添加请求
            return response;
        }).catch(error => {
            throw error;
        })
    },
}
export default AlbumService;