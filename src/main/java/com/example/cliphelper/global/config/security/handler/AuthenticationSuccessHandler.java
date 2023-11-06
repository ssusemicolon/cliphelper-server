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
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.cliphelper.domain.user.entity.User;
import com.example.cliphelper.domain.user.repository.UserRepository;
import com.example.cliphelper.global.config.security.dto.JwtDto;
import com.example.cliphelper.global.config.security.dto.UserDto;
import com.example.cliphelper.global.config.security.token.JwtAuthenticationToken;
import com.example.cliphelper.global.config.security.token.OAuthLoginToken;
import com.example.cliphelper.global.config.security.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {
        OAuthLoginToken oAuth2User = (OAuthLoginToken) authentication;
        UserDto userDto = UserDto.from(oAuth2User);
        Long userId = getOrSaveUser(userDto);

        log.info("on authentication success! userId: {}", userId);

        JwtDto token = jwtUtil.createJwt(new JwtAuthenticationToken(userId, "",
                List.of("ROLE_USER").stream().map(SimpleGrantedAuthority::new).toList()));

        writeResponse(request, response, token);
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

        log.info("new user signup! email: {}", email);
        User user = User.builder().username(username).email(email).picture(profile).build();
        user = this.userRepository.save(user);

        return user.getId();
    }
}