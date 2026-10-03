package io.github.ehdnd.mvc2.login.domain.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Member {

  private Long id;

  @NotBlank
  private String loginId;     // 로그인 id
  @NotEmpty
  private String name;        // 사용자 이름
  @NotBlank
  private String password;    // 비밀번호
}
