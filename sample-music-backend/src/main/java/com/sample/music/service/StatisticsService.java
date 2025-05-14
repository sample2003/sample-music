package com.sample.music.service;

import com.sample.music.mapper.StatisticsMapper;
import com.sample.music.pojo.dto.ListenDTO;
import com.sample.music.pojo.entity.Song;
import com.sample.music.pojo.vo.view.SongView;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticsMapper statisticsMapper;

    public void recordListen(ListenDTO listenDTO) {
        statisticsMapper.recordListen(listenDTO);
    }

    public List<SongView> getTopSongs() {
        List<Song> songs = statisticsMapper.getTopSongs();
        List<SongView> songViews = new ArrayList<>();
        BeanUtils.copyProperties(songs, songViews);
        return songViews;
    }
}
