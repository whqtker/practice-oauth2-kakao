package org.ll.kakao_login.global.webMvc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ll.ai_recommendation.domain.member.entity.Member;
import org.ll.ai_recommendation.domain.member.service.MemberService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
// @LoginUser 어노테이션이 붙은 매개변수에 현재 로그인한 사용자의 정보를 자동으로 주입
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberService memberService;

    // 특정 매개변수가 이 리졸버에 의해 처리될 수 있는지 확인
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class) &&
                parameter.getParameterType().equals(Member.class);
//            || parameter.getParameterType().equals(Author.class);
    }

//    @Override
//    public Object resolveArgument(
//            MethodParameter parameter, ModelAndViewContainer mavContainer,
//            NativeWebRequest webRequest, WebDataBinderFactory binderFactory
//    ) {
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return null;
//        }
//
//        Object principal = authentication.getPrincipal();
//        if (!(principal instanceof SecurityUser)) {
//            return null;
//        }
//
//        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
//        String username = securityUser.getUsername();
//
//        Member loginUser = memberService.findByUsername(username).get();
//
////        if (parameter.getParameterType().equals(Author.class)) {
////
////            return new Author(loginUser.getId(), loginUser.getNickname());
////        }
//
//        return loginUser;
//    }

    // 매개변수의 값을 해석
    @Override
    public Object resolveArgument(
        MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory
    ) {

        // 모든 헤더 출력
        webRequest.getHeaderNames().forEachRemaining(headerName -> {
            log.debug("Header: {} = {}", headerName, webRequest.getHeader(headerName));
        });

        // 헤더에서 X-User-Id에서 사용자 ID 추출
        String userIdStr = webRequest.getHeader("X-User-Id");

        if (userIdStr == null) {
            log.debug("X-User-Id header not found");
            return null;
        }

        try {
            Long userId = Long.parseLong(userIdStr);
            log.debug("Found userId in header: {}", userId);

            // 추출한 userId로 MemberService에서 사용자 정보 조회
            Member loginUser = memberService.findById(userId).get();

            // 필요에 따라 MemberService에서 상세 정보 로드 (선택적)
            // 아래 코드는 상세 정보가 필요할 때 주석 해제
            // return memberServiceClient.getMemberById(userId);

            return loginUser;
        } catch (NumberFormatException e) {
            log.error("Invalid userId format: {}", userIdStr, e);
            return null;
        }
    }
}