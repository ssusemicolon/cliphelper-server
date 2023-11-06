package com.example.cliphelper.global.config.security.dto;

import java.util.Map;

import com.example.cliphelper.global.config.security.token.OAuthLoginToken;

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

    public static UserDto from(OAuthLoginToken oauth2User) {
        Map<String, Object> attributes = (Map<String, Object>) oauth2User.getPrincipal();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");

        return UserDto.builder().email(email).name(name).picture(picture).build();

    }
}