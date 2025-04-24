package org.ll.kakao_login.global.security;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ll.kakao_login.domain.member.entity.Member;
import org.ll.kakao_login.domain.member.service.MemberService;
import org.ll.kakao_login.global.rq.Rq;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
// OAuth2 인증 성공 후 동작 커스텀
public class CustomOAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final MemberService memberService;
    private final Rq rq;
    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Member actor = memberService.findById(rq.getActor().getId()).get();

        // 조회된 사용자 정보를 기반으로 인증 쿠키 생성
        rq.makeAuthCookies(actor);
        String redirectUrl = request.getParameter("state");

        // 추출한 redirectUrl로 리다이렉트
        response.sendRedirect(redirectUrl);
    }
}