package org.ll.kakao_login.domain.member.dto.response;


import jakarta.validation.constraints.NotNull;
import org.ll.kakao_login.domain.member.entity.Member;

public record MemberInfoDto(@NotNull Long id,@NotNull String username, @NotNull String nickname, String avatar) {
    public MemberInfoDto(Member member) {
        this(member.getId(), member.getUsername(), member.getNickname(), member.getAvatar());
    }
}