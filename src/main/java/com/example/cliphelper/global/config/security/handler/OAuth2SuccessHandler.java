package com.example.cliphelper.global.config.security.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.cliphelper.domain.user.entity.User;
import com.example.cliphelper.domain.user.repository.UserRepository;
import com.example.cliphelper.global.config.security.dto.JwtDto;
import com.example.cliphelper.global.config.security.dto.UserDto;
import com.example.cliphelper.global.config.security.token.JwtAuthenticationToken;
import com.example.cliphelper.global.config.security.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        UserDto userDto = UserDto.from(oAuth2User);

        log.info("Principal에서 꺼낸 OAuth2User = {}", oAuth2User);

        // 최초 로그인이라면 회원가입 처리를 한다.
        log.info("토큰 발행 시작");

        Long userId = getOrSaveUser(userDto);

        JwtDto token = jwtUtil.createJwt(new JwtAuthenticationToken(userId, "",
                List.of("ROLE_USER").stream().map(SimpleGrantedAuthority::new).toList()));
        log.info("{}", token);

        writeResponse(request, response, token);

        // http://localhost:8080/oauth2/authorization/google

        // targetUrl = UriComponentsBuilder.fromUriString("/home")
        // .queryParam("token", "token")
        // .build().toUriString();
        // getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    public static void writeResponse(HttpServletRequest request, HttpServletResponse response, Object data)
            throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, data);
            os.flush();
        }
    }

    private Optional<User> getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    private Long getOrSaveUser(UserDto userDto) {
        final String email = userDto.getEmail();
        final String username = userDto.getName();
        final String profile = userDto.getPicture();

        Optional<User> optionalUser = getUserByEmail(email);

        if (optionalUser.isPresent()) {
            return optionalUser.get().getId();
        }

        User user = User.builder().username(username).email(email).picture(profile).build();
        user = this.userRepository.save(user);

        return user.getId();
    }
}