package com.sample.music.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Data
public class FilesType {
    /**
     * 文本文件
     */
    @JsonIgnore
    private MultipartFile textFiles;
    /**
     * 图片文件
     */
    @JsonIgnore
    private MultipartFile imageFiles;
    /**
     * 音频文件
     */
    @JsonIgnore
    private MultipartFile audioFiles;
}
