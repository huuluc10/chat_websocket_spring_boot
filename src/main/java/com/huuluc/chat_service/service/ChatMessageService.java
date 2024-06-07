package com.huuluc.chat_service.service;

import com.huuluc.chat_service.model.ChatMessage;
import com.huuluc.chat_service.model.dto.ChatMessagePayload;
import com.huuluc.chat_service.repository.ChatMessageRepository;
import com.huuluc.chat_service.repository.UserAppRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final UserAppRepository userAppRepository;

    public void saveChatMessage(ChatMessage chatMessage) {
        repository.save(chatMessage);
    }

    public List<ChatMessagePayload> getChatHistory(String chatId) {
        Optional<List<ChatMessage>> history = repository.findByChatId(chatId);

        if (history.isEmpty()) {
            return List.of();
        } else {
            List<ChatMessage> chatHistory = history.get();
            List<ChatMessagePayload> payload = new ArrayList<>();

            for (ChatMessage chatMessage : chatHistory) {
                ChatMessagePayload chatMessagePayload = new ChatMessagePayload(chatMessage);

                String sender = chatMessagePayload.getSender();
                String senderAvatar = userAppRepository.findByUsername(sender).getAvatar();
                String receiver = chatMessagePayload.getReceiver();
                String receiverAvatar = userAppRepository.findByUsername(receiver).getAvatar();

                chatMessagePayload.setSenderAvatar(senderAvatar);
                chatMessagePayload.setReceiverAvatar(receiverAvatar);
                chatMessagePayload.setChatId(chatMessage.getChatId());

                payload.add(chatMessagePayload);

            }

            return payload;
        }
    }
}
