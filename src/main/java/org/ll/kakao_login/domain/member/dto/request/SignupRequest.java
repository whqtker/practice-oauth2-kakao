package org.ll.kakao_login.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignupRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String nickname
) {
}
