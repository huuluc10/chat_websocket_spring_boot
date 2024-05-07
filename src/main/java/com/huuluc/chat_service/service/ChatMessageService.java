package com.huuluc.chat_service.service;

import com.huuluc.chat_service.model.ChatMessage;
import com.huuluc.chat_service.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository repository;

    public void saveChatMessage(ChatMessage chatMessage) {
        repository.save(chatMessage);
    }

    public List<ChatMessage> getChatHistory(String chatId) {
        Optional<List<ChatMessage>> history = repository.findByChatId(chatId);

        if (history.isEmpty()) {
            return List.of();
        } else {
            return history.get();
        }
    }
}
