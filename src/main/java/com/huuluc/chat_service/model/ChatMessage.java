package com.huuluc.chat_service.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Setter
@Getter
@Builder
@Document(collection = "chat_message")
public class ChatMessage {
    @Id
    private String id;
    private String chatId;
    private String content;
    private String sender;
    private String receiver;
    private Date timestamp;
}
