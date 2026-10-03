package io.github.ehdnd.mvc2.login.web.member;

import io.github.ehdnd.mvc2.login.domain.member.Member;
import io.github.ehdnd.mvc2.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

  private final MemberRepository memberRepository;

  @GetMapping("/add")
  public String addForm(@ModelAttribute("member") Member member) {
    return "members/addMemberForm";
  }

  @PostMapping("/add")
  public String save(@Validated @ModelAttribute Member member, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "members/addMemberForm";
    }

    memberRepository.save(member);
    return "redirect:/";
  }

}
