package com.example.cliphelper.service;

import com.example.cliphelper.entity.Article;
import com.example.cliphelper.entity.ArticleTag;
import com.example.cliphelper.entity.Tag;
import com.example.cliphelper.repository.ArticleTagRepository;
import com.example.cliphelper.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TagService {
    private final TagRepository tagRepository;
    private final ArticleTagRepository articleTagRepository;

    // 태그는 모든 사용자가 공유할 수 있다고 가정한다. (인스타그램의 해시태그처럼)
    public Long registerTag(String name) {
        Tag tag = tagRepository.findByName(name)
                .orElse(null);

        if (tag == null) {
            return tagRepository.save(new Tag(name)).getId();
        } else {
            return tag.getId();
        }
    }

    public void registerTagInArticle(Article article, List<String> names) {
        Map<String, Tag> tagMap = tagRepository.findAll().stream()
                .collect(Collectors.toMap(Tag::getName, tag -> tag));

        names.forEach(name -> {
            final Tag tag;
            if (tagMap.containsKey(name)) {
                tag = tagMap.get(name);
            } else {
                tag = tagRepository.save(new Tag(name));
            }

            articleTagRepository.save(new ArticleTag(article, tag));
        });
    }
}
