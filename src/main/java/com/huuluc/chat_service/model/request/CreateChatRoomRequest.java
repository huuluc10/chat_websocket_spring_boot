package com.huuluc.chat_service.model.request;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class CreateChatRoomRequest {
    private List<String> participants;
}
