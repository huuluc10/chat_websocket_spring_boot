package com.huuluc.chat_service.repository;

import com.huuluc.chat_service.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    @Query(value = "{\"chatId\": ?0}", sort = "{ 'timestamp' : 1 }")
    Optional<List<ChatMessage>> findByChatId(String chatId);
}
