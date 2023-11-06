package com.example.cliphelper.domain.user.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cliphelper.domain.user.dto.UserResponseDto;
import com.example.cliphelper.domain.user.service.UserService;
import com.example.cliphelper.global.result.ResultCode;
import com.example.cliphelper.global.result.ResultResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AdminUserController {
    private final UserService userService;

    @GetMapping("/admin/users")
    public ResultResponse findAllUsers() {
        List<UserResponseDto> userResponseDtos = userService.findAllUsers();
        return ResultResponse.of(ResultCode.ALL_USERS_FIND_SUCCESS, userResponseDtos);
    }

    // finUser(Long userId)를 findUser(void)로 바꿔놔가지고, 잠시 주석 처리해놓는다.
    /*
     * @GetMapping("/users/{userId}")
     * public ResultResponse findUser(@PathVariable("userId") Long userId) {
     *
     * UserResponseDto userResponseDto = userService.findUser(userId);
     * return ResultResponse.of(ResultCode.USER_FIND_SUCCESS, userResponseDto);
     * }
     */
}
