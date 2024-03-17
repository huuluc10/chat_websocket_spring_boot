package com.huuluc.chat_service.service;

import com.huuluc.chat_service.model.ChatRoom;
import com.huuluc.chat_service.model.request.CreateChatRoomRequest;
import com.huuluc.chat_service.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService{
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom saveChatRoom(CreateChatRoomRequest request){
        ChatRoom chatRoom = ChatRoom.builder()
                .participants(request.getParticipants())
                .build();
        return chatRoomRepository.save(chatRoom);
    }
}
