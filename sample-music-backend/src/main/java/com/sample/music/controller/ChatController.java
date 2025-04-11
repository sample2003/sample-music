package com.sample.music.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

/*    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat")
    public void handleChatMessage(ChatMessageDTO dto) {
        ChatMessage message = chatService.saveUserMessage(dto);
        messagingTemplate.convertAndSendToUser(
                dto.getUserId().toString(),
                "/queue/ai-response",
                new ResponseMessage("MESSAGE_RECEIVED", message)
        );
    }*/
}