package com.example.cliphelper.domain.collection.dto;

import com.example.cliphelper.domain.collection.entity.Collection;
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
        return Collection.builder()
                .title(title)
                .description(description)
                .isPublic(isPublic)
                .build();
//        return new Collection(title, description, isPublic);
    }
}
