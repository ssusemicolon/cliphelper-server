package com.example.cliphelper.global.scheduler;

import com.example.cliphelper.domain.alarm.dto.PushNotificationRequestDto;
import com.example.cliphelper.domain.article.dto.ArticleResponseDto;
import com.example.cliphelper.domain.article.service.ArticleService;
import com.example.cliphelper.domain.user.entity.User;
import com.example.cliphelper.domain.user.service.NotificationTokenService;
import com.example.cliphelper.domain.user.service.UserService;
import com.example.cliphelper.global.service.FCMService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Scheduler {
    private final UserService userService;
    private final ArticleService articleService;
    private final FCMService fcmService;
    private final NotificationTokenService notificationTokenService;

    /**
     * 스케줄러 실행 주기: 매일 자정(00시 00분)
     * 스케줄러의 기능
     * - 아티클의 최근 접속 시간이 15일이 지난 경우, 해당 아티클을 삭제한다.
     * 사용하는 클래스
     * - LocalDate
     * - Period
     */
    @Scheduled(cron = "${schedules.cron.article.cleanup}", zone = "Asia/Seoul")
    public void autoCleanupArticleByLRU() {
        System.out.println("==========LRU 아티클 삭제 스케줄러 동작 시작==========");
        List<ArticleResponseDto> articleResponseDtos = articleService.findAllArticles();

        articleResponseDtos.forEach(articleResponseDto -> {
            LocalDate now = LocalDate.now();
            LocalDate recentAccessTime = articleResponseDto.getRecentAccessTime().toLocalDate();

            Period elapsedTimeSinceAccess = Period.between(recentAccessTime, now);
            if (elapsedTimeSinceAccess.getDays() >= 15) {
                articleService.deleteArticle(articleResponseDto.getArticleId());
            }
        });
    }

    /**
     *   10분마다 해당하는 사용자들에게 알람을 보내는 스케줄러
     *   1. now(ㅁㅁ시 ㅁㅁ분)를 희망 시간대로 설정한 유저를 조회하는 쿼리 실행
     *   2. 각 유저별로 추천할 아티클 선정(어떻게든 선정)
     *   3. 해당 아티클을 유저에게 추천하는 푸시 알림 전송
     */
    @Scheduled(cron = "0 0/10 * * * ?", zone = "Asia/Seoul")
    public void sendPushNotification() {
        System.out.println("==================푸시 알림 스케줄러 동작=======================");
        // 현재 시간을 조회 후, 초와 나노초는 0으로 변경한 시간대를 리턴하는 함수 호출
        LocalTime now = getCurrentHourAndMinuteToLocalTime();

        // 현재 시간대를 알람 희망 시간대로 설정한 유저 조회
        // 단, 회원이 알람을 받겠다고 설정한 회원만 알림 전송 대상으로 한다.
        // 단, 등록한 아티클이 있는 회원만 알림 전송 대상으로 한다.
        List<User> users = userService.findUsersByAlarmTime(now)
                .stream()
                .filter(user -> user.isEnableNotifications() == true)
                .filter(user -> articleService.getArticleCountByUserId(user.getId()) > 0)
                .collect(Collectors.toList());

        // 테스트 코드
        users.forEach(
                user -> System.out.printf("알람시간 설정한 회원: %s\n", user.getUsername())
        );

        List<PushNotificationRequestDto> pushNotificationRequestDtos = users
                .stream()
                .map(user -> notificationTokenService.findNotificationTokensByUserId(user.getId()))
                .flatMap(Collection::stream)
                .map(notificationTokenResponseDto -> PushNotificationRequestDto.of(
                        notificationTokenResponseDto.getDeviceToken(),
                        articleService.findOldestUnseenArticle(notificationTokenResponseDto.getUser().getUserId()),
                        notificationTokenResponseDto.getUser().getUsername()))
                .collect(Collectors.toList());


        // 선택한 알고리즘을 바탕으로, 유저의 디바이스 토큰과 함께 묶어 Message 객체 생성 후 send
        pushNotificationRequestDtos.forEach(pushNotificationRequestDto ->
                fcmService.sendArticleRecommendationNotification(pushNotificationRequestDto));
    }

    private LocalTime getCurrentHourAndMinuteToLocalTime() {
        LocalTime now = LocalTime.now();
        return now.withSecond(0).withNano(0);
    }
}
