package org.ll.kakao_login.global.webMvc;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

// Spring MVC 설정을 커스터마이징
@Configuration
@RequiredArgsConstructor
public class CustomWebMvcConfig implements WebMvcConfigurer {
  private final LoginUserArgumentResolver loginUserArgumentResolver;

  // 컨트롤러 메서드의 매개변수를 해석하는 리졸버 등록
  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    // 리졸버에 추가함으로써 @LoginUser 어노테이션으로 로그인한 사용자의 정보를 컨트롤러 메서드에 주입 가능
    resolvers.add(loginUserArgumentResolver);
  }
}

