package com.sample.music.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliOssConfig {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String urlPrefix;
    private String maxFileSize;
    private String maxRequestSize;

    @Bean
    public OSS ossClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
}
