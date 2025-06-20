package com.sample.music.pojo.entity;

import lombok.Data;
import java.util.Date;

@Data
public class RequestLog {
    private Long id;
    private String requestMethod;
    private String requestUri;
    private String remoteAddr;
    private String requestParams;
    private String requestBody;
    private String userAgent;
    private String authorization;
    private Integer responseStatus;
    private String responseBody;
    private Long executionTime;
    private Date createTime;
}