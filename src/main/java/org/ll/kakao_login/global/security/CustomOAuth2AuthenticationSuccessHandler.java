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
        String username = ((SecurityUser) authentication.getPrincipal()).getUsername();
        Member actor = memberService.findByUsername(username)
            .orElseThrow(() -> new IllegalStateException("Member not found: " + username));
        rq.makeAuthCookies(actor); // 최신 nickname, avatar가 반영된 Member로 JWT 생성
        String redirectUrl = request.getParameter("state");

        // 추출한 redirectUrl로 리다이렉트
        response.sendRedirect(redirectUrl);
    }
}