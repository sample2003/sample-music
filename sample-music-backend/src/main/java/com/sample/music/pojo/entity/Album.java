package com.sample.music.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sample.music.common.FilesType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class Album extends FilesType {
    /**
     * 专辑id
     */
    private Long id;
    /**
     * 艺术家id
     */
    private Long artistId;
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
     * 相关标签
     */
    private String tags;
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
