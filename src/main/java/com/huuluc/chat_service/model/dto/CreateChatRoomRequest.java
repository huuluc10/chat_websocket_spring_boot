package com.huuluc.chat_service.model.dto;

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
