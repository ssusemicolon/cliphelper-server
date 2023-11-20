package com.example.cliphelper.domain.user.service.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cliphelper.domain.user.entity.redis.RefreshToken;
import com.example.cliphelper.domain.user.repository.redis.RefreshTokenRedisRepository;
import com.example.cliphelper.global.config.security.exception.JwtInvalidException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Transactional
    public void addRefreshToken(Long memberId, String tokenValue) {
        final RefreshToken refreshToken = RefreshToken.builder()
                .memberId(memberId)
                .value(tokenValue)
                .build();

        refreshTokenRedisRepository.save(refreshToken);
    }

    @Transactional(readOnly = true)
    public Optional<RefreshToken> findRefreshToken(Long memberId, String value) {
        return refreshTokenRedisRepository.findByMemberIdAndValue(memberId, value);
    }

    @Transactional
    public void deleteRefreshToken(RefreshToken refreshToken) {
        refreshTokenRedisRepository.delete(refreshToken);
    }

    @Transactional
    public void deleteRefreshTokenByValue(Long memberId, String value) {
        final RefreshToken refreshToken = refreshTokenRedisRepository.findByMemberIdAndValue(memberId, value)
                .orElseThrow(JwtInvalidException::new);
        refreshTokenRedisRepository.delete(refreshToken);
    }

    @Transactional
    public void deleteRefreshTokenByMemberIdAndId(Long memberId, String id) {
        final RefreshToken refreshToken = refreshTokenRedisRepository.findByMemberIdAndId(memberId, id)
                .orElseThrow(JwtInvalidException::new);
        refreshTokenRedisRepository.delete(refreshToken);
    }

}
