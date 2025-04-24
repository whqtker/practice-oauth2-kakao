package org.ll.kakao_login.global.rq;


import com.ll.here_is_paw_back_member.domain.member.entity.Member;
import com.ll.here_is_paw_back_member.domain.member.service.MemberService;
import com.ll.here_is_paw_back_member.global.config.AppConfig;
import com.ll.here_is_paw_back_member.global.security.SecurityUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.ll.kakao_login.domain.member.entity.Member;
import org.ll.kakao_login.domain.member.service.MemberService;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Arrays;
import java.util.Optional;

// Request/Response 를 추상화한 객체
// Request, Response, Cookie, Session 등을 다룬다.
@RequestScope
@Component
@RequiredArgsConstructor
public class Rq {
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final MemberService memberService;

    public void setLogin(Member member) {
        UserDetails user = new SecurityUser(
                member.getId(),
                member.getUsername(),
                "",
                member.getNickname(),
                member.getAuthorities()
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                user.getPassword(),
                user.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public Member getActor() {
        return Optional.ofNullable(
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                )
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof SecurityUser)
                .map(principal -> (SecurityUser) principal)
                .map(securityUser -> new Member(securityUser.getId(), securityUser.getUsername(), securityUser.getNickname()))
                .orElse(null);
    }

    public void setCookie(String name, String value) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .domain(AppConfig.getSiteBackUrl())
                .sameSite("Strict")
                .secure(true)
                .httpOnly(true)
//                .httpOnly(false)
                .build();
        resp.addHeader("Set-Cookie", cookie.toString());
    }

    public String getCookieValue(String name) {
        return Optional
                .ofNullable(req.getCookies())
                .stream() // 1 ~ 0
                .flatMap(cookies -> Arrays.stream(cookies))
                .filter(cookie -> cookie.getName().equals(name))
                .map(cookie -> cookie.getValue())
                .findFirst()
                .orElse(null);
    }

    public void deleteCookie(String name) {
        ResponseCookie cookie = ResponseCookie.from(name, null)
                .path("/")
                .domain(AppConfig.getSiteBackUrl())
                .sameSite("Strict")
                .secure(true)
                .httpOnly(true)
                .maxAge(0)
                .build();

        resp.addHeader("Set-Cookie", cookie.toString());
    }

    public void setHeader(String name, String value) {
        resp.setHeader(name, value);
    }

    public String getHeader(String name) {
        return req.getHeader(name);
    }

    public void refreshAccessToken(Member member) {
        String newAccessToken = memberService.genAccessToken(member);

        setHeader("Authorization", "Bearer " + member.getApiKey() + " " + newAccessToken);
        setCookie("accessToken", newAccessToken);
    }

    public String makeAuthCookies(Member member) {
        String accessToken = memberService.genAccessToken(member);
        setCookie("apiKey", member.getApiKey());
        setCookie("accessToken", accessToken);
        return accessToken;
    }
}