package by.powerssolutions.hesfintech.validator;

import by.powerssolutions.hesfintech.dto.request.UserRegistrationDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchingValidator implements ConstraintValidator<PasswordMatching, UserRegistrationDto> {

    /**
     * Проверяет, являются ли предоставленные данные о регистрации пользователя действительными (совпадают ли пароли).
     *
     * @param userRegistrationDto        Данные о регистрации пользователя для проверки.
     * @param constraintValidatorContext Контекст для ограничений проверки.
     * @return {@code true}, если данные о регистрации пользователя действительны, в противном случае {@code false}.
     */
    @Override
    public boolean isValid(UserRegistrationDto userRegistrationDto, ConstraintValidatorContext constraintValidatorContext) {
        return userRegistrationDto.getPassword().equals(userRegistrationDto.getVerifyPassword());
    }
}
