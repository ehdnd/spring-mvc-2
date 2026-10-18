package io.github.ehdnd.mvc2.typeconverter;

import io.github.ehdnd.mvc2.typeconverter.converter.IntegerToStringConverter;
import io.github.ehdnd.mvc2.typeconverter.converter.IpPortToStringConverter;
import io.github.ehdnd.mvc2.typeconverter.converter.StringToIntegerConverter;
import io.github.ehdnd.mvc2.typeconverter.converter.StringToIpPortConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new StringToIpPortConverter());
    registry.addConverter(new IpPortToStringConverter());
    registry.addConverter(new StringToIntegerConverter());
    registry.addConverter(new IntegerToStringConverter());
  }
}
