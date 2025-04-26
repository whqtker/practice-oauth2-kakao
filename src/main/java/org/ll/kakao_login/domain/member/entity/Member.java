package org.ll.kakao_login.domain.member.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.ll.kakao_login.global.jpa.BaseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Member extends BaseEntity {
    @Column(unique = true, length = 30)
    private String username;

    private String password;

    @Column(length = 30)
    private String nickname;

    @Column(unique = true, length = 50)
    private String apiKey;

//    //채팅 관계
//    @OneToMany(fetch = FetchType.EAGER ,mappedBy = "chatUser")
//    @JsonManagedReference
//    private List<ChatRoom> chatRoomsCU;
//
//    @OneToMany(fetch = FetchType.EAGER ,mappedBy = "targetUser")
//    @JsonManagedReference
//    private List<ChatRoom> chatRoomsTU;
//
//    //메세지 관계
//    @OneToMany(fetch = FetchType.EAGER ,mappedBy = "member")
//    @JsonManagedReference
//    private List<ChatMessage> chatMessages;

    private String avatar;

    private Double radius;

    public boolean isAdmin() {
        return "admin".startsWith(username);
    }

    public boolean isManager() {
        return "manager".startsWith(username);
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }

//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Payment> payments = new ArrayList<>();
    private Long point;

    public Member(long id, String username, String nickname, String avatar) {
        this.setId(id);
        this.username = username;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthoritiesAsStringList()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public List<String> getAuthoritiesAsStringList() {
        List<String> authorities = new ArrayList<>();

        if (isAdmin())
            authorities.add("ROLE_ADMIN");

        if (isManager())
            authorities.add("ROLE_MANAGER");

        return authorities;
    }
}