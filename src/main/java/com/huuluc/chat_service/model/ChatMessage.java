package com.huuluc.chat_service.model;

import com.huuluc.chat_service.model.dto.ChatMessagePayload;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chat_message")
public class ChatMessage {
    @Id
    private String id;
    private String chatId;
    private String message;
    private String sender;
    private String receiver;
    private Date timestamp;

    public ChatMessage(ChatMessagePayload payload) {
        this.chatId = payload.getChatId();
        this.message = payload.getMessage();
        this.sender = payload.getSender();
        this.receiver = payload.getReceiver();
        this.timestamp = payload.getTimestamp();
    }
}
