package com.example.cliphelper.domain.alarm.dto;

import com.example.cliphelper.domain.alarm.entity.AlarmTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Builder
@Getter
@RequiredArgsConstructor
public class AlarmTimeResponseDto {
    private final Long alarmTimeId;

    @JsonFormat(pattern = "HH:mm")
    private final LocalTime time;

    public static AlarmTimeResponseDto of(AlarmTime alarmTime) {
        return AlarmTimeResponseDto.builder()
                .alarmTimeId(alarmTime.getId())
                .time(alarmTime.getTime())
                .build();
    }
}
