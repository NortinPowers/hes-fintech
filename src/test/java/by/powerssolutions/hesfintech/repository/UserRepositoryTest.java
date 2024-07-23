package by.powerssolutions.hesfintech.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import by.powerssolutions.hesfintech.config.TestContainerConfig;
import by.powerssolutions.hesfintech.domain.User;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
@ActiveProfiles("test")
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(value = "classpath:sql/user/user-repository-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private String username;

    {
        username = "admin";
    }

    @Test
    void findByUsernameShouldReturnUser_whenUserIsPresent() {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        assertTrue(optionalUser.isPresent());
        assertEquals(username, optionalUser.get().getUsername());
    }

    @Test
    void findByUsernameShouldReturnEmptyOptional_whenUserIsNotPresent() {
        username = "nonExistUser";
        Optional<User> optionalUser = userRepository.findByUsername(username);

        assertFalse(optionalUser.isPresent());
    }
}
