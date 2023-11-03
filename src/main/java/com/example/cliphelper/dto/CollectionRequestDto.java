package com.example.cliphelper.dto;

import com.example.cliphelper.entity.Collection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
public class CollectionRequestDto {
    @NotBlank
    private String title;

    private String description;

    @NotNull
    private boolean isPublic;

    private List<Long> articles;

    public Collection toEntity() {
        return new Collection(title, description, isPublic);
    }
}
