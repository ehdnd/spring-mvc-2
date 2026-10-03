package io.github.ehdnd.mvc2.login.domain.login;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginForm {

  @NotEmpty
  private String loginId;

  @NotEmpty
  private String password;
}
