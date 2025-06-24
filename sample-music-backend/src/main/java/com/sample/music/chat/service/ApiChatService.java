package com.sample.music.chat.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.music.chat.ChatMapper;
import com.sample.music.chat.model.ChatMessage;
import com.sample.music.chat.model.ChatRequest;
import com.sample.music.chat.config.DeepSeekConfig;
import com.sample.music.pojo.vo.ChatResponse;
import com.sample.music.utils.UserContext;
import groovy.json.StringEscapeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Qualifier("apiChatService")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.ai.model-type", havingValue = "DEEPSEEK_API")
public class ApiChatService implements ChatService{
    private final DeepSeekConfig chatClient;

    private final ChatMapper chatMapper;

    private final WebClient deepSeekWebClient;

    @Override
    public String generateText(String prompt, String sessionId) {
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

    @Override
    public Flux<String> generateStream(String prompt, String sessionId, boolean isNewChat) {
        List<ChatRequest.Message> messages;
        if (isNewChat) {
            messages = List.of(new ChatRequest.Message("user", prompt));
        }else {
            List<ChatMessage> chatMessages = chatMapper.userSelectChatDetail(UserContext.getUser(), sessionId);

            messages = chatMessages.stream()
                    .map(msg -> new ChatRequest.Message(msg.getIsAi() ? "assistant" : "user", msg.getMessage()))
                    .collect(Collectors.toList());

            messages.add(new ChatRequest.Message("user", prompt));
        }

        ChatRequest request = ChatRequest.builder()
                .model("deepseek-chat")
                .messages(messages)
                .temperature(0.7)
                .stream(true)
                .build();

        return deepSeekWebClient.post()
                .uri(chatClient.getApiUrl())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + chatClient.getApiKey())
                .accept(MediaType.TEXT_EVENT_STREAM)  // 添加事件流接受类型
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(String.class)
                .map(this::parseSSEEvent)  // 解析SSE事件格式
                .filter(content -> !content.isEmpty());
    }

        /*public Flux<String> generateStream(String prompt) {
        ChatRequest request = ChatRequest.builder()
                .model("deepseek-chat")
                .messages(List.of(new ChatRequest.Message("user", prompt)))
                .temperature(0.7)
                .stream(true)
                .build();

        return deepSeekWebClient.post()
                .uri(chatClient.getApiUrl())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + chatClient.getApiKey())
                .accept(MediaType.TEXT_EVENT_STREAM)  // 添加事件流接受类型
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(String.class);
    }*/


    private String parseSSEEvent(String event) {
        try {
            JsonNode node = new ObjectMapper().readTree(event);

            // 处理可能的两种数据结构
            JsonNode choices = node.path("choices");
            if (choices.isArray() && !choices.isEmpty()) {
                JsonNode delta = choices.get(0).path("delta");

                // 检查是否存在content字段
                if (delta.has("content")) {
                    String content = delta.path("content").asText();
                    // 处理unicode转义字符（如\u0026 -> &）
                    System.out.println(StringEscapeUtils.unescapeJava(content));
                    return StringEscapeUtils.unescapeJava(content);
                }
            }
        } catch (Exception e) {
            // 添加日志记录异常
        }
        return "";
    }

    @Override
    @Transactional
    public void saveChatRecord(Long userId, String sessionId, String message, int isAi) {
        chatMapper.insertMessage(userId, sessionId, message, isAi);
    }

    @Bean
    public WebClient webClient(DeepSeekConfig config) {
        return WebClient.builder()
                .baseUrl(config.getApiUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
