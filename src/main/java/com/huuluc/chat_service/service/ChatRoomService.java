package com.huuluc.chat_service.service;

import com.huuluc.chat_service.model.ChatMessage;
import com.huuluc.chat_service.model.ChatRoom;
import com.huuluc.chat_service.model.UserApp;
import com.huuluc.chat_service.model.dto.ChatMessagePayload;
import com.huuluc.chat_service.model.dto.ChatRoomPayload;
import com.huuluc.chat_service.model.dto.CreateChatRoomRequest;
import com.huuluc.chat_service.repository.ChatRoomRepository;
import com.huuluc.chat_service.repository.UserAppRepository;
import com.huuluc.englearn.utils.HeadersHTTP;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService{
    private final ChatRoomRepository chatRoomRepository;
    private final UserAppRepository userAppRepository;

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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType(HeadersHTTP.MEDIA_TYPE, HeadersHTTP.MEDIA_SUBTYPE, StandardCharsets.UTF_8));

        if (chatRoom.isPresent()){
            return ResponseEntity.ok().headers(headers).body(chatRoom.get());
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

    public List<ChatRoomPayload> getChatRoomByParticipant(String participant) {
        log.info("Get chat room by participant: " + participant);
        Optional<List<ChatRoom>> chatRooms = chatRoomRepository.getChatRoomByParticipant(participant);
        List<ChatRoomPayload> chatRoomPayloads = new ArrayList<>();

         if (chatRooms.isPresent()) {
            for (ChatRoom chatRoom : chatRooms.get()){
                if (chatRoom.getLastMessage() == null){
                    chatRoomRepository.deleteById(chatRoom.getId());
                }
            }
        }

         for (ChatRoom chatRoom : chatRooms.get()){
             ChatRoomPayload chatRoomPayload = ChatRoomPayload.builder()
                     .chatId(chatRoom.getChatId())
                     .participants(chatRoom.getParticipants().stream().filter(p -> !p.equals(participant)).collect(Collectors.toList()))
                     .lastMessage(null)
                     .isSeen(chatRoom.isSeen())
                     .build();

             ChatMessage lastMessage = chatRoom.getLastMessage();

             String sender = lastMessage.getSender();
             String senderAvatar = userAppRepository.findByUsername(sender).getAvatar();
             String receiver = lastMessage.getReceiver();
             String receiverAvatar = userAppRepository.findByUsername(receiver).getAvatar();

             ChatMessagePayload chatMessagePayload = new ChatMessagePayload(lastMessage);
             chatMessagePayload.setChatId(chatRoom.getChatId());
             chatMessagePayload.setSenderAvatar(senderAvatar);
             chatMessagePayload.setReceiverAvatar(receiverAvatar);

             chatRoomPayload.setLastMessage(chatMessagePayload);
             chatRoomPayloads.add(chatRoomPayload);
            }

        return chatRoomPayloads;
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
