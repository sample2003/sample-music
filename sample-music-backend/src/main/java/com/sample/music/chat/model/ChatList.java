package com.sample.music.chat.model;

import lombok.Data;

@Data
public class ChatList {
    private Long userId;
    private String sessionId;
    private String content;
    private String lastMessageTime;
    private String firstMessageTime;
    private int timeCategory;
}
