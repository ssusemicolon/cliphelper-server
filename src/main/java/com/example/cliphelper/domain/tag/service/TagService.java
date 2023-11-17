package com.example.cliphelper.domain.tag.service;

import com.example.cliphelper.domain.article.entity.Article;
import com.example.cliphelper.domain.tag.entity.ArticleTag;
import com.example.cliphelper.domain.tag.entity.Tag;
import com.example.cliphelper.domain.tag.repository.ArticleTagRepository;
import com.example.cliphelper.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TagService {
    private final TagRepository tagRepository;
    private final ArticleTagRepository articleTagRepository;

    @Transactional
    public Long registerTag(String name) {
        Tag tag = tagRepository.findByName(name)
                .orElse(null);

        if (tag == null) {
            return tagRepository.save(new Tag(name)).getId();
        } else {
            return tag.getId();
        }
    }

    @Transactional
    public void registerTagInArticle(Article article, List<String> names) {
        Map<String, Tag> tagMap = tagRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        tag -> tag.getName(), tag -> tag));

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
