package com.sample.music.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sample.music.common.FilesType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class Artist extends FilesType {
    /**
     * 艺术家id
     */
    private Long id;
    /**
     * 艺术家名
     */
    private String name;
    /**
     * 艺术家性别
     */
    private String sex;
    /**
     * 艺术家照片
     */
    private String avatar;
    /**
     * 关注数
     */
    private Integer follows;
    /**
     * 艺术家描述
     */
    private String description;
    /**
     * 相关标签
     */
    private String tags;
    /**
     * 是否认证
     */
    private Integer isCheck;
    /**
     * 艺术家出生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String birthday;
    /**
     * 艺术家信息上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;
    /**
     * 艺术家信息上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updateTime;
}
