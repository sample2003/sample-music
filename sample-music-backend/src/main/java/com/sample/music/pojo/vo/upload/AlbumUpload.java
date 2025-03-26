package com.sample.music.pojo.vo.upload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sample.music.common.FilesType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlbumUpload extends FilesType {
    /**
     * 艺术家名
     */
    private String artist;
    /**
     * 专辑名
     */
    private String title;
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
     * 专辑信息更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updateTime;
}
