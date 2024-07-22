package by.powerssolutions.hesfintech.validator;

import static by.powerssolutions.hesfintech.utils.Constants.PASSWORD_NOT_MATCHING;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordMatchingValidator.class)
public @interface PasswordMatching {

    String message() default PASSWORD_NOT_MATCHING;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
