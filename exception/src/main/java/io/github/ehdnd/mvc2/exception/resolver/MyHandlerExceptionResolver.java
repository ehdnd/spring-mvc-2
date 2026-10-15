package io.github.ehdnd.mvc2.exception.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

  @Override
  public @Nullable ModelAndView resolveException(HttpServletRequest request,
      HttpServletResponse response, @Nullable Object handler, Exception ex) {

    try {
      if (ex instanceof IllegalArgumentException) {
        log.info("IllegalArgumentException resolver to 400");
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        // 에러를 먹고 정상적으로 400을 WAS 에게 전달한다.
        return new ModelAndView();  // 빈 ModelAndView -> 뷰 렌더링 하지 않고 정상 흐름으로 서블릿 리턴
      }

    } catch (IOException e) {
      log.error("resolver ex", e);
    }

    return null;  // 다음 ExceptionResolver 를 찾고, 없으면 기존에 발생한 예외를 서블릿 밖으로 던진다.
  }
}
