package com.sample.music.pojo.vo.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sample.music.pojo.entity.Song;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PlaylistView {
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
     * 歌单里的所有歌曲的数量
     */
    private Integer songsNumber;
}
