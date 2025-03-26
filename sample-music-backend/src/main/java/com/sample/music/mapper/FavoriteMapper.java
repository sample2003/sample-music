package com.sample.music.mapper;

import com.sample.music.pojo.entity.Favorites;
import com.sample.music.pojo.vo.favorite.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FavoriteMapper {
    void AddToFavorites(Favorites favorites);

    void RemoveByFavorites(Long userId, String targetType, Long targetId);

    List<FavoriteSong> queryFavoritesSong(String targetType, Long userId);

    List<FavoritePlaylist> queryFavoritePlaylist(String targetType, Long userId);

    List<FavoriteAlbum> queryFavoriteAlbum(String targetType, Long userId);

    List<FavoriteUser> queryFavoriteUser(String targetType, Long userId);

    List<FavoriteComment> queryFavoriteComment(String targetType, Long userId);
}
