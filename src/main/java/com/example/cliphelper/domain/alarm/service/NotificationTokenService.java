package com.example.cliphelper.domain.alarm.service;

import com.example.cliphelper.domain.alarm.repository.NotificationTokenRepository;
import com.example.cliphelper.domain.user.repository.UserRepository;
import com.example.cliphelper.global.config.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationTokenService {
    private final UserRepository userRepository;
    private final NotificationTokenRepository notificationTokenRepository;
    private final SecurityUtils securityUtils;


}
