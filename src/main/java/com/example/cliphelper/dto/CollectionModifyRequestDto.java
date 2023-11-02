package com.example.cliphelper.dto;

import com.example.cliphelper.entity.Collection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
public class CollectionModifyRequestDto {
    private String title;
    private String description;
    private Boolean isPublic;
}
