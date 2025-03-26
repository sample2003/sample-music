package com.sample.music.pojo.vo.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PublicityView {
    /**
     * 公告id
     */
    private Long id;
    /**
     * 发布人
     */
    private String username;
    /**
     * 发布人头像
     */
    private String userAvatar;
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
