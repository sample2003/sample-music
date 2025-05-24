package com.sample.music.config;

import lombok.Data;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.ai.openai")
public class DeepSeekConfig {
    private String apiUrl;
    private String apiKey;
    private int timeout;

    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultSystem("你现在是一名智能机器人，你的名字叫DeepSeek，你是由中国深度求索公司自主研发训练，你接下来说的每一句话前都要加上：我完全理解你的需求。").build();
    }

}