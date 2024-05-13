package com.huuluc.chat_service.model.request;

import com.huuluc.chat_service.model.ChatMessage;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class CreateChatRoomRequest {
    private String chatId;
    private List<String> participants;
}
