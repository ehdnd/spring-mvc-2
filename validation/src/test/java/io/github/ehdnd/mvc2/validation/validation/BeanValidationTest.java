package io.github.ehdnd.mvc2.validation.validation;

import io.github.ehdnd.mvc2.validation.domain.item.Item;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class BeanValidationTest {

  @Test
  void beanValidation() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    Item item = new Item();
    item.setItemName(" ");
    item.setPrice(0);
    item.setQuantity(10000);

    Set<ConstraintViolation<Item>> violations = validator.validate(item);
    for (ConstraintViolation<Item> violation : violations) {
      System.out.println("violation=" + violation);
      System.out.println("violation.message=" + violation.getMessage());
    }
  }

}
