package io.github.ehdnd.mvc2.exception.api;

import io.github.ehdnd.mvc2.exception.exception.UserException;
import io.github.ehdnd.mvc2.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ApiExceptionV2Controller {

  // 이 컨트롤러 안에서 IllegalArgumentException 오류가 터지면 얘가 잡아 JSON 응답을 준다.
  // 서블릿 컨테이너까지 안올라간다. 정상처리, 정상흐름으로 끝난다.
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException.class)
  public ErrorResult illegalExHandler(IllegalArgumentException e) {
    log.error("[exceptionHandler] ex", e);
    return new ErrorResult("BAD", e.getMessage());
  }

  // UserException 생략 가능하다.
  @ExceptionHandler
  public ResponseEntity<ErrorResult> userHandler(UserException e) {
    log.error("[exceptionHandler] ex", e);
    ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
    return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
  }

  // 최상위 Exception -> 자식까지 잡아준다 = 모든 예외는 여기로 온다.
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler
  public ErrorResult exHandler(Exception e) {
    log.error("[exceptionHandler] ex", e);
    return new ErrorResult("EX", "내부 오류");
  }

  @GetMapping("/api2/members/{id}")
  public MemberDto getMember(@PathVariable("id") String id) {

    switch (id) {
      case "ex" -> throw new RuntimeException("잘못된 사용자");
      case "bad" -> throw new IllegalArgumentException("잘못된 입력 값");
      case "user-ex" -> throw new UserException("사용자 오류");
    }

    return new MemberDto(id, "hello " + id);
  }

  @Data
  @AllArgsConstructor
  static class MemberDto {

    private String memberId;
    private String name;
  }
}
