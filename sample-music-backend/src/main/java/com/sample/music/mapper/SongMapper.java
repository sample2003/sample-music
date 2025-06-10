package com.sample.music.mapper;

import com.sample.music.pojo.entity.Song;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SongMapper {

    void insertSong(Song song);

    void deleteSongById(Long songId);

    void updateSong(Song song);

    void listenersPlusOne(Long id);

    Song querySongById(Long songId);

    Song querySongByArtistName(String artistName);

    int countAll();

    int countByCondition(String condition, String params);

    List<Song> conditionAndPagedQuery(String condition, String params);

    List<Song> PagedQuery();

    List<Song> queryAllByIds(List<Long> ids);
}
