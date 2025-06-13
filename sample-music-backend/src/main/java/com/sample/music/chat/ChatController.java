package com.sample.music.chat;

import com.github.pagehelper.PageHelper;
import com.sample.music.common.Result;
import com.sample.music.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatMapper chatMapper;

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

    private final ChatServiceFactory chatServiceFactory;

    @PostMapping("/generate")
    public Result<?> generate(@RequestBody Map<String, String> request) {
        try {
            String prompt = request.get("prompt");
            String sessionId = request.getOrDefault("sessionId", UUID.randomUUID().toString());
            Long userId = UserContext.getUser(); // 从上下文中获取当前用户

            ChatService chatService = chatServiceFactory.getChatService();
            String response = chatService.generateText(prompt);
            chatService.saveChatRecord(userId, sessionId, prompt, response);

            return Result.success(Map.of(
                    "sessionId", sessionId,
                    "result", response
            ));
        }catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/generate/stream")
    public SseEmitter streamGenerate(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");
        String sessionId = request.getOrDefault("sessionId", UUID.randomUUID().toString());
        Long userId = UserContext.getUser();

        SseEmitter emitter = new SseEmitter(60_000L); // 60秒超时
        StringBuilder fullResponse = new StringBuilder();

        ChatService chatService = chatServiceFactory.getChatService();
        chatService.generateStream(prompt)
                .subscribe(
                        content -> {
                            fullResponse.append(content);
                            try {
                                emitter.send(SseEmitter.event().data(content));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        error -> {
                            emitter.completeWithError(error);
                            chatService.saveChatRecord(userId, sessionId, prompt, fullResponse.toString());
                        },
                        () -> {
                            emitter.complete();
                            chatService.saveChatRecord(userId, sessionId, prompt, fullResponse.toString());
                        }
                );

        return emitter;
    }

    @GetMapping("/chatData")
    public Result<List<ChatMessage>> getChatHistory(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {

        Long userId = UserContext.getUser();
        PageHelper.startPage(page, size);
        List<ChatMessage> history = chatMapper.selectAllSession();
        return Result.success(history);
    }

    @GetMapping("/history/one")
    public Result<List<ChatMessage>> getChatHistory(
            @RequestParam String sessionId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {

        Long userId = UserContext.getUser();
        PageHelper.startPage(page, size);
        List<ChatMessage> history = chatMapper.selectBySession(userId, sessionId);
        return Result.success(history);
    }

}

