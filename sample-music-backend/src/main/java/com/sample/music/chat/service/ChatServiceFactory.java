package com.sample.music.chat.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceFactory {
    private final ChatService apiService;
    private final ChatService localService;
    private final String modelType;

    public ChatServiceFactory(
            @Qualifier("apiChatService") @Lazy ChatService apiService,
            @Qualifier("localChatService") @Lazy ChatService localService,
            @Value("${spring.ai.model-type}") String modelType
    ) {
        this.apiService = apiService;
        this.localService = localService;
        this.modelType = modelType;
    }

    public ChatService getChatService() {
        if ("LOCAL_DEEPSEEK".equals(modelType) && localService == null) {
            throw new IllegalStateException("LOCAL_DEEPSEEK模式需要启用本地模型服务");
        }
        return "LOCAL_DEEPSEEK".equals(modelType) ? localService : apiService;
    }
}