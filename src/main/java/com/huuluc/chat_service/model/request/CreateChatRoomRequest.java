package com.huuluc.chat_service.model.request;

import com.huuluc.chat_service.model.ChatMessage;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class CreateChatRoomRequest {
    private List<String> participants;
    private ChatMessage lastMessage;
}
