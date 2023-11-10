package com.example.cliphelper.domain.user.service;

import com.example.cliphelper.domain.alarm.dto.AlarmTimeResponseDto;
import com.example.cliphelper.domain.alarm.entity.AlarmTime;
import com.example.cliphelper.domain.alarm.service.AlarmTimeService;
import com.example.cliphelper.domain.article.entity.Article;
import com.example.cliphelper.domain.user.dto.UserModifyRequestDto;
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
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final FileService fileService;
    private final AlarmTimeService alarmTimeService;
    private final SecurityUtils securityUtils;
    private final UserRepository userRepository;

    public Long createUser(UserRequestDto userRequestDto) {
        User user = userRequestDto.toEntity();
        Long userId = userRepository.save(user)
                .getId();

        return userId;
    }

    public List<UserProfileResponseDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserProfileResponseDto> userProfileResponseDtos = new ArrayList<>();
        for (User user : users) {
            userProfileResponseDtos.add(UserProfileResponseDto.of(user));
        }

        return userProfileResponseDtos;
    }

    public UserProfileResponseDto findUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        return UserProfileResponseDto.of(user);
    }

    public UserDetailedProfileResponseDto getUserProfile() {
        User user = userRepository.findById(securityUtils.getCurrentUserId())
                .orElseThrow(() -> new RuntimeException("해당 userId를 가진 회원이 존재하지 않습니다."));

        int followerCount = user.getCollections().stream()
                .map(collection -> collection.getBookmarks().size())
                .mapToInt(Integer::intValue).sum();

        return UserDetailedProfileResponseDto.of(user, followerCount);
    }

    public List<AlarmTimeResponseDto> getUserAlarmTimeList() {
        User user = userRepository.findById(securityUtils.getCurrentUserId())
                .orElseThrow(() -> new RuntimeException("해당 userId를 가진 회원이 존재하지 않습니다."));

        List<AlarmTimeResponseDto> alarmTimeResponseDtos = user.getAlarmTimeList()
                .stream()
                .map(alarmTime -> AlarmTimeResponseDto.of(alarmTime))
                .collect(Collectors.toList());

        return alarmTimeResponseDtos;
    }

    public void modifyUser(UserModifyRequestDto userModifyRequestDto) {
        User user = userRepository.findById(securityUtils.getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        user.changeInfo(
                userModifyRequestDto.getEmail(),
                userModifyRequestDto.getUsername(),
                userModifyRequestDto.getPicture());

        userRepository.save(user);
    }

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

    public void changeNotificationsPreference(boolean status) {
        User user = userRepository.findById(securityUtils.getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        user.changeEnableNotifications(status);
        userRepository.flush();
    }

    public void addAlarmTime(String alarmTimeStr) {
        User user = userRepository.findById(securityUtils.getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        int hour = Integer.parseInt(alarmTimeStr.split(":")[0]);
        int minute = Integer.parseInt(alarmTimeStr.split(":")[1]);

        LocalTime alarmTime = LocalTime.of(hour, minute);
        alarmTimeService.addAlarmTime(user, alarmTime);
    }

    public void modifyAlarmTime(Long alarmTimeId, String alarmTimeStr) {
        int hour = Integer.parseInt(alarmTimeStr.split(":")[0]);
        int minute = Integer.parseInt(alarmTimeStr.split(":")[1]);

        alarmTimeService.modifyAlarmTime(alarmTimeId, hour, minute);
    }

    public void deleteAlarmTime(Long alarmTimeId) {
        alarmTimeService.deleteAlarmTime(alarmTimeId);
    }
}
