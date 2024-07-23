package by.powerssolutions.hesfintech;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import by.powerssolutions.hesfintech.controller.AccountController;
import by.powerssolutions.hesfintech.controller.AuthController;
import by.powerssolutions.hesfintech.controller.UserController;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor
class HesFintechApplicationTest {

    private final AccountController accountController;
    private final AuthController authController;
    private final UserController userController;

    @Test
    void adminControllerMustBeNotNull_whenContextLoaded() {
        assertThat(accountController).isNotNull();
    }

    @Test
    void authControllerMustBeNotNull_whenContextLoaded() {
        assertThat(authController).isNotNull();
    }

    @Test
    void userControllerMustBeNotNull_whenContextLoaded() {
        assertThat(userController).isNotNull();
    }
}
