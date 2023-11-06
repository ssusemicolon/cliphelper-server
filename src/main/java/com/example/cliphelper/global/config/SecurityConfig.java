package com.example.cliphelper.global.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
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
import com.example.cliphelper.global.config.security.filter.LoginAuthenticationFilter;
import com.example.cliphelper.global.config.security.handler.AuthenticationSuccessHandler;
import com.example.cliphelper.global.config.security.handler.CustomAccessDeniedHandler;
import com.example.cliphelper.global.config.security.handler.CustomAuthenticationEntryPoint;
import com.example.cliphelper.global.config.security.handler.CustomAuthenticationFailureHandler;
import com.example.cliphelper.global.config.security.provider.LoginAuthenticationProvider;
import com.example.cliphelper.global.config.security.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
        private final JwtUtil jwtUtil;
        private final AuthenticationSuccessHandler authenticationSuccessHandler;
        private final CustomAuthenticationFailureHandler authenticationFailureHandler;
        private final CustomAuthenticationEntryPoint authenticationEntryPoint;
        private final CustomAccessDeniedHandler accessDeniedHandler;
        private final LoginAuthenticationProvider loginAuthenticationProvider;

        // filters
        public LoginAuthenticationFilter loginAuthenticationFilter() {
                final LoginAuthenticationFilter filter = new LoginAuthenticationFilter();
                filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
                filter.setAuthenticationFailureHandler(authenticationFailureHandler);
                filter.setAuthenticationManager(authenticationManager());
                return filter;
        }

        @Bean
        AuthenticationManager authenticationManager() {
                return new ProviderManager(Arrays.asList(loginAuthenticationProvider));
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
                                .authorizeRequests(
                                                requests -> requests.antMatchers().permitAll()
                                                                .anyRequest().authenticated())
                                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil),
                                                UsernamePasswordAuthenticationFilter.class)
                                .addFilterBefore(loginAuthenticationFilter(),
                                                JwtAuthenticationFilter.class)
                                .exceptionHandling(handling -> handling
                                                .authenticationEntryPoint(authenticationEntryPoint)
                                                .accessDeniedHandler(accessDeniedHandler))
                                .build();
        }
}
