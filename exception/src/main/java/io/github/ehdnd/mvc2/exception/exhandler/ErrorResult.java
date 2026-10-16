package io.github.ehdnd.mvc2.exception.exhandler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResult {

  private String code;
  private String message;
}
