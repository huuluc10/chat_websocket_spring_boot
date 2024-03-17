package com.huuluc.chat_service.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Setter
@Getter
@Builder
@Document(collection = "chat_room")
public class ChatRoom {
    @Id
    private String chatId;
    private List<String> participants;
    private ChatMessage lastMessage;
}
