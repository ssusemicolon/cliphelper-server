package com.example.cliphelper.domain.alarm.repository;

import com.example.cliphelper.domain.alarm.entity.AlarmTime;
import com.example.cliphelper.domain.user.entity.User;
import com.example.cliphelper.domain.user.repository.UserRepository;
import com.example.cliphelper.util.UserUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AlarmTimeRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AlarmTimeRepository alarmTimeRepository;

    @Test
    @DisplayName("existsByUserIdAndTime() 호출 시, 파라미터로 전달 받은 userId를 가진 회원이 파라미터로 전달 받은 해당 시간을 알람 시간대로 설정한 경우 true를 리턴한다.")
    void existsByUserIdAndTime_givenUncreatedUserId() {
        // given
        User user = UserUtils.newInstance();
        user.changeEnableNotifications(true);

        LocalTime time = LocalTime.of(15, 0);

        AlarmTime alarmTime = AlarmTime.builder()
                .id(null)
                .time(time)
                .user(user)
                .build();

        userRepository.save(user);
        alarmTimeRepository.save(alarmTime);

        // when
        Long wrongUserId = user.getId() + 1;
        LocalTime wrongTime = time.plusHours(1);

        boolean wrongUserIdResult = alarmTimeRepository.existsByUserIdAndTime(wrongUserId, time);
        boolean wrongTimeResult = alarmTimeRepository.existsByUserIdAndTime(user.getId(), wrongTime);
        boolean correctResult = alarmTimeRepository.existsByUserIdAndTime(user.getId(), time);

        // then
        assertThat(wrongUserIdResult).isFalse();
        assertThat(wrongTimeResult).isFalse();
        assertThat(correctResult).isTrue();
    }
}