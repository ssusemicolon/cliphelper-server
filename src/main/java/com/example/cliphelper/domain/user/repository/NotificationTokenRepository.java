package com.example.cliphelper.domain.user.repository;

import com.example.cliphelper.domain.user.entity.NotificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationTokenRepository extends JpaRepository<NotificationToken, Long> {

    boolean existsByDeviceToken(String deviceToken);

    List<NotificationToken> findByUserId(Long userId);
}
