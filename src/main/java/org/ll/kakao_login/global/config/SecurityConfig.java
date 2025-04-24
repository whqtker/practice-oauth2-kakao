package org.ll.kakao_login.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ll.kakao_login.global.security.CustomAuthorizationRequestResolver;
import org.ll.kakao_login.global.security.CustomOAuth2AuthenticationSuccessHandler;
import org.ll.kakao_login.global.security.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
  private final CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler;
  private final CustomAuthorizationRequestResolver customAuthorizationRequestResolver;

//  @Bean
//  public CorsConfigurationSource corsConfigurationSource() {
//    CorsConfiguration configuration = new CorsConfiguration();
//    configuration.setAllowedOrigins(Arrays.asList(
//        "http://localhost:5173",
//        "http://43.203.126.129:8001"));
//    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
//    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
//    configuration.setAllowCredentials(true);
//
//    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//    source.registerCorsConfiguration("/**", configuration);
//    return source;
//  }


  @Bean
  public SecurityFilterChain baseSecurityFilterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService) throws Exception {
    http
        .authorizeHttpRequests(authorizeRequests ->
            authorizeRequests
                // 공개 API 설정 유지
//                .requestMatchers("/h2-console/**").permitAll()
//                .requestMatchers("/swagger-ui/index.html").permitAll()
//                .requestMatchers("/api/v1/members/me").permitAll()
//                .requestMatchers("/api/v1/members/signup").permitAll()
//                .requestMatchers("/api/v1/members/login").permitAll()
//                .requestMatchers("/api/v1/members/logout").permitAll()
                // 나머지 API는 모두 허용 (API 게이트웨이에서 이미 인증됨)
                .anyRequest().permitAll()
        )
        .headers(
            headers ->
                headers.frameOptions(
                    HeadersConfigurer.FrameOptionsConfig::sameOrigin
                )
        )
        .csrf(AbstractHttpConfigurer::disable)
//        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .formLogin(
            AbstractHttpConfigurer::disable
        )
        .sessionManagement((sessionManagement) -> sessionManagement
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .oauth2Login(
            oauth2Login -> oauth2Login
                .successHandler(customOAuth2AuthenticationSuccessHandler)
                .authorizationEndpoint(
                    authorizationEndpoint -> authorizationEndpoint
                        .authorizationRequestResolver(customAuthorizationRequestResolver)
                )
        )
    // CustomAuthenticationFilter 제거
    ;

    return http.build();
  }
}