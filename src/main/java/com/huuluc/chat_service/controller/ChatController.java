package com.huuluc.chat_service.controller;

import com.huuluc.chat_service.model.ChatMessage;
import com.huuluc.chat_service.model.ChatRoom;
import com.huuluc.chat_service.model.UserApp;
import com.huuluc.chat_service.model.request.CreateChatRoomRequest;
import com.huuluc.chat_service.service.ChatMessageService;
import com.huuluc.chat_service.service.ChatRoomService;
import com.huuluc.chat_service.service.UserAppService;
import com.huuluc.englearn.utils.HeadersHTTP;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final UserAppService userAppService;

    @MessageMapping("/chat")
    public void sendMessageWithWebsocket(@Payload Message<ChatMessage> message) {
        log.info("{} to {} at {}", message.getPayload().getSender(), message.getPayload().getReceiver(), message.getPayload().getTimestamp());

        // Get participants from chatId
        ChatMessage chatMessage = message.getPayload();
        List<UserApp> participants = new ArrayList<>();
        participants.add(UserApp.builder()
                .username(chatMessage.getSender())
                .avatar(chatMessage.getSenderAvatar())
                .build());
        participants.add(UserApp.builder()
                .username(chatMessage.getReceiver())
                .avatar(chatMessage.getReceiverAvatar())
                .build());

        // Create user if not exist
        for (UserApp participant : participants) {
            if (!userAppService.isUserAppExist(participant.getUsername())) {
                userAppService.createUserApp(participant);
            } else {
                userAppService.updateAvatar(participant);
            }
        }

        // Update chat room
        ChatRoom chatRoom = chatRoomService.updateChatRoom(chatMessage);


        simpMessagingTemplate.convertAndSendToUser(message.getPayload().getReceiver(), "/queue/chat/" + chatRoom.getChatId(), message.getPayload());
        simpMessagingTemplate.convertAndSendToUser(message.getPayload().getReceiver(), "/queue/chats", chatRoom);

        // Save message to database
        chatMessageService.saveChatMessage(chatMessage);
    }

    @GetMapping("/chat/{chatId}/history")
    public ResponseEntity<List<ChatMessage>> getChatHistory(@PathVariable String chatId) {
        log.info("Get chat history of chat with id {}", chatId);
        List<ChatMessage> chatHistory = chatMessageService.getChatHistory(chatId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType(HeadersHTTP.MEDIA_TYPE, HeadersHTTP.MEDIA_SUBTYPE, StandardCharsets.UTF_8));
        return ResponseEntity.ok().headers(headers).body(chatHistory);
    }
}
