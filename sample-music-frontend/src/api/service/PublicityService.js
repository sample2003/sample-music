import service from "@/api/request";

const PublicityService = {
  insertPublicity: function (publicity) {
    return service({
      method: 'POST',
      url: '/api/publicity/insert',
      data: publicity
    }).then(response => {
      return response;
    }).catch(error => {
      return error;
    })
  },
  conditionAndPaged: function (condition, publicityType, pageNum, pageSize) {
    // 创建一个URLSearchParams对象
    const params = new URLSearchParams();

    // 检查condition是否存在，如果存在则添加到查询参数中
    if (condition) {
      params.append('condition', condition);
    }

    // 检查publicityType是否存在，如果存在则添加到查询参数中
    if (publicityType) {
      params.append('publicityType', publicityType);
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
    const url = `/api/publicity/query/conditionAndPaged?${params}`;
    return service({
      method: "GET",
      url: url,
    }).then(response => {
      return response;
    }).catch(error => {
      throw error;
    })
  },

  query: function (id) {
    // 构建完整的URL
    const url = `/api/publicity/query/${id}`;
    return service({
      method: "GET",
      url: url,
    }).then(response => {
      return response;
    }).catch(error => {
      throw error;
    })
  }
}
export default PublicityService;