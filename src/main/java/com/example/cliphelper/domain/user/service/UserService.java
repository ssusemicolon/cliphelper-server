package com.example.cliphelper.domain.user.service;

import com.example.cliphelper.domain.alarm.dto.AlarmTimeResponseDto;
import com.example.cliphelper.domain.alarm.entity.AlarmTime;
import com.example.cliphelper.domain.alarm.service.AlarmTimeService;
import com.example.cliphelper.domain.article.entity.Article;
import com.example.cliphelper.domain.user.dto.UserModifyProfileRequestDto;
import com.example.cliphelper.domain.user.dto.UserDetailedProfileResponseDto;
import com.example.cliphelper.domain.user.dto.UserRequestDto;
import com.example.cliphelper.domain.user.dto.UserProfileResponseDto;
import com.example.cliphelper.domain.user.entity.User;
import com.example.cliphelper.domain.user.repository.UserRepository;
import com.example.cliphelper.global.config.security.util.SecurityUtils;
import com.example.cliphelper.global.error.ErrorCode;
import com.example.cliphelper.global.error.exception.EntityNotFoundException;
import com.example.cliphelper.global.service.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final FileService fileService;
    private final AlarmTimeService alarmTimeService;
    private final SecurityUtils securityUtils;
    private final UserRepository userRepository;

    @Transactional
    public Long createUser(UserRequestDto userRequestDto) {
        User user = userRequestDto.toEntity();

        return userRepository.save(user).getId();
    }

    public List<UserProfileResponseDto> findAllUsers() {
        List<User> users = userRepository.findAll();

        return users
                .stream()
                .map(user -> UserProfileResponseDto.of(user))
                .collect(Collectors.toList());
    }

    public UserProfileResponseDto findUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        return UserProfileResponseDto.of(user);
    }

    public UserDetailedProfileResponseDto getUserProfile() {
        User user = userRepository.findById(securityUtils.getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        return UserDetailedProfileResponseDto.of(user);
    }

    public List<AlarmTimeResponseDto> getUserAlarmTimeList() {
        User user = userRepository.findById(securityUtils.getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        return user.getAlarmTimeList()
                .stream()
                .map(alarmTime -> AlarmTimeResponseDto.of(alarmTime))
                .collect(Collectors.toList());
    }

    @Transactional
    public void modifyProfile(UserModifyProfileRequestDto userModifyProfileRequestDto) {
        User user = userRepository.findById(securityUtils.getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        user.changeUsername(userModifyProfileRequestDto.getUsername());

        final MultipartFile modifiedPicture = userModifyProfileRequestDto.getPicture();
        if (modifiedPicture != null) {
            log.info("upload file: {}", modifiedPicture.getName());
            final String uuid = UUID.randomUUID().toString();
            final String fileUrl = fileService.uploadFile(modifiedPicture, uuid);

            user.changePicture(fileUrl);
        }
    }

    @Transactional
    public void deleteUser() {
        User user = userRepository.findById(securityUtils.getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        List<Article> articles = user.getArticles();
        articles.forEach(article -> {
            if (article.getFileUrl() != null) {
                fileService.deleteFile(article.getTitle());
            }
        });

        userRepository.deleteById(securityUtils.getCurrentUserId());
    }

    @Transactional
    public void changeNotificationsPreference(boolean status) {
        User user = userRepository.findById(securityUtils.getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        user.changeEnableNotifications(status);
    }

    @Transactional
    public void addAlarmTime(String alarmTimeStr) {
        User user = userRepository.findById(securityUtils.getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        int hour = convertToHour(alarmTimeStr);
        int minute = convertToMinute(alarmTimeStr);

        LocalTime alarmTime = LocalTime.of(hour, minute);
        alarmTimeService.addAlarmTime(user, alarmTime);
    }

    // 수정하려는 AlarmTime의 주인이 맞는지 인가 과정이 필요한가?
    @Transactional
    public void modifyAlarmTime(Long alarmTimeId, String alarmTimeStr) {
        int hour = convertToHour(alarmTimeStr);
        int minute = convertToMinute(alarmTimeStr);

        alarmTimeService.modifyAlarmTime(alarmTimeId, hour, minute);
    }

    private int convertToHour(String alarmTimeStr) {
        return Integer.parseInt(alarmTimeStr.split(":")[0]);
    }

    private int convertToMinute(String alarmTimeStr) {
        return Integer.parseInt(alarmTimeStr.split(":")[1]);
    }

    @Transactional
    public void deleteAlarmTime(Long alarmTimeId) {
        alarmTimeService.deleteAlarmTime(alarmTimeId);
    }

    // 특정 시간대를 알람 희망 시간대로 설정한 유저 조회
    public List<User> findUsersByAlarmTime(LocalTime time) {
        return userRepository.findByAlarmTime(time);
    }
}
