package com.huuluc.chat_service.controller;

import com.huuluc.chat_service.model.ChatRoom;
import com.huuluc.chat_service.model.request.CreateChatRoomRequest;
import com.huuluc.chat_service.model.request.FindChatRoomRequest;
import com.huuluc.chat_service.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping("/chat-room")
    public ChatRoom createChatRoom(@RequestBody CreateChatRoomRequest request) {
        return chatRoomService.createChatRoom(request);
    }

    @GetMapping("/chat-room/get")
    public Optional<ChatRoom> getChatRoomById(@RequestBody FindChatRoomRequest request) {
        log.info("Get chat room by participants: {}", request.getParticipants());
        return chatRoomService.existsByParticipants(request.getParticipants());
    }

    @GetMapping("/chat-room")
    public List<ChatRoom> getChatRoomByParticipant(@RequestParam String participant) {
        log.info("Get chat room by participant: {}", participant);
        return chatRoomService.getChatRoomByParticipant(participant);
    }

    @PostMapping("/chat-room/update")
    public ChatRoom markChatRoomAsSeen(@RequestParam String chatId) {
        log.info("Mark chat room as seen by chatId: {}", chatId);
        return chatRoomService.markChatRoomAsSeen(chatId);
    }
}
