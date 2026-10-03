package io.github.ehdnd.mvc2.login.domain.login;

import io.github.ehdnd.mvc2.login.domain.member.Member;
import io.github.ehdnd.mvc2.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

  private final MemberRepository memberRepository;

  /**
   * @return null 이면 로그인 실패
   */
  public Member login(String loginId, String password) {
    return memberRepository.findByLoginId(loginId).filter(m -> m.getPassword().equals(password))
        .orElse(null);
  }
}
