package com.sample.music.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sample.music.pojo.entity.Song;
import com.sample.music.pojo.vo.view.SongView;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PlaylistWithSongs {
    /**
     * 歌单id
     */
    private Long id;
    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户头像
     */
    private String userAvatar;
    /**
     * 歌单名
     */
    private String title;
    /**
     * 点赞数
     */
    private Integer likes;
    /**
     * 收听数
     */
    private Integer listeners;
    /**
     * 歌单封面
     */
    private String cover;
    /**
     * 相关标签
     */
    private String tags;
    /**
     * 是否公开：0 私密，1 公开
     */
    private Integer isPublic;
    /**
     * 是否修改：0 未修改，1 修改
     */
    private Integer isUpdate;
    /**
     * 歌单简介
     */
    private String description;
    /**
     * 歌单创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;
    /**
     * 歌单更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updateTime;

    /**
     * 歌单里的所有歌曲
     */
    List<SongView> songs;
}
