package com.huuluc.chat_service.service;

import com.huuluc.chat_service.model.ChatMessage;
import com.huuluc.chat_service.model.ChatRoom;
import com.huuluc.chat_service.model.request.CreateChatRoomRequest;
import com.huuluc.chat_service.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Optional<ChatRoom> existsByParticipants(List<String> participants) {
        return chatRoomRepository.existsByParticipants(participants);
    }

    public ChatRoom updateChatRoom(ChatMessage chatMessage) {
        List<String> participants = new ArrayList<>();
        participants.add(chatMessage.getSender());
        participants.add(chatMessage.getReceiver());
        //Check if chat room exists
        Optional<ChatRoom> chatRoomOptional = this.existsByParticipants(participants);

        ChatRoom chatRoom;
        if (chatRoomOptional.isEmpty()){
            CreateChatRoomRequest request = CreateChatRoomRequest.builder()
                    .participants(participants)
                    .lastMessage(chatMessage)
                    .build();
            chatRoom = this.createChatRoom(request);
        } else {
            chatRoom = chatRoomOptional.get();
            chatRoom.setLastMessage(chatMessage);
            this.updateChatRoom(chatRoom);
        }
        return chatRoom;
    }

}
