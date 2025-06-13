package com.sample.music.chat;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMapper {
    void insertMessage(Long userId, String sessionId, String message, int isAi);

    List<ChatMessage> selectAllSession();

    List<ChatMessage>  selectBySession(Long userId, String sessionId);
}
