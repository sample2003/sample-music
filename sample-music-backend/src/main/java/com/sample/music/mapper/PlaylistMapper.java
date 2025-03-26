package com.sample.music.mapper;

import com.sample.music.pojo.entity.Playlist;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PlaylistMapper {
    /**
     * 创建歌单（用户）
     */
    void insertPlaylist(Playlist songList);

    /**
     * 删除歌单（用户）
     */
    void deletePlayListById(Long id);

    /**
     * 更新歌单（用户）
     */
    void updatePlayListById(Playlist songList);

    /**
     * 更新歌单封面（用户）
     */
    void updatePlaylistCover(Long id, String cover);

    /**
     * 查询所有歌单（管理员）
     */
    List<Playlist> queryPlayListByAll();

    /**
     * 查询一个歌单和相关歌曲信息（游客）
     */
    Playlist selectPlaylistById(Long id);

    /**
     * 收听数
     */
    Playlist selectPlaylistByIdUser(Long id, Long userId);

    /**
     * 收听数
     */
    List<Playlist> selectPlayListByPage(Long userId);

    /**
     * 收听数
     */
    List<Playlist> PublicPagedQuery();

    /**
     * 收听数
     */
    List<Playlist> PagedQuery(Long userId);

    /**
     * 收听数
     */
    List<Playlist> PublicConditionAndPagedQuery(String condition);

    /**
     * 收听数
     */
    List<Playlist> conditionAndPagedQuery(String condition, Long userId);

    /**
     * 收听数
     */
    List<Playlist> queryAllByIds(List<Long> ids);

    /**
     * 收听数
     */
    void favoritesPlus(Long id);

    /**
     * 收听数
     */
    void favoriteSubtract(Long id);

    /**
     * 收听数
     */
    void listenersPlusOne(Long id);

}
