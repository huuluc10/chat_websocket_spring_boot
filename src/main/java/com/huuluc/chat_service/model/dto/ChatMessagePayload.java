package com.huuluc.chat_service.model.dto;

import com.huuluc.chat_service.model.ChatMessage;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessagePayload {
    private String chatId;
    private String message;
    private String sender;
    private String senderAvatar;
    private String receiver;
    private String receiverAvatar;
    private Date timestamp;

    public ChatMessagePayload(ChatMessage chatMessage) {
        this.chatId = chatMessage.getChatId();
        this.message = chatMessage.getMessage();
        this.sender = chatMessage.getSender();
        this.receiver = chatMessage.getReceiver();
        this.timestamp = chatMessage.getTimestamp();
    }
}
