package com.example.cliphelper.global.config.security.dto;

import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDto {
    private String email;
    private String name;
    private String picture;

    @Builder
    public UserDto(String email, String name, String picture) {
        this.email = email;
        this.name = name;
        this.picture = picture;
    }

    public static UserDto from(OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String picture = oauth2User.getAttribute("picture");

        return UserDto.builder().email(email).name(name).picture(picture).build();

    }
}