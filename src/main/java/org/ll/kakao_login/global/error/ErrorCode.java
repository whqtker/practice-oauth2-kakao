package org.ll.kakao_login.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "제공된 입력 값이 유효하지 않습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 요청 방식입니다."),
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST, "요청한 엔티티를 찾을 수 없습니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "제공된 값의 타입이 유효하지 않습니다."),
    ERROR_PARSING_JSON_RESPONSE(HttpStatus.BAD_REQUEST, "JSON 응답을 파싱하는 중 오류가 발생했습니다."),
    MISSING_INPUT_VALUE(HttpStatus.BAD_REQUEST, "필수 입력 값이 누락되었습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    // Token
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "액세스 토큰이 만료되었습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다."),


    // User
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "해당 유저가 존재하지 않습니다"),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    // Auth
    ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "인증되지 않은 유저입니다."),
    SC_FORBIDDEN(HttpStatus.UNAUTHORIZED, "권한이 없는 유저입니다."),
    INVALID_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "서명 검증에 실패했습니다." ),
    ILLEGAL_REGISTRATION_ID(HttpStatus.BAD_REQUEST,"해당 사항이 없는 로그인 경로입니다."),

    TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "토큰이 만료되었습니다."),

    // post
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),

    // myPet
    MY_PET_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 반려견을 찾을 수 없습니다."),

    //S3
    S3_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3 업로드 중 오류가 발생했습니다."),
  
    // missing
    MISSING_NOT_FOUND(HttpStatus.NOT_FOUND, "신고글을 찾을 수 없습니다."),

    // payment
    PAYMENT_FAILED(HttpStatus.BAD_REQUEST, "결제 처리에 실패했습니다."),
    DUPLICATE_PAYMENT(HttpStatus.BAD_REQUEST, "중복된 결제 요청입니다."),
    PAYMENT_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "결제 정보 저장에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
