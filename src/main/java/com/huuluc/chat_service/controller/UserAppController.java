package com.huuluc.chat_service.controller;

import com.huuluc.chat_service.model.UserApp;
import com.huuluc.chat_service.service.UserAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class UserAppController {
    private final UserAppService userAppService;

    @PostMapping("/user")
    public UserApp createUserApp(@RequestBody UserApp user) {
        log.info("Create user: {}", user.getUsername());
        return userAppService.createUserApp(user);
    }

    @GetMapping("/user/{userId}")
    public UserApp getUserApp(@PathVariable String userId) {
        return userAppService.getUserApp(userId);
    }

    @GetMapping("/user")
    public List<UserApp> getAllUserApp() {
        return userAppService.getAllUserApp();
    }
}
