package com.example.cliphelper.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class UserModifyPictureRequestDto {
    @NotNull
    private String picture;
}
