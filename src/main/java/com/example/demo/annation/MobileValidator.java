package com.example.demo.annation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @ClassName MobileValidator
 * @Description TODO
 * @Date 2023/10/3 21:27
 */
public class MobileValidator implements ConstraintValidator<MobileValidate, String> {

    private static final Pattern MOBILE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    @Override
    public void initialize(MobileValidate constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // 如果值为null, 使用@NotNull来验证
        }
        return MOBILE_PATTERN.matcher(value).matches();
    }
}

