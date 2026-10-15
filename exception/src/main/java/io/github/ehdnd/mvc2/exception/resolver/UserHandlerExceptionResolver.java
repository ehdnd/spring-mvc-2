package io.github.ehdnd.mvc2.exception.resolver;

import io.github.ehdnd.mvc2.exception.exception.UserException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import tools.jackson.databind.ObjectMapper;

@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public @Nullable ModelAndView resolveException(HttpServletRequest request,
      HttpServletResponse response, @Nullable Object handler, Exception ex) {

    // 예외를 DispatcherServlet 안에서 잡아 정상 응답으로 바꾸자
    try {

      if (ex instanceof UserException) {
        log.info("UserException resolver to 400");
        // HTTP 헤더에 따라서 처리. json vs HTML
        String acceptHeader = request.getHeader("accept");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        if ("application/json".equals(acceptHeader)) {
          Map<String, Object> errorResult = new HashMap<>();
          errorResult.put("ex", ex.getClass());
          errorResult.put("message", ex.getMessage());

          String result = objectMapper.writeValueAsString(errorResult);

          response.setContentType("application/json");
          response.setCharacterEncoding("utf-8");
          response.getWriter().write(result);

          return new ModelAndView();
        } else {
          // TEXT/HTML
          return new ModelAndView("error/400");
        }
      }

    } catch (IOException e) {
      log.error("resolver ex", e);
    }

    return null;
  }
}
