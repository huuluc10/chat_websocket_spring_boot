package com.huuluc.chat_service.controller;

import com.huuluc.chat_service.model.ChatMessage;
import com.huuluc.chat_service.model.ChatRoom;
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



@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {
    private SimpMessagingTemplate simpMessagingTemplate;
    private ChatRoomService chatRoomService;
    @MessageMapping("/chat/{chatId}")
    @SendTo("/user/chat/{chatId}")
    public ChatMessage sendMessageWithWebsocket(@DestinationVariable String chatId,
                                                      @Payload Message<ChatMessage> message) {
        log.info("new message arrived in chat with id {}", chatId);
        log.info("{} to {} at {}", message.getPayload().getSender(), message.getPayload().getReceiver(), message.getPayload().getTimestamp());

        //Get participants from chatId
        ChatRoom chatRoom = chatRoomService.getChatRoomById(chatId);

        simpMessagingTemplate.convertAndSend("/user/chat/" + chatId, message.getPayload());
        ChatMessage messages = message.getPayload();
        return messages;
    }
}
