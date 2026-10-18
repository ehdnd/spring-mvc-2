package io.github.ehdnd.mvc2.typeconverter.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  @GetMapping("/hello-v1")
  public String helloV1(HttpServletRequest request) {
    String data = request.getParameter("data"); // 문자 10
    Integer intValue = Integer.valueOf(data);
    System.out.println("intValue = " + intValue);
    return "ok";
  }

  @GetMapping("/hello-v2")
  public String helloV2(@RequestParam Integer data) {
    // 스프링이 중간에 타입을 변환해줬다.
    System.out.println("data = " + data);
    return "ok";
  }
}
