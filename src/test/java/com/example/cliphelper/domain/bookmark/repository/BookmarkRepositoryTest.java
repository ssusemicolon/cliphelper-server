package com.example.cliphelper.domain.bookmark.repository;

import com.example.cliphelper.domain.article.repository.ArticleRepository;
import com.example.cliphelper.domain.bookmark.entity.Bookmark;
import com.example.cliphelper.domain.collection.entity.Collection;
import com.example.cliphelper.domain.collection.repository.CollectionRepository;
import com.example.cliphelper.domain.user.entity.User;
import com.example.cliphelper.domain.user.repository.UserRepository;
import com.example.cliphelper.util.CollectionUtils;
import com.example.cliphelper.util.UserUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookmarkRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookmarkRepository bookmarkRepository;
    @Autowired
    CollectionRepository collectionRepository;

    @Test
    @DisplayName("deleteBookmarkByCollectionIdAndUserId() 함수를 호출하여 파라미터로 전달 받은 userId를 가진 회원이 collectionId를 가진 컬렉션을 북마크한 행위를 해제한다.")
    void 컬렉션_아이디와_유저_아이디를_이용해_북마크_해제() {
        // given
        User userA = UserUtils.newInstance();
        User userB = UserUtils.newInstance();

        Collection collection = CollectionUtils.newInstance();
        collection.setUser(userA);
        Bookmark bookmark = new Bookmark(userB, collection);

        userRepository.save(userA);
        userRepository.save(userB);
        collectionRepository.save(collection);
        bookmarkRepository.save(bookmark);

        // when
        bookmarkRepository.deleteBookmarkByCollectionIdAndUserId(collection.getId(), userB.getId());

        // then
        Optional<Bookmark> result = bookmarkRepository.findById(bookmark.getId());
        assertThat(result).isEmpty();
    }
}