package com.example.cliphelper.domain.user.dto;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserModifyProfileRequestDto {
    private MultipartFile picture;

    @NotBlank
    private String username;
}
