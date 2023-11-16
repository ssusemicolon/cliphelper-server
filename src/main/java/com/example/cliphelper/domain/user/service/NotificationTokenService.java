package com.example.cliphelper.domain.user.service;

import com.example.cliphelper.domain.user.dto.DeviceTokenRequestDto;
import com.example.cliphelper.domain.user.entity.NotificationToken;
import com.example.cliphelper.domain.user.repository.NotificationTokenRepository;
import com.example.cliphelper.domain.user.entity.User;
import com.example.cliphelper.domain.user.repository.UserRepository;
import com.example.cliphelper.global.config.security.util.SecurityUtils;
import com.example.cliphelper.global.error.ErrorCode;
import com.example.cliphelper.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class NotificationTokenService {
    private final UserRepository userRepository;
    private final NotificationTokenRepository notificationTokenRepository;
    private final SecurityUtils securityUtils;

    @Transactional
    public void registerNotificationToken(DeviceTokenRequestDto deviceTokenRequestDto) {
        User user = userRepository.findById(securityUtils.getCurrentUserId())
                        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        NotificationToken notificationToken = new NotificationToken(
                deviceTokenRequestDto.getDeviceToken(),
                user);
        notificationTokenRepository.save(notificationToken);
    }
}
