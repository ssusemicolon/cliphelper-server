package com.example.cliphelper.global.config.security.provider;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.example.cliphelper.global.config.security.exception.LoginFailedException;
import com.example.cliphelper.global.config.security.token.OAuthLoginToken;
import com.example.cliphelper.global.config.security.vo.GoogleOAuthResult;
import com.example.cliphelper.global.config.security.vo.KakaoOAuthResult;
import com.example.cliphelper.global.config.security.vo.OAuthType;
import com.example.cliphelper.global.service.GoogleFeignClient;
import com.example.cliphelper.global.service.KakaoFeignClient;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class LoginAuthenticationProvider implements AuthenticationProvider {
    private final GoogleFeignClient googleClient;
    private final KakaoFeignClient kakaoClient;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final Authentication authenticated = verify(authentication);

        if (authenticated == null) {
            throw new LoginFailedException();
        }

        return authenticated;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return OAuthLoginToken.class.isAssignableFrom(aClass);
    }

    private OAuthLoginToken verify(Authentication authentication) {
        try {

            final OAuthType oAuthType = (OAuthType) authentication.getPrincipal();
            final String key = (String) authentication.getCredentials();

            log.info("oauth type: {}, key: {}", oAuthType, key);

            if (oAuthType == null || key == null) {
                return null;
            }

            switch (oAuthType) {
                case GOOGLE:
                    return googleVerify(key);
                case KAKAO:
                    return kakaoVerify(key);
            }
        } catch (FeignException fe) {
            throw new LoginFailedException();
        }

        return null;
    }

    private OAuthLoginToken googleVerify(String key) {
        // GoogleOAuthResult result = googleClient.verify(key);
        // final String username = result.getName();
        // final String userEmail = result.getEmail();
        // final String picture = result.getPicture();

        final String username = "테스트유저";
        final String userEmail = "hi@naver.com";
        final String picture = "test.picture";

        log.info("picture: {}", picture);
        log.info("username: {}", username);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", userEmail);
        attributes.put("picture", picture);
        attributes.put("name", username);

        var ret = new OAuthLoginToken(attributes, null);
        log.info("ret: {}, {}", ret.getCredentials(), ret.getPrincipal());

        return ret;
    }

    private OAuthLoginToken kakaoVerify(String key) {
        KakaoOAuthResult result = kakaoClient.verify(key);
        final String username = result.getProperties().getNickname();
        final String userEmail = result.getKakao_account().getEmail();
        final String picture = result.getProperties().getProfile_image();

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", userEmail);
        attributes.put("picture", picture);
        attributes.put("name", username);

        return new OAuthLoginToken(userEmail, attributes);
    }
}
