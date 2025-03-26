package com.sample.music.service;

import com.sample.music.exception.BusinessException;
import com.sample.music.mapper.PlaylistMapper;
import com.sample.music.mapper.SongMapper;
import com.sample.music.pojo.dto.FavoriteItems;
import com.sample.music.pojo.entity.Favorites;
import com.sample.music.pojo.entity.Song;
import com.sample.music.mapper.RelateMapper;
import com.sample.music.pojo.vo.favorite.FavoriteSong;
import com.sample.music.utils.ThreadLocalUtil;
import com.sample.music.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Map;

import static com.sample.music.constant.HttpStatusCode.CONFLICT;

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
