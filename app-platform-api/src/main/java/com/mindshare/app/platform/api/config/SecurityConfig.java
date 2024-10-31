package com.mindshare.app.platform.api.config;

import io.client.core.filter.InitializerFilter;
import io.client.core.handler.DefaultAccessDeniedHandler;
import io.client.core.handler.DefaultAuthenticationEntryPoint;
import io.client.core.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final JwtProvider jwtProvider;
  private DefaultAuthenticationEntryPoint defaultAuthenticationEntryPoint;
  private DefaultAccessDeniedHandler defaultAccessDeniedHandler;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .sessionManagement(manage -> manage.sessionCreationPolicy(
            SessionCreationPolicy.STATELESS
        ))
        .authorizeHttpRequests(auth ->
            auth
                .requestMatchers("/", "/favicon.ico", "/v1/auth/**").permitAll()
                .requestMatchers("/v1/**").permitAll()
                .anyRequest().authenticated())
        /**
         * UsernamePasswordAuthenticationFilter에서 아이디, 패스워드로 유저 인증 검증 함
         * 인증정보 검증 전 토큰 소유 여부, 현재 서버에서 발급한 토큰이 맞는지 InitializeFilter로 확인
         */
        .addFilterBefore(new InitializerFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(except ->
            except.authenticationEntryPoint(defaultAuthenticationEntryPoint)
                .accessDeniedHandler(defaultAccessDeniedHandler))
        .cors(Customizer.withDefaults())
        .build();
  }
}
