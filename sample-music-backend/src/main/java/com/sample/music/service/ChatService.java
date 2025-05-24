package com.sample.music.service;

import com.sample.music.config.DeepSeekConfig;
import com.sample.music.pojo.dto.ChatRequest;
import com.sample.music.pojo.vo.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final DeepSeekConfig chatClient;
    private final WebClient deepSeekWebClient;

    public String generateText(String prompt) {
        ChatRequest request = ChatRequest.builder()
                .model("deepseek-chat")
                .messages(List.of(
                        new ChatRequest.Message("user", prompt)
                ))
                .temperature(0.7)
                .build();

        return deepSeekWebClient.post()
                .uri(chatClient.getApiUrl())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + chatClient.getApiKey())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .map(res -> res.getChoices().get(0).getMessage().getContent())
                .block();
    }

    @Bean
    public WebClient webClient(DeepSeekConfig config) {
        return WebClient.builder()
                .baseUrl(config.getApiUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
