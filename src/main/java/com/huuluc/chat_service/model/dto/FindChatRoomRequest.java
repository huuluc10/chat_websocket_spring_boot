package com.huuluc.chat_service.model.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class FindChatRoomRequest {
    private List<String> participants;
}
