package com.example.cliphelper.domain.alarm.repository;

import com.example.cliphelper.domain.alarm.entity.AlarmTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface AlarmTimeRepository extends JpaRepository<AlarmTime, Long> {
    boolean existsByUserIdAndTime(Long userId, LocalTime time);

    List<AlarmTime> findAllByTimeIs(LocalTime time);
}
