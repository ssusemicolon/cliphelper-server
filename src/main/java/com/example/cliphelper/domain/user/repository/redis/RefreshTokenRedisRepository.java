package com.example.cliphelper.domain.user.repository.redis;

import org.springframework.data.repository.CrudRepository;

import com.example.cliphelper.domain.user.entity.redis.RefreshToken;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
    List<RefreshToken> findAllByMemberId(Long memberId);

    Optional<RefreshToken> findByMemberIdAndValue(Long memberId, String value);

    Optional<RefreshToken> findByMemberIdAndId(Long memberId, String id);
}
