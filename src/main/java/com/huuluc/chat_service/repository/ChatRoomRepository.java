package com.huuluc.chat_service.repository;

import com.huuluc.chat_service.model.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    @Query(value = "{\"participants\": { $all: ?0 }}")
    Optional<ChatRoom> existsByParticipants(List<String> participants);
}
