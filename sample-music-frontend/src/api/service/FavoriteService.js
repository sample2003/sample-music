import service from "@/api/request";

const FavoriteService = {
    addToFavorites: function (targetType, targetId) {
        return service({
            method: 'POST',
            url: `api/favorite/add?targetType=${targetType}&targetId=${targetId}`,
        }).then(response => {
            return response.code === 200;
        }).catch(error => {
            throw error;
        })
    },
    removeByFavorites: function (targetType, targetId) {
        return service({
            method: 'DELETE',
            url: `api/favorite/remove?targetType=${targetType}&targetId=${targetId}`,
        }).then(response => {
            return response.code === 200;
        }).catch(error => {
            throw error;
        })
    },
    queryFavoriteAlbum: function () {
        return service({
            method: "GET",
            url: "api/favorite/query/album",
        }).then(response => {
            return response.data;
        }).catch(error => {
            throw error;
        })
    },
    queryFavoriteComment: function () {
        return service({
            method: "GET",
            url: "api/favorite/query/comment",
        }).then(response => {
            return response.data;
        }).catch(error => {
            throw error;
        })
    },
    queryFavoritePlaylist: function () {
        return service({
            method: "GET",
            url: "api/favorite/query/playlist",
        }).then(response => {
            return response.data;
        }).catch(error => {
            throw error;
        })
    },
}

export default FavoriteService;