package io.github.ehdnd.mvc2.login;

import io.github.ehdnd.mvc2.login.domain.item.Item;
import io.github.ehdnd.mvc2.login.domain.item.ItemRepository;
import io.github.ehdnd.mvc2.login.domain.member.Member;
import io.github.ehdnd.mvc2.login.domain.member.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataInit {

  private final ItemRepository itemRepository;
  private final MemberRepository memberRepository;

  /**
   * 테스트용 데이터 추가
   */
  @PostConstruct
  public void init() {
    itemRepository.save(new Item("itemA", 10000, 10));
    itemRepository.save(new Item("itemB", 20000, 20));

    Member member = new Member();
    member.setLoginId("test");
    member.setPassword("test!");
    member.setName("tester");
    memberRepository.save(member);
  }

}