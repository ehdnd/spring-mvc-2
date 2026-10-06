package io.github.ehdnd.mvc2.exception;

import io.github.ehdnd.mvc2.exception.filter.LogFilter;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Bean
  public FilterRegistrationBean logFilter() {

    FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

    filterRegistrationBean.setFilter(new LogFilter());
    filterRegistrationBean.setOrder(1);
    filterRegistrationBean.addUrlPatterns("/*");
    // 둘 다 넣으면 오류 페이지 요청에서도 필터가 호출된다.
    filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);

    return filterRegistrationBean;
  }

}
