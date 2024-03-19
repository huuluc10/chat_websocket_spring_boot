package com.huuluc.chat_service.controller;

import com.huuluc.chat_service.model.ChatMessage;
import com.huuluc.chat_service.model.ChatRoom;
import com.huuluc.chat_service.model.request.CreateChatRoomRequest;
import com.huuluc.chat_service.service.ChatMessageService;
import com.huuluc.chat_service.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

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
    @MessageMapping("/chat/{chatId}")
    @SendTo("/user/chat/{chatId}")
    public ChatMessage sendMessageWithWebsocket(@DestinationVariable String chatId,
                                                      @Payload Message<ChatMessage> message) {
        log.info("new message arrived in chat with id {}", chatId);
        log.info("{} to {} at {}", message.getPayload().getSender(), message.getPayload().getReceiver(), message.getPayload().getTimestamp());
        log.info("content: {}", message.getPayload().getMessage());
        log.info("Id: {}", message.getPayload().getId());

        // Get participants from chatId
        ChatMessage chatMessage = message.getPayload();
        List<String> participants = Arrays.asList(chatMessage.getSender(), chatMessage.getReceiver());

        // Update chat room
        ChatRoom chatRoom = chatRoomService.updateChatRoom(chatMessage);


        // Send chat room to each participant
        for (String participant : participants) {
            simpMessagingTemplate.convertAndSendToUser(participant, "/chats", chatRoom);
        }

        // Save message to database
        chatMessageService.saveChatMessage(chatMessage);
        return chatMessage;
    }
}
