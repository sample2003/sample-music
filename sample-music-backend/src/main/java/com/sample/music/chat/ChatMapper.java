package com.sample.music.chat;

import com.sample.music.chat.model.ChatMessage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMapper {
    void insertMessage(Long userId, String message, String sessionId, int isAi);

    List<ChatMessage> selectAllSession();

    List<ChatMessage>  selectBySession(Long userId, String sessionId);
}
