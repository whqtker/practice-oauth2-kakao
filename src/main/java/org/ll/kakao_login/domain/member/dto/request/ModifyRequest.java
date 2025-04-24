package org.ll.kakao_login.domain.member.dto.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record ModifyRequest(@NotNull Long id, MultipartFile profileImage, String nickname){

    public boolean hasProfile() {
        return profileImage != null;
    }

    public boolean hasNickname() {
        return nickname != null;
    }

}

