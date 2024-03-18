package com.huuluc.chat_service.service;

import com.huuluc.chat_service.model.ChatRoom;
import com.huuluc.chat_service.model.request.CreateChatRoomRequest;
import com.huuluc.chat_service.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService{
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom getChatRoomById(String chatId){
        return chatRoomRepository.findById(chatId).orElse(null);
    }

    public ChatRoom updateChatRoom(ChatRoom chatRoom){
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom createChatRoom(CreateChatRoomRequest request){
        ChatRoom chatRoom = ChatRoom.builder()
                .participants(request.getParticipants())
                .lastMessage(request.getLastMessage())
                .build();
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom existsByParticipants(List<String> participants) {
        return chatRoomRepository.existsByParticipants(participants);
    }

}
