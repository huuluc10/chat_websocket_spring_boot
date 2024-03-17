package com.huuluc.chat_service.controller;

import com.huuluc.chat_service.model.ChatRoom;
import com.huuluc.chat_service.model.request.CreateChatRoomRequest;
import com.huuluc.chat_service.model.request.FindChatRoomRequest;
import com.huuluc.chat_service.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping("/chat-room")
    public ChatRoom createChatRoom(@RequestBody CreateChatRoomRequest request) {
        return chatRoomService.createChatRoom(request);
    }

    @GetMapping("/chat-room")
    public ChatRoom getChatRoomById(@RequestBody FindChatRoomRequest request) {
        return chatRoomService.existsByParticipants(request.getParticipants());
    }

}
