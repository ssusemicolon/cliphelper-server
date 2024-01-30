package com.example.cliphelper.domain.user.repository;

import com.example.cliphelper.domain.user.entity.NotificationToken;
import com.example.cliphelper.domain.user.entity.User;
import com.example.cliphelper.util.UserUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NotificationTokenRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    NotificationTokenRepository notificationTokenRepository;

    @Test
    void 동일한_디바이스_토큰이_존재하면_true_리턴() {
        // given
        User user = UserUtils.newInstance();
        String deviceToken = "abcd123";
        NotificationToken notificationToken = new NotificationToken(deviceToken, user);

        userRepository.save(user);
        notificationTokenRepository.save(notificationToken);

        // when
        boolean result = notificationTokenRepository.existsByDeviceToken(deviceToken);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("파라미터로 전달 받은 deviceToken을 token으로 가진 NotificationToken 객체가 존재하지 않는다면 false를 리턴한다.")
    void 동일한_디바이스_토큰이_존재하지_않으면_false_리턴() {
        // given
        User user = UserUtils.newInstance();
        String deviceToken = "abcd123";
        NotificationToken notificationToken = new NotificationToken(deviceToken, user);

        userRepository.save(user);
        notificationTokenRepository.save(notificationToken);

        // when
        String dummyToken = "dummy123";
        boolean result = notificationTokenRepository.existsByDeviceToken(dummyToken);

        // then
        assertThat(result).isFalse();
    }
}