package com.sample.music.service;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sample.music.exception.BusinessException;
import com.sample.music.mapper.AlbumMapper;
import com.sample.music.mapper.ArtistMapper;
import com.sample.music.pojo.dto.PageBean;
import com.sample.music.pojo.dto.PlaylistUpload;
import com.sample.music.pojo.dto.UserDTO;
import com.sample.music.pojo.entity.Comment;
import com.sample.music.pojo.entity.Playlist;
import com.sample.music.mapper.PlaylistMapper;
import com.sample.music.pojo.entity.Song;
import com.sample.music.pojo.entity.User;
import com.sample.music.pojo.vo.CommentWithUser;
import com.sample.music.pojo.vo.PlaylistWithSongs;
import com.sample.music.pojo.vo.view.SongView;
import com.sample.music.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistMapper playlistMapper;

    private final ArtistMapper artistMapper;

    private final AlbumMapper albumMapper;

    private final RelateService relateService;

    private final FileManageService fileManageService;

    private final UserService userService;

    /**
     * 用户创建歌单
     *
     * @param playlistUpload 歌单信息
     */
    public void insertPlaylist(PlaylistUpload playlistUpload) {
        Playlist playlist = new Playlist();
        BeanUtils.copyProperties(playlistUpload, playlist);
        if (playlistUpload.getImageFiles() != null) {
            String coverUrl = fileManageService.uploadFile(playlistUpload.getImageFiles(), "cover");
            playlist.setCover(coverUrl);
        } else {
            playlist.setCover(null);
        }
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer id = (Integer) claims.get("id");
        Long userId = Long.valueOf(id);
        playlist.setUserId(userId);
        playlistMapper.insertPlaylist(playlist);
    }

    /**
     * 删除歌单
     *
     * @param id 歌单id
     */
    public void deletePlayListById(Long id) {
        playlistMapper.deletePlayListById(id);
    }

    /**
     * 修改歌单
     *
     * @param playList 歌单信息与图片文件
     */
    public void updatePlayListById(Playlist playList) {
        playlistMapper.updatePlayListById(playList);
    }

    /**
     * 修改歌单图片
     *
     * @param id 歌单id
     * @param coverUrl 图片文件地址
     */
    public void updatePlaylistCover(Long id, String coverUrl) {
        playlistMapper.updatePlaylistCover(id, coverUrl);
    }

    /**
     * 查询所有歌单（管理员）
     *
     * @return List<PlayList>
     */
    public List<Playlist> queryPlayListByAll() {
        return playlistMapper.queryPlayListByAll();
    }

    /**
     * 条件分页查询歌单
     *
     * @param condition 条件
     * @param pageNum   分页参数，页码
     * @param pageSize  分页参数，每页大小
     * @return PageBean<PlayList>
     */
    public PageBean<Playlist> conditionAndPagedQuery(String condition, Integer pageNum, Integer pageSize) {

        PageBean<Playlist> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        if (condition == null || condition.isEmpty()) {
            // 没有条件，执行分页查询
            List<Playlist> playlists = playlistMapper.PublicPagedQuery();
            pb.setTotal(playlists.size()); // 直接使用分页结果的size作为总数
            pb.setItems(playlists);
        } else {
            // 有条件，执行条件查询
            List<Playlist> playlists = playlistMapper.PublicConditionAndPagedQuery(condition);
            pb.setTotal(playlists.size()); // 直接使用分页结果的size作为总数
            pb.setItems(playlists);
        }
        return pb;
    }

    public PageBean<PlaylistWithSongs> UserPagedQuery(boolean isPublic, String condition, Integer pageNum, Integer pageSize) {
        Long userId = null;
        List<Playlist> playlists;

        // 开启分页
        PageBean<PlaylistWithSongs> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        if(!isPublic) {
            // 获取登录用户
            Map<String, Object> claims = ThreadLocalUtil.get();
            Integer id = (Integer) claims.get("id");
            userId = Long.valueOf(id);
            // 查询歌单
            if(condition == null) playlists = playlistMapper.PagedQuery(userId);
            else playlists = playlistMapper.conditionAndPagedQuery(condition, userId);
        }else {
            // 查询歌单
            if(condition == null) playlists = playlistMapper.PublicPagedQuery();
            else playlists = playlistMapper.PublicConditionAndPagedQuery(condition);
        }
        List<PlaylistWithSongs> playlistWithSongs = this.enhancePlaylist(playlists);

        pb.setTotal(playlistWithSongs.size());
        pb.setItems(playlistWithSongs);

        return pb;
    }

    public List<PlaylistWithSongs> enhancePlaylist(List<Playlist> playlists) {
        System.out.println(playlists);
        List<PlaylistWithSongs> playlistWithSongs = new ArrayList<>();
        // 查询歌单信息并获取相关信息
        playlists.forEach(playlist -> {
            // 获取创建歌单的用户信息
            UserDTO user = userService.findUserById(playlist.getUserId());
            if (user != null) {
                PlaylistWithSongs playlistWithSong = new PlaylistWithSongs();
                BeanUtils.copyProperties(playlist, playlistWithSong);
                // 获取歌曲信息
                List<Song> songs = relateService.selectSongsAllByPlaylist(playlist.getId());
                List<SongView> songViews = new ArrayList<>();
                songs.forEach(song -> {
                    SongView songView = new SongView();
                    BeanUtils.copyProperties(song, songView);
                    songView.setTags(JSONUtil.toList(song.getTags(), String.class));
                    songView.setArtistAvatar(artistMapper.selectArtistByName(song.getArtist()).getAvatar());
                    songView.setAlbumCover(albumMapper.selectAlbumByTitle(song.getAlbum()).getCover());
                    songViews.add(songView);
                });
                playlistWithSong.setSongs(songViews);
                // 获取用户部分信息
                playlistWithSong.setUsername(user.getUsername());
                playlistWithSong.setUserAvatar(user.getAvatar());
                // 如果没有修改过歌单，自动获取歌单封面
                if(!songViews.isEmpty() && playlistWithSong.getIsUpdate() == 0) {
                    playlistMapper.updatePlaylistCover(playlistWithSong.getId(), songViews.get(0).getCover());
                }
                playlistWithSongs.add(playlistWithSong);
            }
        });
        return playlistWithSongs;
    }

    /**
     * 歌单是否存在
     *
     * @param id 歌单id
     * @return boolean
     */
    public boolean isExist(Long id) {
        Playlist playlist = selectPlayListById(id);
        return playlist != null;
    }

    /**
     * 根据id获取歌单
     *
     * @param id 歌单id
     * @return Playlist 歌单
     */
    public Playlist selectPlayListById(Long id) {
        return playlistMapper.selectPlaylistById(id);
    }

    /**
     * 查询一个歌单和相关歌曲信息（游客）
     *
     * @param id 歌单id
     * @return PlaylistWithSongs 歌单与相关信息
     */
    public PlaylistWithSongs queryPlaylistWithSongs(Long id) {
        // 获取歌单信息
        Playlist playlist = playlistMapper.selectPlaylistById(id);
        if (playlist == null) throw new BusinessException(404, "未发现该歌单");
        // 添加相关信息
        PlaylistWithSongs playlistWithSongs = new PlaylistWithSongs();
        BeanUtils.copyProperties(playlist, playlistWithSongs);
        // 添加歌曲
        List<Song> songs = relateService.selectSongsAllByPlaylist(id);
        List<SongView> songViews = new ArrayList<>();
        songs.forEach(song -> {
            SongView songView = new SongView();
            BeanUtils.copyProperties(song, songView);
            songView.setTags(JSONUtil.toList(song.getTags(), String.class));
            songView.setArtistAvatar(artistMapper.selectArtistByName(song.getArtist()).getAvatar());
            songView.setAlbumCover(albumMapper.selectAlbumByTitle(song.getAlbum()).getCover());
            songViews.add(songView);
        });
        playlistWithSongs.setSongs(songViews);
        // 添加用户信息
        UserDTO u = userService.findUserById(playlist.getUserId());
        playlistWithSongs.setUserId(u.getId());
        playlistWithSongs.setUsername(u.getUsername());
        playlistWithSongs.setUserAvatar(u.getAvatar());
        return playlistWithSongs;
    }

    /**
     * 收听数+1
     *
     * @param id 歌单id
     */
    public void listenersPlusOne(Long id) {
        playlistMapper.listenersPlusOne(id);
    }

}
