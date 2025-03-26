package com.sample.music.mapper;

import com.sample.music.pojo.entity.Album;
import com.sample.music.pojo.vo.view.AlbumView;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AlbumMapper {
    void insertAlbumByOne(Album album);

    void deleteAlbumById(Long id);

    void updateAlbumById(Album album);

    void listenersPlusOne(Long id);

    AlbumView selectAlbumById(Long albumId);

    List<Album> selectAlbumByAll();

    Album selectAlbumByTitle(String title);

    List<AlbumView> PagedQuery();

    List<AlbumView> conditionAndPagedQuery(String condition);

    void favoritesPlus(Long id);

    void favoriteSubtract(Long id);
}
