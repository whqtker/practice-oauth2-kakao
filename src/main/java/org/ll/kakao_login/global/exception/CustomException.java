package org.ll.kakao_login.global.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ll.kakao_login.global.error.ErrorCode;


@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
}
