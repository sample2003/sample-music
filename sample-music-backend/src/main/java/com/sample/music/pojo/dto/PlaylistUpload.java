package com.sample.music.pojo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PlaylistUpload {
    /**
     * 歌单标题
     */
    private String title;
    /**
     * 是否隐私
     */
    private Integer isPublic;
    /**
     * 图片文件
     */
    @JsonIgnore
    private MultipartFile imageFiles;
    /**
     * 标签
     */
    private List<String> tags;
}