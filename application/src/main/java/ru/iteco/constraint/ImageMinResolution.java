package ru.iteco.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = { ImageMinResolutionValidator.class })
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageMinResolution {

    String message() default "{ru.iteco.constraint.ImageMinResolution.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int width() default 100;

    int height() default 100;
}
