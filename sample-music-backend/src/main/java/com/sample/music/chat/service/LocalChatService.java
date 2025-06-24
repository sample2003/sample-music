package com.sample.music.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Qualifier("localChatService") // 添加限定符
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.ai.model-type", havingValue = "LOCAL_DEEPSEEK")
public class LocalChatService implements ChatService{
    @Override
    public String generateText(String prompt, String sessionId) {
        return "";
    }

    @Override
    public Flux<String> generateStream(String prompt, String sessionId, boolean isNewChat) {
        return null;
    }

    @Override
    public void saveChatRecord(Long userId, String sessionId, String message, int isAi) {

    }
}
