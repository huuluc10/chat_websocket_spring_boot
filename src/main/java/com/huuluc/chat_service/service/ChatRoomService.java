package com.huuluc.chat_service.service;

import com.huuluc.chat_service.model.ChatMessage;
import com.huuluc.chat_service.model.ChatRoom;
import com.huuluc.chat_service.model.request.CreateChatRoomRequest;
import com.huuluc.chat_service.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService{
    private final ChatRoomRepository chatRoomRepository;

    public Optional<ChatRoom> getChatRoomByChatId(String chatId){
        return chatRoomRepository.findByChatId(chatId);
    }


    public ChatRoom createChatRoom(CreateChatRoomRequest request, ChatMessage lastMessage){
        if (request.getChatId() == null) {
            request.setChatId(UUID.randomUUID().toString());
        }
        ChatRoom chatRoom = ChatRoom.builder()
                .id(request.getChatId())
                .chatId(request.getChatId())
                .participants(request.getParticipants())
                .lastMessage(lastMessage)
                .isSeen(false)
                .build();

        return chatRoomRepository.save(chatRoom);
    }

    public ResponseEntity<ChatRoom> existsByParticipants(List<String> participants) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.existsByParticipants(participants);

        if (chatRoom.isPresent()){
            return ResponseEntity.ok(chatRoom.get());
        }
        return ResponseEntity.noContent().build();
    }

    public ChatRoom updateChatRoom(ChatMessage chatMessage) {
        List<String> participants = new ArrayList<>();
        participants.add(chatMessage.getSender());
        participants.add(chatMessage.getReceiver());

        //Check if chat room exists
        ChatRoom cR = this.existsByParticipants(participants).getBody();

        ChatRoom chatRoom;
        if (cR == null){
            String chatId = UUID.randomUUID().toString();
            chatMessage.setChatId(chatId);
            CreateChatRoomRequest request = CreateChatRoomRequest.builder()
                    .chatId(chatId)
                    .participants(participants)
                    .build();
            chatRoom = this.createChatRoom(request, chatMessage);
        } else {
            chatRoom = cR;
            chatMessage.setChatId(chatRoom.getChatId());
            chatRoom.setLastMessage(chatMessage);
            chatRoom.setSeen(false);
            // Delete old chat room and save new chat room
            chatRoomRepository.deleteById(chatRoom.getId());
            chatRoomRepository.save(chatRoom);
        }
        return chatRoom;
    }

    public List<ChatRoom> getChatRoomByParticipant(String participant) {
        log.info("Get chat room by participant: " + participant);
        Optional<List<ChatRoom>> chatRooms = chatRoomRepository.getChatRoomByParticipant(participant);

         if (chatRooms.isPresent()) {
            for (ChatRoom chatRoom : chatRooms.get()){
                if (chatRoom.getLastMessage() == null){
                    chatRoomRepository.deleteById(chatRoom.getId());
                }
            }
        }
        return chatRooms.orElseGet(ArrayList::new);
    }

    public ChatRoom markChatRoomAsSeen(String chatId) {
        log.info("Mark chat room as seen by chatId: " + chatId);
        //Check if chat room exists
        Optional<ChatRoom> chatRoomOptional = this.getChatRoomByChatId(chatId);

        if (chatRoomOptional.isEmpty()){
            return null;
        }
        ChatRoom chatRoom = chatRoomOptional.get();

        chatRoomRepository.deleteById(chatRoom.getId());
        chatRoom.setSeen(true);
        return chatRoomRepository.save(chatRoom);
    }
}
