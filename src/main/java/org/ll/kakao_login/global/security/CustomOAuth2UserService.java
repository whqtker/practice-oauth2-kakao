package org.ll.kakao_login.global.security;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ll.kakao_login.domain.member.entity.Member;
import org.ll.kakao_login.domain.member.service.MemberService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberService memberService;

    // 소셜 로그인이 성공할 때마다 실행됨
    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String oauthId = oAuth2User.getName();
        String providerTypeCode = userRequest
                .getClientRegistration()
                .getRegistrationId()
                .toUpperCase(Locale.getDefault());
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, String> attributesProperties = (Map<String, String>) attributes.get("properties");
        String nickname = attributesProperties.get("nickname");
        String profileImgUrl = attributesProperties.get("profile_image");
        String username = providerTypeCode + "__" + oauthId;
        Member member = memberService.modifyOrJoin(username, nickname, profileImgUrl);

        return new SecurityUser(
                member.getId(),
                member.getUsername(),
                "",
                member.getNickname(),
                member.getAuthorities()
        );
    }
}
