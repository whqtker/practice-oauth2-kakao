package org.ll.kakao_login.global.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

// 클라이언트의 OAuth2 요청을 가로채어 커스텀
@Component
@Slf4j
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
        if (authorizationRequest == null || request == null) {
            return null;
        }

        // 쿼리 파라미터에서 redirectUrl 추출
        String redirectUrl = request.getParameter("redirectUrl");
        log.info("redirectUrl: {}", redirectUrl);

        Map<String, Object> additionalParameters = new HashMap<>(authorizationRequest.getAdditionalParameters());

        // state에 반드시 값이 들어가도록 보장
        String stateValue = (redirectUrl != null && !redirectUrl.isEmpty())
            ? redirectUrl
            : "http://localhost:5173/"; // 또는 서비스 메인 URL

        additionalParameters.put("state", stateValue);

        return OAuth2AuthorizationRequest.from(authorizationRequest)
                .additionalParameters(additionalParameters)
                .state(stateValue)
                .build();
    }
}