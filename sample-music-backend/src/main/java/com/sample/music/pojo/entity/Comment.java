package com.sample.music.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    /**
     * 评论id
     */
    private Long id;
    /**
     * 发布评论用户id
     */
    private Long userId;
    /**
     * 内容
     */
    private String content;
    /**
     * 点赞数
     */
    private Integer likes;
    /**
     * 评论发布为哪个类型
     */
    private String targetType;
    /**
     * 评论发布在哪里
     */
    private Long targetId;
    /**
     * 评论发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;
    /**
     * 评论更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updateTime;
}
