package com.huuluc.chat_service.service;

import com.huuluc.chat_service.model.UserApp;
import com.huuluc.chat_service.repository.UserAppRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAppService {
    private final UserAppRepository userAppRepository;

    public boolean isUserAppExist(String userId) {
        return userAppRepository.existsById(userId);
    }

    public UserApp createUserApp(UserApp user) {
        return userAppRepository.save(user);
    }

    public UserApp updateUserApp(UserApp user) {
        return userAppRepository.save(user);
    }

    public UserApp getUserApp(String username) {
        return userAppRepository.findById(username).orElse(null);
    }

    public List<UserApp> getAllUserApp() {
        return userAppRepository.findAll();
    }

    public int updateAvatar(UserApp newData) {
        String username = newData.getUsername();

        UserApp userInDB = userAppRepository.findById(username).orElse(null);

        if (userInDB == null) {
            return -1;
        }

        if (!newData.getAvatar().equals(userInDB.getAvatar())) {
            userAppRepository.deleteById(username);
            userAppRepository.save(newData);
            return 1;
        }

        return -1;
    }
}
