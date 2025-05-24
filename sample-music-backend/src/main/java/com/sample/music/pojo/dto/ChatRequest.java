package com.sample.music.pojo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChatRequest {
    private String model;
    private List<Message> messages;
    private Double temperature;

    @Data
    @Builder
    public static class Message {
        private String role;
        private String content;
        // 显式添加public构造函数
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
