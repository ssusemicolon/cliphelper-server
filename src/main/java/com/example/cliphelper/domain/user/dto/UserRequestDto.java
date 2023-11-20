package com.example.cliphelper.domain.user.dto;

import com.example.cliphelper.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UserRequestDto {
    @Email(message = "이메일을 필수로 입력해야 합니다.")
    private String email;

    @NotBlank(message = "닉네임을 필수로 입력해야 합니다.")
    private String username;

    private String picture;

    public User toEntity() {
        return User.builder()
                .id(null)
                .email(email)
                .username(username)
                .picture(picture)
                .enableNotifications(false)
                .build();
    }
}
