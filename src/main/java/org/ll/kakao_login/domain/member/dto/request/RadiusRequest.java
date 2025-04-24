package org.ll.kakao_login.domain.member.dto.request;

import jakarta.validation.constraints.NotNull;

public record RadiusRequest(@NotNull Double radius) {
}
