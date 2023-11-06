package com.example.cliphelper.global.config.security.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.cliphelper.global.config.security.dto.LoginRequestDto;
import com.example.cliphelper.global.config.security.token.OAuthLoginToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private static final AntPathRequestMatcher ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/auth/login",
			"POST");
	final ObjectMapper objectMapper = new ObjectMapper();

	public LoginAuthenticationFilter() {
		super(ANT_PATH_REQUEST_MATCHER);

	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		if (!request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		log.info("로그인 필터!");
		try {

			final String requestBody = IOUtils.toString(request.getReader());
			log.info("request body: {}", requestBody);

			final LoginRequestDto loginRequest = objectMapper.readValue(requestBody, LoginRequestDto.class);

			final OAuthLoginToken authRequest = new OAuthLoginToken(
					loginRequest.getType(), loginRequest.getKey());

			log.info("토큰: {}", authRequest);

			log.info("매니저: {}", super.getAuthenticationManager());

			return super.getAuthenticationManager().authenticate(authRequest);
		} catch (IOException ioe) {
			throw new AuthenticationServiceException("Authentication failed while converting request body.");
		}
	}

}