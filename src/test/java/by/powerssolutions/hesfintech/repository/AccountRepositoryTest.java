package by.powerssolutions.hesfintech.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import by.powerssolutions.hesfintech.config.TestContainerConfig;
import by.powerssolutions.hesfintech.domain.Account;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(value = "classpath:sql/account/account-repository-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AccountRepositoryTest {

    @Autowired
    private AccountRepository repository;

    @Test
    void findByUsernameShouldReturnAccount_whenUserIsPresent() {
        Optional<Account> optionalAccount = repository.findByUsername("user");

        assertTrue(optionalAccount.isPresent());
        assertEquals("user", optionalAccount.get().getUser().getUsername());
    }

    @Test
    void findByUsernameShouldReturnEmptyOptional_whenUserIsNotPresent() {
        Optional<Account> optionalAccount = repository.findByUsername("nonExistUser");

        assertFalse(optionalAccount.isPresent());
    }
}
