package com.sample.music.controller;

import com.github.pagehelper.PageHelper;
import com.sample.music.chat.ChatMapper;
import com.sample.music.chat.model.ChatList;
import com.sample.music.chat.model.ChatMessage;
import com.sample.music.chat.service.ChatService;
import com.sample.music.chat.service.ChatServiceFactory;
import com.sample.music.common.Result;
import com.sample.music.utils.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
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
            String response = chatService.generateText(prompt, sessionId);
//            chatService.saveChatRecord(userId, sessionId, prompt, response);

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
        String sessionId;
        boolean isNewChat = true;
        sessionId = request.get("prompt");
        if (sessionId != null) {
            isNewChat = false;
            sessionId = request.getOrDefault("sessionId", UUID.randomUUID().toString());
        }
        Long userId = UserContext.getUser();

        SseEmitter emitter = new SseEmitter(60_000L); // 60秒超时
        StringBuilder fullResponse = new StringBuilder();

        ChatService chatService = chatServiceFactory.getChatService();
        String finalSessionId = sessionId;
        chatService.saveChatRecord(userId, finalSessionId, prompt, 0);
        chatService.generateStream(prompt, sessionId, isNewChat)
                .subscribe(
                        content -> {
                            fullResponse.append(content);
                            try {
                                emitter.send(SseEmitter.event().data(content));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        /*error -> {
                            emitter.completeWithError(error);
                            chatService.saveChatRecord(userId, finalSessionId, prompt, fullResponse.toString());
                        },
                        () -> {
                            emitter.complete();
                            chatService.saveChatRecord(userId, finalSessionId, prompt, fullResponse.toString());
                        }*/
                        error -> {
                            emitter.completeWithError(error);
                            chatService.saveChatRecord(userId, finalSessionId, fullResponse.toString(), 1);
                        },
                        () -> {
                            emitter.complete();
                            chatService.saveChatRecord(userId, finalSessionId, fullResponse.toString(), 1);
                        }
                );

        return emitter;
    }

    @GetMapping("/chatList")
    public Result<List<ChatList>> getChat(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {

        Long userId = UserContext.getUser();
        PageHelper.startPage(page, size);

        List<ChatList> history = chatMapper.userSelectChatList(userId);
        return Result.success(history);
    }

    @GetMapping("/chatDetail/{sessionId}")
    public Result<List<ChatMessage>> getChat(
            @PathVariable String sessionId ,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {

        Long userId = UserContext.getUser();
        PageHelper.startPage(page, size);
        List<ChatMessage> history = chatMapper.userSelectChatDetail(userId, sessionId);
        return Result.success(history);
    }

    public static void main(String[] args) {
        BigDecimal totalPrice = new BigDecimal("0");
        totalPrice = totalPrice.add(new BigDecimal("0"));
        System.out.println(totalPrice);
    }

/*    public Flux<Data> paginatedQuery(int pageSize) {
        return Flux.generate(sink -> {
            int currentPage = sink.currentContext().get("page");
            List<Data> pageResult = mybatisMapper.getPage(currentPage, pageSize);

            if(!pageResult.isEmpty()) {
                pageResult.forEach(sink::next);
                sink.currentContext().put("page", currentPage + 1);
            } else {
                sink.complete();
            }
        });
    }*/
}

