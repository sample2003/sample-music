package com.sample.music.mapper;

import com.sample.music.pojo.entity.Artist;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArtistMapper {

    void insertArtistByOne(Artist artist);

    void deleteArtistById(Long id);

    void updateArtistById(Artist artist);

    Artist selectArtistById(Long id);

    List<Artist> selectArtistByAll();

    Artist selectArtistByName(String name);

    List<Artist> PagedQuery();

    List<Artist> conditionAndPagedQuery(String condition);
}
