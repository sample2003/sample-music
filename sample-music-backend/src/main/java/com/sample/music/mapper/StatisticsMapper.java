package com.sample.music.mapper;

import com.sample.music.pojo.dto.ListenDTO;
import com.sample.music.pojo.entity.Song;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StatisticsMapper {

    void recordListen(ListenDTO listenDTO);

    List<Song> getTopSongs();
}
