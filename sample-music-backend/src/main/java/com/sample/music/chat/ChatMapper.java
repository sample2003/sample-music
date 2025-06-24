package com.sample.music.chat;

import com.sample.music.chat.model.ChatList;
import com.sample.music.chat.model.ChatMessage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMapper {
    void insertMessage(Long userId, String sessionId, String message, int isAi);

    List<ChatList> userSelectChatList(Long userId);

    List<ChatMessage>  userSelectChatDetail(Long userId, String sessionId);
}
