package com.example.cliphelper.global.config.security.provider;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.cliphelper.global.config.security.vo.OAuth2Attribute;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
        @Override
        public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
                // 1번 DefaultOAuth2UserService 객체를 성공정보를 바탕으로 만든다.
                OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

                // 2번 생성된 Service 객체로 부터 User를 받는다.
                OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

                // 3번 받은 User로 부터 user 정보를 받는다.
                String registrationId = userRequest.getClientRegistration().getRegistrationId();
                String userNameAttributeName = userRequest.getClientRegistration()
                                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
                log.info("registrationId = {}", registrationId);
                log.info("userNameAttributeName = {}", userNameAttributeName);

                // 4번 SuccessHandler가 사용할 수 있도록 등록해준다.
                OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(registrationId, userNameAttributeName,
                                oAuth2User.getAttributes());

                var memberAttribute = oAuth2Attribute.convertToMap();

                return new DefaultOAuth2User(
                                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                                memberAttribute, "email");
        }
}