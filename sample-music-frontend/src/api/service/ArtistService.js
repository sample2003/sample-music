import service from "@/api/request";

const ArtistService = {
    insertArtist: function (artist) {
        return service({
            method: 'POST',
            url: '/api/artist/insert',
            data: artist
        }).then(response => {
            return response;
        }).catch(error => {
            return error;
        })
    },
    selectArtistById: function (artistId) {
        return service({
            url: `/api/artist/select/${artistId}`,
            method: "GET"
        }).then(response => {
            return response.data;
        }).catch(error => {
            throw error;
        })
    },
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
        const url = `/api/artist/query/conditionAndPaged?${params}`;
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
export default ArtistService;