package io.github.ehdnd.mvc2.login.web;

import io.github.ehdnd.mvc2.login.web.argumentResolver.LoginMemberArgumentResolver;
import io.github.ehdnd.mvc2.login.web.filter.LogFilter;
import io.github.ehdnd.mvc2.login.web.filter.LoginCheckFilter;
import io.github.ehdnd.mvc2.login.web.interceptor.LogInterceptor;
import io.github.ehdnd.mvc2.login.web.interceptor.LoginCheckInterceptor;
import jakarta.servlet.Filter;
import java.util.List;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new LoginMemberArgumentResolver());
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {

    registry.addInterceptor(new LogInterceptor()).order(1).addPathPatterns("/**")
        .excludePathPatterns("/css/**", "/*.ico", "/error");

    // 세밀한 패턴 적용이 가능하다.
    registry.addInterceptor(new LoginCheckInterceptor()).order(2).addPathPatterns("/**")
        .excludePathPatterns("/", "/members/add", "/login", "/logout", "/css/**", "/*.ico",
            "/error");
  }

  //  @Bean
  public FilterRegistrationBean logFilter() {
    FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
    filterRegistrationBean.setFilter(new LogFilter());
    filterRegistrationBean.setOrder(1);
    filterRegistrationBean.addUrlPatterns("/*");

    return filterRegistrationBean;
  }

  //  @Bean
  public FilterRegistrationBean loginCheckFilter() {
    FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
    filterRegistrationBean.setFilter(new LoginCheckFilter());
    filterRegistrationBean.setOrder(2);
    filterRegistrationBean.addUrlPatterns("/*");  // 화이트리스트 빼고 미래의 모든 URL에 대해서도 적용하고픔

    return filterRegistrationBean;
  }
}
