package by.powerssolutions.hesfintech.validator;

import static by.powerssolutions.hesfintech.utils.Constants.USER_EXIST;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = UserExistValidator.class)
public @interface UserExist {

    String message() default USER_EXIST;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
