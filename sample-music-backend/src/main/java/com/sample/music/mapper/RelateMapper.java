package com.sample.music.mapper;

import com.sample.music.pojo.entity.Song;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RelateMapper {
    void addSongsToPlaylist(Long playlistId, Long id);

    void deleteSongsByPlaylist(Long playlistId, Long id);

    List<Song> selectSongsAllByPlaylist(Long id);

    List<Song> selectSongsAllByAlbum(Long id);

}
