package com.example.cliphelper.domain.user.repository;

import com.example.cliphelper.domain.alarm.entity.AlarmTime;
import com.example.cliphelper.domain.alarm.repository.AlarmTimeRepository;
import com.example.cliphelper.domain.user.entity.User;
import com.example.cliphelper.util.UserUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AlarmTimeRepository alarmTimeRepository;

    @Test
    void 특정_email을_가진_회원_조회() throws Exception {
        // given
        User user = UserUtils.newInstance();

        userRepository.save(user);

        // when
        User result = userRepository.findByEmail(user.getEmail())
                .orElse(null);

        // then
        assertThat(result).isEqualTo(user);
    }

    @Test
    @DisplayName("findByEmail() 호출 시, 파라미터로 전달 받은 email을 가진 User가 없을 경우 Optional.empty()를 리턴한다.")
    void 특정_email을_가진_회원이_존재하지_않는_경우_() {
        List<User> userList = UserUtils.newInstanceList(3);
        Set<String> emailSet = new HashSet<>();
        for (User user : userList) {
            emailSet.add(user.getEmail());
        }
        userRepository.saveAll(userList);

        // DB에 저장된 회원들 중 누구와도 일치하지 않는 이메일 생성
        String targetEmail = RandomStringUtils.random(6, true, true) + "@naver.com";
        while (emailSet.contains(targetEmail)) {
            targetEmail = RandomStringUtils.random(6, true, true) + "@naver.com";
        }

        // when
        Optional<User> result = userRepository.findByEmail(targetEmail);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void 특정_알람시간대를_설정해_놓은_회원_조회() throws Exception {
        // given
        User userA = UserUtils.newInstance();
        User userB = UserUtils.newInstance();
        User userC = UserUtils.newInstance();

        LocalTime timeForUserAAndUserB = LocalTime.of(20, 10);
        LocalTime timeForUserC = LocalTime.of(22, 0);

        AlarmTime alarmTimeOfUserA = AlarmTime.builder()
                .time(timeForUserAAndUserB)
                .user(userA)
                .build();

        AlarmTime alarmTimeOfUserB = AlarmTime.builder()
                .time(timeForUserAAndUserB)
                .user(userB)
                .build();

        AlarmTime alarmTimeOfUserC = AlarmTime.builder()
                .time(timeForUserC)
                .user(userC)
                .build();


        userRepository.save(userA);
        userRepository.save(userB);
        userRepository.save(userC);

        alarmTimeRepository.save(alarmTimeOfUserA);
        alarmTimeRepository.save(alarmTimeOfUserB);
        alarmTimeRepository.save(alarmTimeOfUserC);

        // when
        List<User> result = userRepository.findByAlarmTime(timeForUserAAndUserB);

        // then
        assertThat(result).contains(userA);
        assertThat(result).contains(userB);
        assertThat(result).doesNotContain(userC);
    }
}