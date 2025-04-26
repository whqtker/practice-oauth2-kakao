package org.ll.kakao_login.global.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

;

@Getter
public class SecurityUser extends User implements OAuth2User {
    private long id;
    private String nickname;
    private String avatar; // 추가

    public SecurityUser(
            long id,
            String username,
            String password,
            String nickname,
            String avatar, // 추가
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(username, password, authorities);
        this.id = id;
        this.nickname = nickname;
        this.avatar = avatar; // 추가
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public String getName() {
        return getUsername();
    }
}