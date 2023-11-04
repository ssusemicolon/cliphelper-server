package com.example.cliphelper.global.config.security.filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.cliphelper.global.config.security.dto.UserDto;
import com.example.cliphelper.global.config.security.handler.HandlerUtility;
import com.example.cliphelper.global.config.security.util.JwtUtil;
import com.example.cliphelper.global.error.BusinessException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public Authentication getAuthentication(UserDto member) {
        return new UsernamePasswordAuthenticationToken(member, "",
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            final String token = jwtUtil.extractJwt(request);
            Authentication authentication = jwtUtil.getAuthentication(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (BusinessException be) {
            logger.info("exception: " + be.getErrorCode());
            HandlerUtility.writeResponse(request, response, be.getErrorCode());
        }

    }
}