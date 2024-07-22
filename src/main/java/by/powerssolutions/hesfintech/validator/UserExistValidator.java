package by.powerssolutions.hesfintech.validator;

import by.powerssolutions.hesfintech.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserExistValidator implements ConstraintValidator<UserExist, String> {

    private final UserService userService;

    /**
     * Проверяет, является ли указанное имя пользователя уникальным.
     *
     * @param username                   Имя пользователя для проверки.
     * @param constraintValidatorContext Контекст валидатора ограничений.
     * @return true, если имя пользователя уникально, иначе false.
     */
    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return !userService.isUserExist(username);
    }
}
