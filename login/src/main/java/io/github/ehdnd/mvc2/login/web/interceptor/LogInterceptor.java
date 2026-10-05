package io.github.ehdnd.mvc2.login.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

  public static final String LOG_ID = "logId";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    String requestURI = request.getRequestURI();
    String uuid = UUID.randomUUID().toString();

    // uuid 를 afterCompletion에서도 사용하게 하자.
    request.setAttribute(LOG_ID, uuid);

    // @RequestMapping: HandlerMethod
    // 정적 리소스: ResourceHttpRequestHandler
    if (handler instanceof HandlerMethod) {
      HandlerMethod hm = (HandlerMethod) handler;// 호출할 컨트롤러의 모든 정보가 포함되어있다.
    }

    log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler);
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      @Nullable ModelAndView modelAndView) throws Exception {

    log.info("postHandle [{}]", modelAndView);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, @Nullable Exception ex) throws Exception {

    String requestURI = request.getRequestURI();
    String uuid = (String) request.getAttribute(LOG_ID);
    log.info("RESPONSE [{}][{}][{}]", uuid, requestURI, handler);
    if (ex != null) {
      log.error("afterCompletion error", ex);
    }
  }
}
