package com.huuluc.chat_service.model;

import org.springframework.data.annotation.Id;

public class UserApp {
    @Id
    private String username;
    private String avatar;
}
