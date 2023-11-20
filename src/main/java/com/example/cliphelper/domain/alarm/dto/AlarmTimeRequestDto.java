package com.example.cliphelper.domain.alarm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class AlarmTimeRequestDto {
    @NotNull
    private String alarmTime;
}
