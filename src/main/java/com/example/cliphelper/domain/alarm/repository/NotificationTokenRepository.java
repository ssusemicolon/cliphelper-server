package com.example.cliphelper.domain.alarm.repository;

import com.example.cliphelper.domain.alarm.entity.NotificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTokenRepository extends JpaRepository<NotificationToken, Long> {
}
