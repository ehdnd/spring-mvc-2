package io.github.ehdnd.mvc2.validation.web.validation;

import io.github.ehdnd.mvc2.validation.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return Item.class.isAssignableFrom(clazz);
    // isAssignableFrom -> item == class or item == subItem
  }

  @Override
  public void validate(Object target, Errors errors) {
    Item item = (Item) target;

    ValidationUtils.rejectIfEmpty(errors, "itemName", "required");
    if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1_000_000) {
      errors.rejectValue("price", "range", new Object[]{1000, 1_000_000}, null);
    }
    if (item.getQuantity() == null || item.getQuantity() > 9999) {
      errors.rejectValue("quantity", "max", new Object[]{9999}, null);
    }

    // 특정 필드가 아닌 복합 룰 검증
    if (item.getPrice() != null && item.getQuantity() != null) {
      int resultPrice = item.getPrice() * item.getQuantity();
      if (resultPrice < 10_000) {
        errors.reject("totalPriceMin", new Object[]{10_000, resultPrice},
            null);
      }
    }
  }
}
