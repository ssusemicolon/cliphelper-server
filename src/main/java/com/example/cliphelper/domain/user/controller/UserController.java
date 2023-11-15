package com.example.cliphelper.domain.user.controller;

import javax.validation.Valid;

import com.example.cliphelper.domain.alarm.dto.AlarmTimeRequestDto;
import com.example.cliphelper.domain.alarm.dto.AlarmTimeResponseDto;
import com.example.cliphelper.domain.user.dto.DeviceTokenRequestDto;
import com.example.cliphelper.domain.user.service.NotificationTokenService;
import com.example.cliphelper.domain.user.dto.UserDetailedProfileResponseDto;
import com.example.cliphelper.domain.user.dto.UserModifyProfileRequestDto;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cliphelper.domain.user.dto.UserRequestDto;
import com.example.cliphelper.domain.user.dto.UserProfileResponseDto;
import com.example.cliphelper.domain.user.service.UserService;
import com.example.cliphelper.global.config.security.util.SecurityUtils;
import com.example.cliphelper.global.result.ResultCode;
import com.example.cliphelper.global.result.ResultResponse;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final NotificationTokenService notificationTokenService;
    private final SecurityUtils securityUtils;

    @PostMapping("/auth/signup")
    public ResultResponse join(@Valid @RequestBody UserRequestDto userRequestDto) {
        Long userId = userService.createUser(userRequestDto);
        return ResultResponse.of(ResultCode.USER_JOIN_SUCCESS);
    }

    @PostMapping("/users/deviceToken")
    public ResultResponse registerDevice(@Valid @RequestBody DeviceTokenRequestDto deviceTokenRequestDto) {
        notificationTokenService.registerNotificationToken(deviceTokenRequestDto);
        return ResultResponse.of(ResultCode.NOTIFICATION_TOKEN_REGISTER_SUCCESS);
    }

    @GetMapping("/users")
    public ResultResponse findUser() {
        System.out.println("this is users");
        UserProfileResponseDto userProfileResponseDto = userService.findUser(securityUtils.getCurrentUserId());
        return ResultResponse.of(ResultCode.USER_FIND_SUCCESS, userProfileResponseDto);
    }

    /**
     * 스크랩 개수, 북마크 개수, 팔로워, 알림받을 시간 목록 등
     * 
     * @return
     */
    @GetMapping("/users/profile")
    public ResultResponse getUserProfile() {
        UserDetailedProfileResponseDto userDetailedProfileResponseDto = userService.getUserProfile();
        return ResultResponse.of(ResultCode.USER_PROFILE_FIND_SUCCESS, userDetailedProfileResponseDto);
    }

    @GetMapping("/users/alarms")
    public ResultResponse getUserAlarTimeList() {
        List<AlarmTimeResponseDto> alarmTimeResponseDtos = userService.getUserAlarmTimeList();
        return ResultResponse.of(ResultCode.ALARM_TIME_LIST_FIND_SUCCESS, alarmTimeResponseDtos);
    }

    @PatchMapping("/users/profile")
    public ResultResponse modifyUsername(@ModelAttribute @Valid UserModifyProfileRequestDto userModifyRequestDto) {
        userService.modifyProfile(userModifyRequestDto);
        return ResultResponse.of(ResultCode.USER_MODIFY_PROFILE_SUCCESS);
    }

    @PatchMapping("/users/alarms/setting")
    public ResultResponse changeNotificationsPreference(@RequestParam("status") boolean status) {
        userService.changeNotificationsPreference(status);
        return ResultResponse.of(ResultCode.ALLOW_NOTIFICATIONS_SUCCESS);
    }

    @PostMapping("/users/alarms")
    public ResultResponse addAlarmTime(@RequestParam("alarmTime") String alarmTimeStr) {
        userService.addAlarmTime(alarmTimeStr);
        return ResultResponse.of(ResultCode.ALARM_TIME_ADD_SUCCESS);
    }

    @PatchMapping("/users/alarms/{alarmTimeId}")
    public ResultResponse modifyAlarmTime(@PathVariable("alarmTimeId") Long alarmTimeId,
            @RequestBody AlarmTimeRequestDto alarmTimeRequestDto) {
        userService.modifyAlarmTime(alarmTimeId, alarmTimeRequestDto.getAlarmTime());
        return ResultResponse.of(ResultCode.ALARM_TIME_MODIFY_SUCCESS);
    }

    @DeleteMapping("/users/alarms/{alarmTimeId}")
    public ResultResponse deleteAlarmTime(@PathVariable("alarmTimeId") Long alarmTimeId) {
        userService.deleteAlarmTime(alarmTimeId);
        return ResultResponse.of(ResultCode.ALARM_TIME_DELETE_SUCCESS);
    }

    @DeleteMapping("/users")
    public ResultResponse deleteUser() {
        userService.deleteUser();
        return ResultResponse.of(ResultCode.USER_DELETE_SUCCESS);
    }
}
