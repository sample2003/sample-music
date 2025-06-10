package com.sample.music.service;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.sample.music.constant.TargetType;
import com.sample.music.exception.BusinessException;
import com.sample.music.mapper.ArtistMapper;
import com.sample.music.mapper.SongMapper;
import com.sample.music.pojo.dto.PageBean;
import com.sample.music.pojo.entity.Album;
import com.sample.music.mapper.AlbumMapper;
import com.sample.music.pojo.entity.Artist;
import com.sample.music.pojo.entity.Song;
import com.sample.music.pojo.vo.AlbumWithSongs;
import com.sample.music.pojo.vo.upload.AlbumUpload;
import com.sample.music.pojo.vo.view.AlbumView;
import com.sample.music.pojo.vo.view.SongView;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final ArtistMapper artistMapper;

    private final AlbumMapper albumMapper;

    private final FileManageService fileManageService;

    private final RelateService relateService;

    private final ArtistService artistService;

    private final SongMapper songMapper;

    public void insertAlbumByOne(AlbumUpload albumUpload) {
        if (albumUpload.getImageFiles() != null) {
            String coverUrl = fileManageService.uploadFile(albumUpload.getImageFiles(), "cover");
            albumUpload.setCover(coverUrl);
        } else {
            albumUpload.setCover(null);
        }
        Long artistId = artistService.selectArtistByName(albumUpload.getArtist()).getId();
        Album album = new Album();
        BeanUtils.copyProperties(albumUpload, album);
        album.setArtistId(artistId);
        album.setLikes(0);
        album.setListeners(0);
        albumMapper.insertAlbumByOne(album);
    }

    public void deleteAlbumById(Long id) {
        if (this.isExist(id)) {
            AlbumView album = this.selectAlbumById(id);
            fileManageService.delete(album.getCover());
            albumMapper.deleteAlbumById(id);
        } else {
            throw new BusinessException(500, "专辑不存在");
        }
    }

    public void updateAlbumById(Album album) {
        if (this.isExist(album.getId())) {
            String coverUrl = fileManageService.uploadFile(album.getImageFiles(), "cover");
            album.setCover(coverUrl);
            this.updateAlbumById(album);
            albumMapper.updateAlbumById(album);
        } else {
            throw new BusinessException(500, "更新专辑失败");
        }
    }

    public AlbumView selectAlbumById(Long id) {
        AlbumView albumView = albumMapper.selectAlbumById(id);
        if (StringUtil.isNullOrEmpty(albumView.getCover())) {
            List<Song> song = songMapper.conditionAndPagedQuery(albumView.getTitle(), "album");
            albumView.setCover(song.get(0).getCover());
        }
        return albumView;
    }

    public Album selectAlbumByTitle(String title) {
        return albumMapper.selectAlbumByTitle(title);
    }

    public List<Album> selectAlbumByAll() {
        return albumMapper.selectAlbumByAll();
    }

    public boolean isExist(Long id) {
        AlbumView album = selectAlbumById(id);
        return album != null;
    }

    public PageBean<AlbumView> conditionAndPagedQuery(String condition, Integer pageNum, Integer pageSize) {
        PageBean<AlbumView> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<AlbumView> albums;// 直接使用分页结果的size作为总数
        if (condition == null || condition.isEmpty()) {
            // 没有条件，执行分页查询
            albums = albumMapper.PagedQuery();
        } else {
            // 有条件，执行条件查询
            albums = albumMapper.conditionAndPagedQuery(condition);
        }
        for (AlbumView album : albums) {
            if (StringUtil.isNullOrEmpty(album.getCover())) {
                List<Song> song = songMapper.conditionAndPagedQuery(album.getTitle(), "album");
                album.setCover(song.get(0).getCover());
            }
        }
        pb.setTotal(albums.size()); // 直接使用分页结果的size作为总数
        pb.setItems(albums);
        return pb;
    }

    public AlbumWithSongs selectAlbumWithSongs(Long albumId) {
        // 获取专辑信息
        AlbumView album = albumMapper.selectAlbumById(albumId);
        if (album == null) throw new BusinessException(404, "未发现该专辑");
        if (StringUtil.isNullOrEmpty(album.getCover())) {
            List<Song> song = songMapper.conditionAndPagedQuery(album.getTitle(), "album");
            album.setCover(song.get(0).getCover());
        }
        // 获取专辑及相关信息
        AlbumWithSongs albumWithSongs = new AlbumWithSongs();
        BeanUtils.copyProperties(album, albumWithSongs);
        // 获取专辑中的歌曲
        List<Song> songs = relateService.selectSongsAllByAlbum(albumId);
        List<SongView> songViews = new ArrayList<>();
        songs.forEach(song -> {
            SongView songView = new SongView();
            BeanUtils.copyProperties(song, songView);
            songView.setTags(JSONUtil.toList(song.getTags(), String.class));
            songView.setArtistAvatar(artistMapper.selectArtistByName(song.getArtist()).getAvatar());
            songView.setAlbumCover(albumMapper.selectAlbumByTitle(song.getAlbum()).getCover());
            songViews.add(songView);
        });
        albumWithSongs.setSongs(songViews);
        // 获取歌手信息
        Artist artist = artistService.selectArtistByName(album.getArtist());
        albumWithSongs.setArtistAvatar(artist.getAvatar());
        return albumWithSongs;
    }

    /**
     * 收听数+1
     *
     * @param id 歌单id
     */
    public void listenersPlusOne(Long id) {
        albumMapper.listenersPlusOne(id);
    }
}
