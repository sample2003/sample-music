package com.sample.music.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Song {
    /**
     * 歌曲id
     */
    private Long id;
    /**
     * 艺术家名
     */
    private String artist;
    /**
     * 专辑名
     */
    private String album;
    /**
     * 歌曲名
     */
    private String title;
    /**
     * 其他艺术家
     */
    private String artists;
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
    private String tags;
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
    /**
     * 歌曲信息上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private String createTime;
    /**
     * 歌曲信息更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private String updateTime;
}
