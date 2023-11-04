package com.example.cliphelper.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.cliphelper.global.config.security.filter.JwtAuthenticationFilter;
import com.example.cliphelper.global.config.security.handler.CustomAccessDeniedHandler;
import com.example.cliphelper.global.config.security.handler.CustomAuthenticationEntryPoint;
import com.example.cliphelper.global.config.security.handler.CustomAuthenticationFailureHandler;
import com.example.cliphelper.global.config.security.handler.OAuth2SuccessHandler;
import com.example.cliphelper.global.config.security.service.CustomOAuth2UserService;
import com.example.cliphelper.global.config.security.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
        private final JwtUtil jwtUtil;
        private final CustomOAuth2UserService oAuth2UserService;
        private final OAuth2SuccessHandler oAuth2SuccessHandler;
        private final CustomAuthenticationFailureHandler authenticationFailureHandler;
        private final CustomAuthenticationEntryPoint authenticationEntryPoint;
        private final CustomAccessDeniedHandler accessDeniedHandler;

        @Bean
        AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
                return http.getSharedObject(AuthenticationManagerBuilder.class).build();
        }

        @Bean
        BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        CorsConfigurationSource configurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();

                configuration.addAllowedOriginPattern("*");
                configuration.addAllowedHeader("*");
                configuration.addAllowedMethod("*");
                configuration.setAllowCredentials(true);
                configuration.setMaxAge(3600L);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);

                return source;
        }

        @Bean
        SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                return http.httpBasic(basic -> basic.disable())
                                .cors(cors -> configurationSource())
                                .logout(logout -> logout.disable())
                                .formLogin(login -> login.disable())
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(management -> management
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeRequests(requests -> requests.antMatchers("/token/**").permitAll()
                                                .anyRequest().authenticated())
                                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil),
                                                UsernamePasswordAuthenticationFilter.class)
                                .oauth2Login(login -> login
                                                .successHandler(oAuth2SuccessHandler)
                                                .failureHandler(authenticationFailureHandler)
                                                .userInfoEndpoint().userService(oAuth2UserService))
                                .exceptionHandling(handling -> handling
                                                .authenticationEntryPoint(authenticationEntryPoint)
                                                .accessDeniedHandler(accessDeniedHandler))
                                .build();
        }
}
