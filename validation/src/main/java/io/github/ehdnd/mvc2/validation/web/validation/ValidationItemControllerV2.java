package io.github.ehdnd.mvc2.validation.web.validation;

import io.github.ehdnd.mvc2.validation.domain.item.Item;
import io.github.ehdnd.mvc2.validation.domain.item.ItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

  private final ItemRepository itemRepository;
  private final ItemValidator itemValidator;

  @InitBinder // 이 컨트롤러에만 적용된다.
  public void init(WebDataBinder dataBinder) {
    dataBinder.addValidators(itemValidator);
  }

  @GetMapping
  public String items(Model model) {
    List<Item> items = itemRepository.findAll();
    model.addAttribute("items", items);
    return "validation/v2/items";
  }

  @GetMapping("/{itemId}")
  public String item(@PathVariable long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "validation/v2/item";
  }

  @GetMapping("/add")
  public String addForm(Model model) {
    // validation) model을 넘겼기에 검증에 실패했을 때 데이터가 다시 보이도록 그대로 재사용이 가능하다.
    model.addAttribute("item", new Item());
    return "validation/v2/addForm";
  }

  //  @PostMapping("/add")
  public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult,
      RedirectAttributes redirectAttributes) {

    // [V1] FieldError(objectName, field, defaultMessage) 3-인자 생성자 → rejectedValue = null
    // 에러가 있는 필드는 th:field가 모델 값 대신 FieldError.rejectedValue를 출력한다
    //  - 가격 111: 바인딩 성공(item.price=111)이지만 rejectedValue가 null → 입력값 사라짐
    //  - 가격 qqq: 바인딩 실패 → 스프링이 rejectedValue="qqq", bindingFailure=true로 FieldError 생성 → 유지

    // 검증 로직
    if (!StringUtils.hasText(item.getItemName())) {
      bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
    }
    if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
      bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
    }
    if (item.getQuantity() == null || item.getQuantity() > 9999) {
      bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다."));
    }

    // 특정 필드가 아닌 복합 룰 검증
    if (item.getPrice() != null && item.getQuantity() != null) {
      int resultPrice = item.getPrice() * item.getQuantity();
      if (resultPrice < 10000) {
        bindingResult.addError(
            new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
      }
    }

    // 검증에 실패하면 다시 입력 폼으로
    if (bindingResult.hasErrors()) {
      log.info("bindingResult = {}", bindingResult);
      // `bindingResult` 는 자동으로 넘어간다.
      return "validation/v2/addForm";
    }

    // 성공 로직
    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v2/items/{itemId}";
  }


  //  @PostMapping("/add")
  public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult,
      RedirectAttributes redirectAttributes) {

    // [V2] 7-인자 생성자: (objectName, field, rejectedValue, bindingFailure, codes, arguments, defaultMessage)
    // rejectedValue에 사용자 입력값 전달 → 재렌더링 시 입력값 유지
    // bindingFailure=false: 바인딩은 성공했고 검증 로직에서 실패
    // codes/arguments=null, 메시지는 defaultMessage에 하드코딩 (V3에서 codes + errors.properties로 분리)

    // 검증 로직
    if (!StringUtils.hasText(item.getItemName())) {
      bindingResult.addError(
          new FieldError("item", "itemName", item.getItemName(), false, null, null,
              "상품 이름은 필수입니다."));
    }
    if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
      bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null,
          "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
    }
    if (item.getQuantity() == null || item.getQuantity() > 9999) {
      bindingResult.addError(
          new FieldError("item", "quantity", item.getQuantity(), false, null, null,
              "수량은 최대 9,999 까지 허용합니다."));
    }

    // 특정 필드가 아닌 복합 룰 검증
    if (item.getPrice() != null && item.getQuantity() != null) {
      int resultPrice = item.getPrice() * item.getQuantity();
      if (resultPrice < 10000) {
        bindingResult.addError(
            new ObjectError("item", null, null,
                "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
      }
    }

    // 검증에 실패하면 다시 입력 폼으로
    if (bindingResult.hasErrors()) {
      log.info("bindingResult = {}", bindingResult);
      // `bindingResult` 는 자동으로 넘어간다.
      return "validation/v2/addForm";
    }

    // 성공 로직
    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v2/items/{itemId}";
  }


  //  @PostMapping("/add")
  public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult,
      RedirectAttributes redirectAttributes) {

    // errors.properties 메시지 사용

    // 검증 로직
    if (!StringUtils.hasText(item.getItemName())) {
      bindingResult.addError(
          new FieldError("item", "itemName", item.getItemName(), false,
              new String[]{"required.item.itemName"}, null, null));
    }
    if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1_000_000) {
      bindingResult.addError(
          new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"},
              new Object[]{1000, 1_000_000}, null));
    }
    if (item.getQuantity() == null || item.getQuantity() > 9999) {
      bindingResult.addError(
          new FieldError("item", "quantity", item.getQuantity(), false,
              new String[]{"max.item.quantity"}, new Object[]{9999}, null));
    }

    // 특정 필드가 아닌 복합 룰 검증
    if (item.getPrice() != null && item.getQuantity() != null) {
      int resultPrice = item.getPrice() * item.getQuantity();
      if (resultPrice < 10_000) {
        bindingResult.addError(
            new ObjectError("item", new String[]{"totalPriceMin"},
                new Object[]{10_000, resultPrice},
                null));
      }
    }

    // 검증에 실패하면 다시 입력 폼으로
    if (bindingResult.hasErrors()) {
      log.info("bindingResult = {}", bindingResult);
      // `bindingResult` 는 자동으로 넘어간다.
      return "validation/v2/addForm";
    }

    // 성공 로직
    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v2/items/{itemId}";
  }


  //  @PostMapping("/add")
  public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult,
      RedirectAttributes redirectAttributes) {

    // [V4] BindingResult는 target(item)과 objectName("item")을 감싸서 생성된 객체라 이미 알고 있다
    //      (@ModelAttribute 바로 뒤에 선언하는 건 어느 객체의 BindingResult인지 매칭하는 규칙)
    // rejectValue()/reject(): 내부에서 rejectedValue를 꺼내고 codes를 생성해 FieldError/ObjectError를 만든다
    // errorCode "required"만 넘기면 MessageCodesResolver가 생성:
    //   required.item.itemName → required.itemName → required.java.lang.String → required

    log.info("objectName={}", bindingResult.getObjectName());
    log.info("target={}", bindingResult.getTarget());

    // 검증 로직
//    ValidationUtils.rejectIfEmpty(bindingResult, "itemName", "required");
    if (!StringUtils.hasText(item.getItemName())) {
      bindingResult.rejectValue("itemName", "required");
    }
    if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1_000_000) {
      bindingResult.rejectValue("price", "range", new Object[]{1000, 1_000_000}, null);
    }
    if (item.getQuantity() == null || item.getQuantity() > 9999) {
      bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
    }

    // 특정 필드가 아닌 복합 룰 검증
    if (item.getPrice() != null && item.getQuantity() != null) {
      int resultPrice = item.getPrice() * item.getQuantity();
      if (resultPrice < 10_000) {
        bindingResult.reject("totalPriceMin", new Object[]{10_000, resultPrice},
            null);
      }
    }

    // 검증에 실패하면 다시 입력 폼으로
    if (bindingResult.hasErrors()) {
      log.info("bindingResult = {}", bindingResult);
      return "validation/v2/addForm";
    }

    // 성공 로직
    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v2/items/{itemId}";
  }


  //  @PostMapping("/add")
  public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult,
      RedirectAttributes redirectAttributes) {

    itemValidator.validate(item, bindingResult);

    if (bindingResult.hasErrors()) {
      log.info("bindingResult = {}", bindingResult);
      return "validation/v2/addForm";
    }

    // 성공 로직
    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v2/items/{itemId}";
  }


  @PostMapping("/add")
  public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult,
      RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      log.info("bindingResult = {}", bindingResult);
      return "validation/v2/addForm";
    }

    // 성공 로직
    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v2/items/{itemId}";
  }

  @GetMapping("/{itemId}/edit")
  public String editForm(@PathVariable Long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "validation/v2/editForm";
  }

  @PostMapping("/{itemId}/edit")
  public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
    itemRepository.update(itemId, item);
    return "redirect:/validation/v2/items/{itemId}";
  }

}
