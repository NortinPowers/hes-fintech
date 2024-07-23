package by.powerssolutions.hesfintech.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import by.powerssolutions.hesfintech.service.UserService;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserExistValidatorTest {

    @Mock
    private UserService userService;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @InjectMocks
    private UserExistValidator userExistValidator;

    private final String name = "Test";
    private boolean flag;

    @Test
    void isValidShouldReturnTrue_whenNameValid() {
        when(userService.isUserExist(name))
                .thenReturn(flag);

        assertTrue(userExistValidator.isValid(name, constraintValidatorContext));
    }

    @Test
    void isValidShouldReturnFalse_whenNameInvalid() {
        flag = true;

        when(userService.isUserExist(name))
                .thenReturn(flag);

        assertFalse(userExistValidator.isValid(name, constraintValidatorContext));
    }
}
