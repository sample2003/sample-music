package com.sample.music.pojo.vo.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

@Data
public class SongView {
    /**
     * 歌曲id
     */
    private Long id;
    /**
     * 艺术家名
     */
    private String artist;
    /**
     * 其他艺术家
     */
    private List<String> artists;
    /**
     * 艺术家头像
     */
    private String artistAvatar;
    /**
     * 专辑名
     */
    private String album;
    /**
     * 专辑封面
     */
    private String albumCover;
    /**
     * 歌曲名
     */
    private String title;
    /**
     * 歌曲时长（秒）
     */
    private Integer duration;
    /**
     * 歌曲文件地址
     */
    private String mp3Url;
    /**
     * 无损歌曲文件地址
     */
    private String flacUrl;
    /**
     * 歌曲封面文件地址
     */
    private String cover;
    /**
     * 歌曲歌词文件地址
     */
    private String lyric;
    /**
     * 风格
     */
    private String style;
    /**
     * 相关标签
     */
    private List<String> tags;
    /**
     * 收听数
     */
    private Integer listeners;
    /**
     * 权限：0 用户；1 管理员；
     */
    private Integer permission;
    /**
     * 歌曲发行日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String releaseDate;
}
