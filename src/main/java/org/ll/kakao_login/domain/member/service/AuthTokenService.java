package org.ll.kakao_login.domain.member.service;

import org.ll.kakao_login.domain.member.entity.Member;
import org.ll.kakao_login.global.Ut.Ut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthTokenService {
    @Value("${custom.jwt.secretKey}")
    private String jwtSecretKey;

    @Value("${custom.accessToken.expirationSeconds}")
    private long accessTokenExpirationSeconds;

    String genAccessToken(Member member) {
        long id = member.getId();
        String username = member.getUsername();
        String nickname = member.getNickname();
        String avatar = member.getAvatar();

        return Ut.jwt.toString(
                jwtSecretKey,
                accessTokenExpirationSeconds,
                Map.of(
                    "id", id,
                    "username", username,
                    "nickname", nickname,
                    "avatar", avatar
                )
        );
    }

    Map<String, Object> payload(String accessToken) {
        Map<String, Object> parsedPayload = Ut.jwt.payload(jwtSecretKey, accessToken);

        if (parsedPayload == null) return null;

        long id = ((Number) parsedPayload.get("id")).longValue();
        String username = (String) parsedPayload.get("username");
        String nickname = (String) parsedPayload.get("nickname");
        String avatar = (String) parsedPayload.get("avatar");

        return Map.of(
            "id", id,
            "username", username,
            "nickname", nickname,
            "avatar", avatar
        );
    }
}
