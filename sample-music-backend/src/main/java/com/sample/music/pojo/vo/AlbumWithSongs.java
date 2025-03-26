package com.sample.music.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sample.music.pojo.entity.Song;
import com.sample.music.pojo.vo.view.SongView;
import lombok.Data;

import java.util.List;

@Data
public class AlbumWithSongs {
    /**
     * 专辑id
     */
    private Long id;
    /**
     * 艺术家Id
     */
    private Long artistId;
    /**
     * 艺术家名
     */
    private String artist;
    /**
     * 艺术家名
     */
    private String artistAvatar;
    /**
     * 专辑名
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
     * 专辑封面
     */
    private String cover;
    /**
     * 专辑简介
     */
    private String description;
    /**
     * 专辑发布日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String releaseDate;

    /**
     * 专辑里的所有歌曲
     */
    List<SongView> songs;
}
