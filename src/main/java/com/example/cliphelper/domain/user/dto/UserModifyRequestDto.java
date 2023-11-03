package com.example.cliphelper.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UserModifyRequestDto {
    @Email(message = "이메일 형식의 문자열을 입력해야 합니다.")
    private String email;
    private String password;
    private String username;
}