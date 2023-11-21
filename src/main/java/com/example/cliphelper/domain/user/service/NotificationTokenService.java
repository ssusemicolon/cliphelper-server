package com.example.cliphelper.domain.user.service;

import com.example.cliphelper.domain.user.dto.NotificationTokenRequestDto;
import com.example.cliphelper.domain.user.dto.NotificationTokenResponseDto;
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

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class NotificationTokenService {
    private final UserRepository userRepository;
    private final NotificationTokenRepository notificationTokenRepository;
    private final SecurityUtils securityUtils;

    @Transactional
    public void registerNotificationToken(NotificationTokenRequestDto notificationTokenRequestDto) {
        User user = userRepository.findById(securityUtils.getCurrentUserId())
                        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        String deviceToken = notificationTokenRequestDto.getDeviceToken();

        // 이미 동일한 device_token을 가진 NotificationToken 엔티티가 있는지 확인
        if (notificationTokenRepository.existsByDeviceToken(deviceToken) == true) {
            // 있는 경우: 함수 종료
            return;
        }

        // 없는 경우: 해당 토큰 저장
        NotificationToken notificationToken = new NotificationToken(deviceToken, user);
        notificationTokenRepository.save(notificationToken);
    }

    public List<NotificationTokenResponseDto> findNotificationTokensByUserId(Long userId) {
        List<NotificationToken> notificationTokens = notificationTokenRepository.findByUserId(userId);

        return notificationTokens
                .stream()
                .map(notificationToken -> NotificationTokenResponseDto.of(notificationToken))
                .collect(Collectors.toList());
    }
}
