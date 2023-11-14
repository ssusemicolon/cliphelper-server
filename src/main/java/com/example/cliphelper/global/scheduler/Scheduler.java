package com.example.cliphelper.global.scheduler;

import com.example.cliphelper.domain.alarm.dto.NotificationRequestDto;
import com.example.cliphelper.domain.alarm.repository.AlarmTimeRepository;
import com.example.cliphelper.domain.article.entity.Article;
import com.example.cliphelper.domain.article.repository.ArticleRepository;
import com.example.cliphelper.domain.article.service.ArticleService;
import com.example.cliphelper.domain.user.entity.User;
import com.example.cliphelper.global.service.FCMService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Scheduler {
    private final ArticleService articleService;
    private final FCMService fcmService;
    private final ArticleRepository articleRepository;
    private final AlarmTimeRepository alarmTimeRepository;

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
        List<Article> articles = articleRepository.findAll();

        articles.forEach(article -> {
            LocalDate now = LocalDate.now();
            LocalDate recentAccessTime = article.getRecentAccessTime().toLocalDate();
            Period elapsedTimeSinceAccess = Period.between(recentAccessTime, now);
            if (elapsedTimeSinceAccess.getDays() >= 15) {
                articleService.deleteArticle(article.getId());
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
        // 현재 시간을 조회 후, 초와 나노초는 0으로 변경한 시간대를 리턴하는 함수 호출
        LocalTime now = getCurrentHourAndMinuteToLocalTime();

        // 현재 시간대를 알람 희망 시간대로 설정한 유저 조회
        // 단, 회원이 알람을 받지 않겠다고 설정한 경우 알림 전송 대상에서 제외한다
        // 단, 등록한 아티클이 없는 유저는 필터하여 알림 전송 대상에서 제외한다
        List<User> users = alarmTimeRepository.findAllByTimeIs(now)
                .stream()
                .map(alarmTime -> alarmTime.getUser())
                .filter(user -> user.isEnableNotifications() && user.getArticles().size() > 0)
                .collect(Collectors.toList());


        // 현재 유저가 여러 개의 디바이스를 가질 수 잇는데, 그 중 0번째 디바이스에게만 알림 보내도록 설정
        List<NotificationRequestDto> notificationRequestDtos = users
                .stream()
                .filter(user -> user.getNotificationTokens().size() > 0)
                .map(user -> NotificationRequestDto.of(
                        user.getNotificationTokens().get(0).getDeviceToken(),
                        // 해당 유저의 아티클들 중 추천할 아티클을 선택하는 알고리즘
                        articleService.findOldestUnseenArticle(user.getId()),
                        user))
                .collect(Collectors.toList());


        // 선택한 알고리즘을 바탕으로, 유저의 디바이스 토큰과 함께 묶어 Message 객체 생성 후 send
        notificationRequestDtos.forEach(notificationRequestDto ->
                fcmService.sendArticleRecommendationNotification(notificationRequestDto));
    }

    private LocalTime getCurrentHourAndMinuteToLocalTime() {
        LocalTime now = LocalTime.now();
        return now.withSecond(0).withNano(0);
    }
}
