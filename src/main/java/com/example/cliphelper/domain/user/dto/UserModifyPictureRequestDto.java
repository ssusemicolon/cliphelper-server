package com.example.cliphelper.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@NoArgsConstructor
public class UserModifyPictureRequestDto {
    private MultipartFile picture;
}
