package com.huuluc.chat_service.controller;

import com.huuluc.chat_service.model.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;



@Controller
@Slf4j
public class ChatController {
    @MessageMapping("/chat/{chatId}")
    @SendTo("/user/chat/{chatId}")
    public ChatMessage sendMessageWithWebsocket(@DestinationVariable String chatId,
                                                      @Payload Message<ChatMessage> message) {
        log.info("new message arrived in chat with id {}", chatId);
        log.info("{} to {} at {}", message.getPayload().getSender(), message.getPayload().getReceiver(), message.getPayload().getTimestamp());
        ChatMessage messages = message.getPayload();
        return messages;
    }
}
