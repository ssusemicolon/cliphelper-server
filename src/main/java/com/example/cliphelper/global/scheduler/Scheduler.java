package com.example.cliphelper.global.scheduler;

import com.example.cliphelper.domain.article.entity.Article;
import com.example.cliphelper.domain.article.repository.ArticleRepository;
import com.example.cliphelper.domain.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;

@RequiredArgsConstructor
@Component
public class Scheduler {
    private final ArticleService articleService;
    private final ArticleRepository articleRepository;

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
    public void sendPushAlarm() {
        LocalTime now = LocalTime.now();
    }
}
