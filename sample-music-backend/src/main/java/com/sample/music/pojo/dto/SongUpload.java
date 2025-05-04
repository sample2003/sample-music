package com.sample.music.pojo.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class SongUpload {
    /**
     * 歌曲id
     */
    private Long id;
    /**
     * 歌曲名
     */
    private String title;
    /**
     * 艺术家名
     */
    private String artist;
    /**
     * 其他艺术家
     */
    private List<String> artists;
    /**
     * 专辑名
     */
    private String album;
    /**
     * 歌曲时长（秒）
     */
    private Integer duration;
    /**
     * 歌曲文件
     */
    @JsonIgnore
    private MultipartFile mp3Url;
    /**
     * 无损歌曲文件
     */
    @JsonIgnore
    private MultipartFile flacUrl;
    /**
     * 歌曲封面文件
     */
    @JsonIgnore
    private MultipartFile imageFiles;
    /**
     * 歌曲歌词文件
     */
    @JsonIgnore
    private MultipartFile lyric;
    /**
     * 风格
     */
    private String style;
    /**
     * 相关标签
     */
    private List<String> tags;
    /**
     * 权限
     */
    private Integer permission;
    /**
     * 歌曲发行日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String releaseDate;
}
