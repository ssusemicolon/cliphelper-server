package com.example.cliphelper.util;

import com.example.cliphelper.domain.article.entity.Article;
import com.example.cliphelper.domain.collection.entity.Collection;
import com.example.cliphelper.domain.user.entity.User;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionUtils {
    public static Collection newInstance() {
        String title = RandomStringUtils.random(6, true, true);

        return Collection.builder()
                .title(title)
                .description(null)
                .isPublic(true)
                .build();
    }

    public static List<Collection> newInstanceList(int collectionCount) {
        List<Collection> collectionList = new ArrayList<>();
        Set<String> titleSet = new HashSet<>();

        while (collectionList.size() < collectionCount) {
            final Collection collection = newInstance();
            if (titleSet.contains(collection.getTitle())) {
                continue;
            }

            collectionList.add(collection);
            titleSet.add(collection.getTitle());
        }

        return collectionList;
    }
}
