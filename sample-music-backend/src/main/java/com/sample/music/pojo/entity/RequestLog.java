package com.sample.music.pojo.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RequestLog {
/*    private Long id;
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
    private Date createTime;*/


    private Long id;
    private String url;
    private String requestMethod;
    private String request;
    private String response;
    private BigDecimal consume;
    private String requestTime;

}