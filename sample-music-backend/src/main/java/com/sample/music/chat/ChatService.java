package com.sample.music.chat;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

public interface ChatService {
    String generateText(String prompt);
    Flux<String> generateStream(String prompt);
    void saveChatRecord(Long userId, String sessionId, String prompt, String response);
}