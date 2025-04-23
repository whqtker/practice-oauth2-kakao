package org.ll.kakao_login.global.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

// 클라이언트의 OAuth2 요청을 가로채어 커스텀
@Component
public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {
    private final DefaultOAuth2AuthorizationRequestResolver defaultResolver;

    // 생성자
    // ClientRegistrationRepository를 주입받아 defaultResolver 초기화
    // "/oauth2/authorization" 를 기본 경로로 설정
    public CustomAuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");
    }

    // HTTP request에서 OAuth2 인증 요청 추출
    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        // defaultResolver를 사용하여 OAuth2AuthorizationRequest를 추출
        OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request);
        return customizeAuthorizationRequest(authorizationRequest, request);
    }

    // 특정 클라이언트 등록 ID에 대한 OAuth2 인증 요청 추출
    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request, clientRegistrationId);
        return customizeAuthorizationRequest(authorizationRequest, request);
    }

    // OAuth2AuthorizationRequest를 커스터마이징
    private OAuth2AuthorizationRequest customizeAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request) {
        // authorizationRequest가 null이거나 request가 null인 경우 null 반환
        if (authorizationRequest == null || request == null) {
            return null;
        }

        // 리다이렉트 url 추출
        String redirectUrl = request.getParameter("redirectUrl");

        // 추가적인 파라미터를 HashMap으로 추출
        Map<String, Object> additionalParameters = new HashMap<>(authorizationRequest.getAdditionalParameters());

        // 리다이렉트 url이 유효하다면 추가 파라미터의 state로 설정
        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            additionalParameters.put("state", redirectUrl);
        }

        // 새로운 OAuth2AuthorizationRequest를 빌드, 반환
        return OAuth2AuthorizationRequest.from(authorizationRequest)
                .additionalParameters(additionalParameters)
                .state(redirectUrl)
                .build();
    }
}