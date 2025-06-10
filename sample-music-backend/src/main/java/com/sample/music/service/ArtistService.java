package com.sample.music.service;

import com.github.pagehelper.PageHelper;
import com.sample.music.constant.TargetType;
import com.sample.music.mapper.ArtistMapper;
import com.sample.music.mapper.SongMapper;
import com.sample.music.pojo.dto.PageBean;
import com.sample.music.pojo.entity.Artist;
import com.sample.music.pojo.entity.Song;
import com.sample.music.pojo.vo.view.SongView;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistService {
    private final ArtistMapper artistMapper;
    private final SongMapper songMapper;
    private final FileManageService fileManageService;

    public void insertArtistByOne(Artist artist) {
        if(artist.getImageFiles() != null) {
            String avatarUrl = fileManageService.uploadFile(artist.getImageFiles(), "cover");
            artist.setAvatar(avatarUrl);
        }else {
            artist.setAvatar(null);
        }
        artist.setFollows(0);
        artistMapper.insertArtistByOne(artist);
    }

    public void deleteArtistById(Long id) {
        artistMapper.deleteArtistById(id);
    }

    public void updateArtistById(Artist artist) {
        artistMapper.updateArtistById(artist);
    }

    public Artist selectArtistById(Long id) {
        Artist artist = artistMapper.selectArtistById(id);
        if (StringUtil.isNullOrEmpty(artist.getAvatar()))  {
            List<Song> song = songMapper.conditionAndPagedQuery(artist.getName(), "artist");
            artist.setAvatar(song.get(0).getCover());
        }
        return artist;
    }

    public List<Artist> selectArtistByAll() {
        return artistMapper.selectArtistByAll();
    }

    public boolean isExist(Long id) {
        Artist artist = selectArtistById(id);
        return artist != null;
    }

    public Artist selectArtistByName(String name) {
        Artist artist = artistMapper.selectArtistByName(name);
        if (StringUtil.isNullOrEmpty(artist.getAvatar())) {
            List<Song> song = songMapper.conditionAndPagedQuery(artist.getName(), "artist");
            artist.setAvatar(song.get(0).getCover());
        }
        return artist;
    }

    public PageBean<Artist> conditionAndPagedQuery(String condition, Integer pageNum, Integer pageSize) {
        PageBean<Artist> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        if (condition == null || condition.isEmpty()) {
            // 没有条件，执行分页查询
            List<Artist> artists = artistMapper.PagedQuery();
            pb.setTotal(artists.size()); // 直接使用分页结果的size作为总数
            pb.setItems(artists);
        } else {
            // 有条件，执行条件查询
            List<Artist> artists = artistMapper.conditionAndPagedQuery(condition);
            pb.setTotal(artists.size()); // 直接使用分页结果的size作为总数
            pb.setItems(artists);
        }
        return pb;
    }
}
