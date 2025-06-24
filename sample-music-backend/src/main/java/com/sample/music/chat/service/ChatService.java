package com.sample.music.chat.service;

import reactor.core.publisher.Flux;

public interface ChatService {
    String generateText(String prompt, String sessionId);

    Flux<String> generateStream(String prompt, String sessionId, boolean isNewChat);

    void saveChatRecord(Long userId, String sessionId, String message, int isAi);
}