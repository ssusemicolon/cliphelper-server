package com.example.cliphelper.global.config.security.util;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.example.cliphelper.global.config.security.dto.JwtDto;
import com.example.cliphelper.global.config.security.exception.JwtExpiredException;
import com.example.cliphelper.global.config.security.exception.JwtInvalidException;
import com.example.cliphelper.global.config.security.token.JwtAuthenticationToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil implements InitializingBean {

    public final static String AUTHORIZATION_HEADER = "Authorization";
    private static final String CLAIM_AUTHORITIES_KEY = "authorities";
    private static final String CLAIM_JWT_TYPE_KEY = "type";
    private static final String CLAIM_MEMBER_ID_KEY = "memberId";

    private static final String BEARER_TYPE = "Bearer";
    private static final String BEARER_TYPE_PREFIX = "Bearer ";
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final int JWT_PREFIX_LENGTH = BEARER_TYPE_PREFIX.length();

    private Key JWT_KEY;

    @Value("${secret.jwt.signature}")
    private String JWT_STRING_KEY;

    @Value("${secret.jwt.access-token-validity-in-seconds}")
    private long ACCESS_TOKEN_VALIDATION_TIME_IN_SECONDS;

    @Value("${secret.jwt.refresh-token-validity-in-seconds}")
    private long REFRESH_TOKEN_VALIDATION_TIME_IN_SECONDS;

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] key = JWT_STRING_KEY.getBytes();
        JWT_KEY = Keys.hmacShaKeyFor(key);
    }

    public Claims parseClaims(String token) {
        try {
            JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(JWT_KEY).build();
            return jwtParser.parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException eje) {
            throw new JwtExpiredException();
        } catch (Exception e) {
            throw new JwtInvalidException();
        }
    }

    public Claims parseClaimsIgnoreExpiration(String token) {
        try {
            JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(JWT_KEY).build();
            return jwtParser.parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException eje) {
            return eje.getClaims();
        } catch (Exception e) {
            throw new JwtInvalidException();
        }
    }

    public String extractJwt(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        return this.extractJwt(authorizationHeader);
    }

    public String extractJwt(String authorizationHeader) {
        if (authorizationHeader == null) {
            throw new JwtInvalidException();
        }
        if (authorizationHeader.startsWith(BEARER_TYPE_PREFIX) == false) {
            throw new JwtInvalidException();
        }
        return authorizationHeader.substring(JWT_PREFIX_LENGTH);
    }

    public Authentication getAuthentication(String token) {
        final Claims claims = parseClaims(token);
        return getAuthentication(token, claims);
    }

    public Authentication getAuthenticationIgnoreExpiration(String token) {
        final Claims claims = parseClaimsIgnoreExpiration(token);
        return getAuthentication(token, claims);
    }

    private Authentication getAuthentication(String token, Claims claims) {
        final List<SimpleGrantedAuthority> authorities = Arrays.stream(
                claims.get(CLAIM_AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        final User principal = new User((String) claims.get(CLAIM_MEMBER_ID_KEY), "", authorities);
        return new JwtAuthenticationToken(principal, token, authorities);
    }

    private Date localDateTimeToDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    private JwtDto createJwt(String id, String authorities) {
        LocalDateTime currentTime = LocalDateTime.now();

        final Date accessTokenExpiresDate = localDateTimeToDate(
                currentTime.plusSeconds(ACCESS_TOKEN_VALIDATION_TIME_IN_SECONDS));
        final Date refreshTokenExpiresDate = localDateTimeToDate(
                currentTime.plusSeconds(REFRESH_TOKEN_VALIDATION_TIME_IN_SECONDS));

        final String accessToken = Jwts.builder()
                .setSubject(ACCESS_TOKEN_SUBJECT)
                .claim(CLAIM_MEMBER_ID_KEY, id)
                .claim(CLAIM_AUTHORITIES_KEY, authorities)
                .claim(CLAIM_JWT_TYPE_KEY, BEARER_TYPE)
                .setExpiration(accessTokenExpiresDate)
                .signWith(JWT_KEY, SignatureAlgorithm.HS512)
                .compact();

        final String refreshToken = Jwts.builder()
                .setSubject(REFRESH_TOKEN_SUBJECT)
                .claim(CLAIM_MEMBER_ID_KEY, id)
                .claim(CLAIM_AUTHORITIES_KEY, authorities)
                .setExpiration(refreshTokenExpiresDate)
                .signWith(JWT_KEY, SignatureAlgorithm.HS512)
                .compact();

        return JwtDto.builder()
                .type(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public JwtDto createJwt(Authentication authentication) {
        final String memberId = authentication.getName();
        final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return createJwt(memberId, authorities);
    }
}