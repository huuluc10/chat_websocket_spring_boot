package com.huuluc.chat_service.controller;

import com.huuluc.chat_service.model.ChatMessage;
import com.huuluc.chat_service.model.ChatRoom;
import com.huuluc.chat_service.model.UserApp;
import com.huuluc.chat_service.model.request.CreateChatRoomRequest;
import com.huuluc.chat_service.service.ChatMessageService;
import com.huuluc.chat_service.service.ChatRoomService;
import com.huuluc.chat_service.service.UserAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @MessageMapping("/chat/{chatId}")
    public void sendMessageWithWebsocket(@DestinationVariable String chatId,
                                                      @Payload Message<ChatMessage> message) {
        log.info("new message arrived in chat with id {}", chatId);
        log.info("{} to {} at {}", message.getPayload().getSender(), message.getPayload().getReceiver(), message.getPayload().getTimestamp());
        log.info("content: {}", message.getPayload().getMessage());
        log.info("Id: {}", message.getPayload().getId());

        // Get participants from chatId
        ChatMessage chatMessage = message.getPayload();
        List<String> participants = Arrays.asList(chatMessage.getSender(), chatMessage.getReceiver());

        // Create user if not exist
        for (String participant : participants) {
            if (!userAppService.isUserAppExist(participant)) {
                String avartarUrl = "images/avatars/" + participant + ".png";
                UserApp userApp = new UserApp(participant, avartarUrl);
                userAppService.createUserApp(userApp);
            }
        }

        // Update chat room
        ChatRoom chatRoom = chatRoomService.updateChatRoom(chatMessage);


        simpMessagingTemplate.convertAndSendToUser(message.getPayload().getReceiver(), "/queue/chat/" + chatId, message);
        simpMessagingTemplate.convertAndSendToUser(message.getPayload().getReceiver(), "/queue/chats", chatRoom);

        // Save message to database
        chatMessageService.saveChatMessage(chatMessage);
//        return chatMessage;
    }

    @GetMapping("/chat/{chatId}/history")
    public ResponseEntity<List<ChatMessage>> getChatHistory(@PathVariable String chatId) {
        log.info("Get chat history of chat with id {}", chatId);
        List<ChatMessage> chatHistory = chatMessageService.getChatHistory(chatId);
        return ResponseEntity.ok(chatHistory);
    }
}
