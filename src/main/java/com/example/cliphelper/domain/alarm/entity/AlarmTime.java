package com.example.cliphelper.domain.alarm.entity;

import com.example.cliphelper.domain.collection.entity.Collection;
import com.example.cliphelper.domain.user.entity.User;
import lombok.Builder;
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
public class AlarmTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_time_id")
    private Long id;

    @Column(name = "time")
    private LocalTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public AlarmTime(Long id, LocalTime time, User user) {
        this.id = id;
        this.time = time;
        this.user =user;
    }

    public void changeTime(int hour, int minute) {
        this.time = this.time
                .withHour(hour)
                .withMinute(minute);
    }

}
