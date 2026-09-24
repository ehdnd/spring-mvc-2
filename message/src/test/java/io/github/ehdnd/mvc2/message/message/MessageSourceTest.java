package io.github.ehdnd.mvc2.message.message;

import static org.assertj.core.api.Assertions.*;

import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

@SpringBootTest
public class MessageSourceTest {

  @Autowired
  MessageSource ms;

  @Test
  void helloMessage() {
    String result = ms.getMessage("hello", null, null); // local 없으면 default 선택
    assertThat(result).isEqualTo("안녕");
  }

  @Test
  void notFoundMessageCode() {
    assertThatThrownBy(() -> ms.getMessage("no_code", null, null)).isInstanceOf(
        NoSuchMessageException.class);
  }

  @Test
  void notFoundMessageCodeDefaultMessage() {
    String res = ms.getMessage("no_code", null, "기본 메시지", null);
    assertThat(res).isEqualTo("기본 메시지");
  }

  @Test
  void argumentMessage() {
    String res = ms.getMessage("hello.name", new Object[]{"Spring"}, null);
    assertThat(res).isEqualTo("안녕 Spring");
  }

  @Test
  void defaultLang() {
    assertThat(ms.getMessage("hello", null, null)).isEqualTo("안녕");
    assertThat(ms.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
  }

  @Test
  void enLang() {
    assertThat(ms.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
  }
}
