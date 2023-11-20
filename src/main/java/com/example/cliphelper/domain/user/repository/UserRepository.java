package com.example.cliphelper.domain.user.repository;


import com.example.cliphelper.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query(value = "select user " +
            "from User user " +
            "join user.alarmTimeList alarmTime " +
            "where alarmTime.time = :time"
    )
    List<User> findByAlarmTime(LocalTime time);
}
