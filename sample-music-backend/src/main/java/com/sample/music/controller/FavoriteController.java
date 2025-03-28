package com.sample.music.controller;

import com.sample.music.common.Result;
import com.sample.music.pojo.entity.Favorites;
import com.sample.music.pojo.vo.favorite.*;
import com.sample.music.service.FavoriteService;
import com.sample.music.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorite")
public class FavoriteController {
    private final FavoriteService favoriteService;


    /**
     * 添加到收藏
     *
     * @param targetType 目标类型
     * @param targetId 目标id
     */
    @PostMapping("/add")
    public Result<String> AddToFavorites(@RequestParam String targetType, @RequestParam Long targetId) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer id = (Integer) claims.get("id");
        Long userId = Long.valueOf(id);
        Favorites favorites = new Favorites();
        // targetType是个常量，只能在song, playlist, album, work, publicity, user, comment里选
        favorites.setTargetType(targetType);
        favorites.setTargetId(targetId);
        favorites.setUserId(userId);
        favoriteService.AddToFavorites(favorites);
        return Result.success();
    }

    /**
     * 移除收藏
     *
     * @param targetType 收藏类型
     * @param targetId 收藏id
     */
    @DeleteMapping("/remove")
    public Result<String> RemoveByFavorites(@RequestParam String targetType, @RequestParam Long targetId) {
        favoriteService.RemoveByFavorites(targetType, targetId);
        return Result.success();
    }

    /**
     * 查看我的歌曲收藏
     *
     */
    @GetMapping("/query/song")
    public Result<List<FavoriteSong>> queryFavoritesSong() {
        List<FavoriteSong> favoriteSong = favoriteService.queryFavoritesSong();
        return Result.success(favoriteSong);
    }

    /**
     * 查看我的歌单收藏
     *
     */
    @GetMapping("/query/playlist")
    public Result<List<FavoritePlaylist>> queryFavoritesPlaylist() {
        List<FavoritePlaylist> favoritePlaylists = favoriteService.queryFavoritePlaylist();
        return Result.success(favoritePlaylists);
    }

    /**
     * 查看我的专辑收藏
     *
     */
    @GetMapping("/query/album")
    public Result<List<FavoriteAlbum>> queryFavoriteAlbum() {
        List<FavoriteAlbum> favoriteAlbums = favoriteService.queryFavoriteAlbum();
        return Result.success(favoriteAlbums);
    }

    /**
     * 查看我关注的用户
     *
     */
    @GetMapping("/query/user")
    public Result<List<FavoriteUser>> queryFavoriteUser() {
        List<FavoriteUser> favoriteUsers = favoriteService.queryFavoriteUser();
        return Result.success(favoriteUsers);
    }


    /**
     * 查看我点赞的评论
     *
     */
    @GetMapping("/query/comment")
    public Result<List<FavoriteComment>> queryFavoriteComment() {
        List<FavoriteComment> favoriteComments = favoriteService.queryFavoriteComment();
        return Result.success(favoriteComments);
    }
}
