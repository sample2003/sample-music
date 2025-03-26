package com.sample.music.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sample.music.common.FilesType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class Playlist extends FilesType {
    /**
     * 歌单id
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
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
     * 标签列表（json数组）
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
     * 歌单创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;
    /**
     * 歌单更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updateTime;
}
