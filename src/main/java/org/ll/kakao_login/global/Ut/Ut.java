package org.ll.kakao_login.global.Ut;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

public class Ut {
    public static class str {
        // 문자열이 null 이거나 공백인지 확인
        public static boolean isBlank(String str) {
            return str == null || str.trim().isEmpty();
        }
    }

    public static class json {
        private static final ObjectMapper om = new ObjectMapper();

        @SneakyThrows
        // 객체를 JSON 문자열로 변환
        public static String toString(Object obj) {
            return om.writeValueAsString(obj);
        }
    }

    // JWT 클래스
    // jjwt 라이브러리 사용
    public static class jwt {
        // JWT 토큰 생성
        public static String toString(String secret, long expireSeconds, Map<String, Object> body) {
            // 토큰 발행 시간 - 현재 시간으로 설정
            Date issuedAt = new Date();

            // 토큰 만료 시간
            Date expiration = new Date(issuedAt.getTime() + 1000L * expireSeconds);

            // secretKey 생성 - HMAC SHA256 알고리즘 사용
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());

            String jwt = Jwts.builder()
                    .claims(body)
                    .issuedAt(issuedAt)
                    .expiration(expiration)
                    .signWith(secretKey)
                    .compact();

            return jwt;
        }

        // JWT 토큰의 유효성 검증
        public static boolean isValid(String secret, String jwtStr) {
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());

            try {
                Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parse(jwtStr);
            } catch (Exception e) {
                return false;
            }

            return true;
        }

        // JWT 토큰의 payload 파싱
        public static Map<String, Object> payload(String secret, String jwtStr) {
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());

            try {
                return (Map<String, Object>) Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parse(jwtStr)
                        .getPayload();
            } catch (Exception e) {
                return null;
            }
        }
    }
}