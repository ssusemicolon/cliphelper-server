package com.example.cliphelper.domain.collection.repository;

import com.example.cliphelper.domain.collection.entity.Collection;
import com.example.cliphelper.domain.user.entity.User;
import com.example.cliphelper.domain.user.repository.UserRepository;
import com.example.cliphelper.util.CollectionUtils;
import com.example.cliphelper.util.UserUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CollectionRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CollectionRepository collectionRepository;
    @Test
    void 특정_userId를_컬럼으로_가진_collection_객체_리스트_조회() throws Exception {
        // given
        User user = UserUtils.newInstance();
        List<Collection> collectionList = CollectionUtils.newInstanceList(5);
        for (Collection collection : collectionList) {
            collection.setUser(user);
        }

        userRepository.save(user);
        collectionRepository.saveAll(collectionList);

        // when
        List<Collection> result = collectionRepository.findByUserId(user.getId());

        // then
        assertThat(result.size()).isEqualTo(collectionList.size());
        for (Collection collection : collectionList) {
            assertThat(result).contains(collection);
        }
    }

    @Test
    void 특정_userId와_다른_값을_컬럼으로_가지고_isPublic_값이_true인_collection_객체_리스트_조회() throws Exception {
        // given
        User userA = UserUtils.newInstance();
        User userB = UserUtils.newInstance();
        List<Collection> collectionListOfUserA = CollectionUtils.newInstanceList(5);
        List<Collection> collectionListOfUserB = CollectionUtils.newInstanceList(5);
        Collection privateCollectionOfUserB = collectionListOfUserB.get(1);
        // isPublic 값을 false로 변경
        privateCollectionOfUserB.changeInfo(
                privateCollectionOfUserB.getTitle(),
                privateCollectionOfUserB.getDescription(),
                false);

        userRepository.save(userA);
        userRepository.save(userB);
        collectionRepository.saveAll(collectionListOfUserA);
        collectionRepository.saveAll(collectionListOfUserB);

        // when
        List<Collection> result = collectionRepository.findByUserIdNotAndIsPublicIsTrue(userA.getId());

        // then
        for (Collection collection : result) {
            assertThat(collection.getUser().getId()).isNotEqualTo(userA.getId());
            assertThat(collection.isPublic()).isTrue();
        }
    }
}