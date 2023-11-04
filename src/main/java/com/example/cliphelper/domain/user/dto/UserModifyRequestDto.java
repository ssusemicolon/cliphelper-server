package com.example.cliphelper.domain.user.dto;

import javax.validation.constraints.Email;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserModifyRequestDto {
    @Email(message = "이메일 형식의 문자열을 입력해야 합니다.")
    private String email;
    private String password;
    private String username;
}