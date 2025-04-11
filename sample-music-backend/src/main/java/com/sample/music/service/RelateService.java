package com.sample.music.service;

import com.sample.music.mapper.PlaylistMapper;
import com.sample.music.mapper.SongMapper;
import com.sample.music.pojo.entity.Song;
import com.sample.music.mapper.RelateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RelateService {

    private final RelateMapper relateMapper;

    private final SongMapper songMapper;

    private final PlaylistMapper playlistMapper;

    public void addSongsToPlaylist(Long playlistId, Long id) {
        relateMapper.addSongsToPlaylist(playlistId, id);
    }

    public void deleteSongsByPlaylist(Long playlistId, Long id) {
        relateMapper.deleteSongsByPlaylist(playlistId, id);
    }

    public List<Song> selectSongsAllByPlaylist(Long id) {
        return relateMapper.selectSongsAllByPlaylist(id);
    }

    public List<Song> selectSongsAllByAlbum(Long id) {
        return relateMapper.selectSongsAllByAlbum(id);
    }
}
