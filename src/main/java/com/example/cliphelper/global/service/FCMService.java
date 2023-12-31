package com.example.cliphelper.global.service;

import com.example.cliphelper.domain.alarm.dto.PushNotificationRequestDto;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.ApsAlert;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FCMService {
    private final FirebaseMessaging firebaseMessaging;

    @Transactional
    public void sendArticleRecommendationNotification(PushNotificationRequestDto requestDto) {
        Message message = Message.builder()
                .setToken(requestDto.getDeviceToken())
                .setNotification(requestDto.toNotification())
                .putData("link", String.format("cliphelper://article/detail/%d", requestDto.getArticleId()))
                .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            System.out.println("=========푸시 알림 전송 과정 예외 발생=========");
            System.out.printf("e.getMessage: %s\n", e.getMessage());
        }
    }
}
