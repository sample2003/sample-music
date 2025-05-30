package com.sample.music.controller;

import com.sample.music.annotation.Public;
import com.sample.music.common.Result;
import com.sample.music.service.ChatService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    /*private final OpenAiChatModel chatModel;

    private final List<Message> chatHistoryList = new ArrayList<>();

    @PostConstruct
    public void init() {
        chatHistoryList.add(new SystemMessage("You are a helpful assistant."));
    }

    @Public
    @GetMapping
    public ChatResponse chat(String message) {
        chatHistoryList.add(new UserMessage(message));
        Prompt prompt = new Prompt(chatHistoryList);
        ChatResponse chatResponse = chatModel.call(prompt);
        if (chatResponse.getResult() != null && chatResponse.getResult().getOutput() != null) {
            chatHistoryList.add(chatResponse.getResult().getOutput());
        }
        return chatResponse;
    }*/

    private final ChatService chatService;

    @Public
    @PostMapping("/generate")
    public Result<?> generate(@RequestBody Map<String, String> request) {
        try {
            String response = chatService.generateText(request.get("prompt"));
            return Result.success(Map.of("result", response));
        }catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

}

