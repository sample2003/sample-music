package com.sample.music.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sample.music.common.FilesType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class Publicity extends FilesType {
    /**
     * 公告id
     */
    private Long id;
    /**
     * 发布人id
     */
    private Long userId;
    /**
     * 公告标题
     */
    private String title;
    /**
     * 公告内容
     */
    private String message;
    /**
     * 观看人数
     */
    private Integer viewers;
    /**
     * 公告封面
     */
    private String cover;
    /**
     * 公告类型
     */
    private String publicityType;
    /**
     * 公告创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;
    /**
     * 公告更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updateTime;
}
