package com.sample.music.chat.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.ai.openai")
public class DeepSeekConfig {
    private String apiUrl;
    private String apiKey;
    private int timeout;

/*    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultSystem("你现在是一名智能机器人，你的名字叫DeepSeek，你是由中国深度求索公司自主研发训练，你接下来说的每一句话前都要加上：我完全理解你的需求。").build();
    }*/

    @Bean
    @ConditionalOnProperty(name = "spring.ai.model-type", havingValue = "LOCAL_DEEPSEEK")
    public WebClient LocalDeepseek() {
        return WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "spring.ai.model-type", havingValue = "DEEPSEEK_API")
    public WebClient DeepseekApi() {
        return WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}