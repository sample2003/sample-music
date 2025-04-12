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
  conditionAndPaged: function (params) {
    const urlSearchParams = new URLSearchParams();
    if(params) {
      if (params.condition) {
        urlSearchParams.append('condition', params.condition);
      }
      if (params.params) {
        urlSearchParams.append('params', params.params);
      }
      if (params.sortType) {
        urlSearchParams.append('sortType', params.sortType);
      }
      if (params.pageNum) {
        urlSearchParams.append('pageNum', params.pageNum);
      }
      if (params.pageSize) {
        urlSearchParams.append('pageSize', params.pageSize);
      }
    }
    const url = `/api/song/select/conditionAndPaged?${urlSearchParams}`;
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