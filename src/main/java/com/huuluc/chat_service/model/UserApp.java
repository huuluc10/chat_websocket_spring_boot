package com.huuluc.chat_service.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Builder
@Document(collection = "user")
public class UserApp {
    @Id
    private String username;
    private String avatar;
}
