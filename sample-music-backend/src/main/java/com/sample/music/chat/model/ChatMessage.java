package com.sample.music.chat.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class ChatMessage {
    private Long id;
    private Long userId;
    private String sessionId;
    private String message;
    private Boolean isAi;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private String createTime;
}