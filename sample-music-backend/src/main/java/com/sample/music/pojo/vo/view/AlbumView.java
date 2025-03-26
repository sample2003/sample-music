package com.sample.music.pojo.vo.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlbumView {
    /**
     * 专辑id
     */
    private Long id;
    /**
     * 专辑名
     */
    private String title;
    /**
     * 艺术家Id
     */
    private Long artistId;
    /**
     * 艺术家名字
     */
    private String artist;
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
     * 专辑信息上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;
    /**
     * 专辑信息更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updateTime;
}
