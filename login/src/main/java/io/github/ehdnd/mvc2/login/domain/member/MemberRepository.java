package io.github.ehdnd.mvc2.login.domain.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class MemberRepository {

  private static Map<Long, Member> store = new HashMap<>();
  private static long sequence = 0L;

  public Member save(Member member) {
    member.setId(++sequence);
    log.info("save: member={}", member);
    store.put(member.getId(), member);
    return member;
  }

  public Member findById(Long id) {
    return store.get(id);
  }

  public Optional<Member> findByLoginId(String loginId) {
    // java 8 의 람다와 스트림을 기본적으로 사용할 줄 알아야한다.
    return findAll().stream()
        .filter(m -> m.getLoginId().equals(loginId))
        .findFirst();
  }

  public List<Member> findAll() {
    return new ArrayList<>(store.values());
  }

  public void clearStore() {
    store.clear();
  }

}
