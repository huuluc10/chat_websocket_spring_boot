package com.huuluc.chat_service.repository;

import com.huuluc.chat_service.model.UserApp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAppRepository extends MongoRepository<UserApp, String> {
    UserApp findByUsername(String username);
}
