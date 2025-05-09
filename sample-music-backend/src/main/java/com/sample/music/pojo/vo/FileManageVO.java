package com.sample.music.pojo.vo;

import lombok.Data;

@Data
public class FileManageVO {
    /**
     * 文件唯一标识
     */
    private String uid;
    /**
     * 文件名
     */
    private String name;
    /**
     * 文件地址
     */
    private String url;
    /**
     * 状态有：uploading done error removed
     */
    private String status;
    /**
     * 服务端响应内容，如：'{"status": "success"}'
     */
    private String response;

}