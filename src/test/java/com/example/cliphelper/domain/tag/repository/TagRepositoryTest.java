package com.example.cliphelper.domain.tag.repository;

import com.example.cliphelper.domain.tag.entity.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TagRepositoryTest {
    @Autowired
    TagRepository tagRepository;

    @Test
    void 태그명으로_Tag_객체_조회() throws Exception {
        String name = "JUnit";
        // given
        Tag tag = new Tag(name);

        tagRepository.save(tag);

        // when
        Tag result = tagRepository.findByName(name)
                .orElse(null);

        // then
        assertThat(result).isEqualTo(tag);
    }
}