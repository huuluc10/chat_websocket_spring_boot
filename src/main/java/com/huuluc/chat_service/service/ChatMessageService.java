package com.huuluc.chat_service.service;

import com.huuluc.chat_service.model.ChatMessage;
import com.huuluc.chat_service.model.ChatRoom;
import com.huuluc.chat_service.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository repository;

    public void saveChatMessage(ChatMessage chatMessage) {
        repository.save(chatMessage);
    }
}
