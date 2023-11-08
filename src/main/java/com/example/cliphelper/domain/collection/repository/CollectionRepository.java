package com.example.cliphelper.domain.collection.repository;

import com.example.cliphelper.domain.collection.entity.Collection;
import org.hibernate.type.TrueFalseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {
    List<Collection> findByUserId(Long userId);

//    @Query(value = "select * from collection where (user_id = :userId AND is_public = TRUE", nativeQuery = true)
//    List<Collection> findOtherCollections(@Param("userId") Long userId);
    List<Collection> findByUserIdNotAndIsPublicIsTrue(Long userId);

}
