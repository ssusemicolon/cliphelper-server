package com.example.cliphelper.domain.alarm.service;

import com.example.cliphelper.domain.alarm.entity.AlarmTime;
import com.example.cliphelper.domain.alarm.repository.AlarmTimeRepository;
import com.example.cliphelper.domain.user.entity.User;
import com.example.cliphelper.global.error.BusinessException;
import com.example.cliphelper.global.error.ErrorCode;
import com.example.cliphelper.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AlarmTimeService {
    private final AlarmTimeRepository alarmTimeRepository;

    @Transactional
    public void addAlarmTime(User user, LocalTime alarmTime) {
        if (alarmTimeRepository.existsByUserIdAndTimeIs(user.getId(), alarmTime)) {
            throw new BusinessException(ErrorCode.ALARM_TIME_ALREADY_EXISTS);
        }
        alarmTimeRepository.save(new AlarmTime(alarmTime, user));
    }

    public AlarmTime getAlarmTime(Long alarmTimeId) {
        return alarmTimeRepository.findById(alarmTimeId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ALARM_TIME_NOT_FOUND));
    }

    @Transactional
    public void modifyAlarmTime(Long alarmTimeId, int hour, int minute) {
        AlarmTime alarmTime = getAlarmTime(alarmTimeId);
        alarmTime.changeTime(hour, minute);

        alarmTimeRepository.flush();
    }

    // 무슨 값을 전달받는가?
    @Transactional
    public void deleteAlarmTime(Long alarmTimeId) {
        if (!alarmTimeRepository.existsById(alarmTimeId)) {
            throw new EntityNotFoundException(ErrorCode.ALARM_TIME_NOT_FOUND);
        }

        alarmTimeRepository.deleteById(alarmTimeId);
    }
}
