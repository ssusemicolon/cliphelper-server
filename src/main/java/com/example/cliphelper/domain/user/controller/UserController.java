package com.example.cliphelper.domain.user.controller;

import com.example.cliphelper.domain.user.dto.UserModifyRequestDto;
import com.example.cliphelper.domain.user.dto.UserRequestDto;
import com.example.cliphelper.domain.user.dto.UserResponseDto;
import com.example.cliphelper.global.result.ResultCode;
import com.example.cliphelper.global.result.ResultResponse;
import com.example.cliphelper.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/auth/signup")
    public ResultResponse join(@Valid @RequestBody UserRequestDto userRequestDto) {
        Long userId = userService.createUser(userRequestDto);
        return ResultResponse.of(ResultCode.USER_JOIN_SUCCESS);
    }

    @GetMapping("/users")
    public ResultResponse findUser() {
        UserResponseDto userResponseDto = userService.findUser();
        return ResultResponse.of(ResultCode.USER_FIND_SUCCESS, userResponseDto);
    }

    /**
     * @param userId
     * @param userModifyRequestDto
     * ===추후 구현 예정===
     * 회원의 활동 정보를 반환한다.
     * 스크랩 개수, 북마크 개수, 팔로워, 알림받을 시간 목록 등
     * @return
     */
    /*
     * @GetMapping("/users/{userId}/profile")
     * public ResultResponse getUserProfile(@PathVariable("userId") Long userId) {
     * UserProfileResponseDto userProfileResponseDto = userService.getUserProfile(userId);
     * return ResultResponse.of(ResultCode.USER_FIND_SUCCESS, userProfileResponseDto);
     * }
     */

    @PatchMapping("/users/{userId}")
    public ResultResponse modifyUser(@PathVariable("userId") Long userId, @Valid @RequestBody UserModifyRequestDto userModifyRequestDto) {
        userService.modifyUser(userId, userModifyRequestDto);
        return ResultResponse.of(ResultCode.USER_MODIFY_SUCCESS);
    }

    @DeleteMapping("/users/{userId}")
    public ResultResponse deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return ResultResponse.of(ResultCode.USER_DELETE_SUCCESS);
    }


}
