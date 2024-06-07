package com.huuluc.chat_service.model.dto;

import com.huuluc.chat_service.model.ChatMessage;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomPayload {
    private String chatId;
    private List<String> participants;
    private ChatMessagePayload lastMessage;
    private boolean isSeen;
}
