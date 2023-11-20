package com.example.cliphelper.domain.user.entity;

import com.example.cliphelper.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@Entity
public class NotificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_token_id")
    private Long id;

    @Column(name = "device_token")
    private String deviceToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public NotificationToken(String deviceToken, User user) {
        this.id = null;
        this.deviceToken = deviceToken;
        this.user =user;
    }
}
