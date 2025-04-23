package org.ll.kakao_login.global.webMvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 메서드의 매개변수에만 사용 가능
@Target(ElementType.PARAMETER)

// 어노테이션이 런타임에도 유지, 리플렉션을 통해 접근 가능
@Retention(RetentionPolicy.RUNTIME)

// @LoginUser 어노테이션 선언
public @interface LoginUser {
}
