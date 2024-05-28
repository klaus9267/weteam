package weteam.backend.domain.common.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import weteam.backend.application.handler.exception.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;

import java.lang.reflect.Field;

public class AtLeastOneNotNullValidator implements ConstraintValidator<AtLeastOneNotNull, Object> {
  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext context) {
    if (obj == null) {
      return false;
    }

    try {
      for (Field field : obj.getClass().getDeclaredFields()) {
        field.setAccessible(true);
        if (field.get(obj) != null) {
          return true;
        }
      }
    } catch (IllegalAccessException e) {
      throw new CustomException(CustomErrorCode.ILLEGAL_ACCESS);
    }

    return false;
  }
}
