package by.powerssolutions.hesfintech.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import by.powerssolutions.hesfintech.config.TestContainerConfig;
import by.powerssolutions.hesfintech.domain.Role;
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
@Sql(value = "classpath:sql/role/role-repository-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private String testRole;

    {
        testRole = "ROLE_USER";
    }

    @Test
    void findByNameShouldReturnRole_whenRoleIsPresent() {
        Optional<Role> optionalRole = roleRepository.findByName(testRole);

        assertTrue(optionalRole.isPresent());
        assertEquals(testRole, optionalRole.get().getName());
    }

    @Test
    void findByNameShouldReturnEmptyOptional_whenRoleIsNotPresent() {
        testRole = "NotExistRole";

        Optional<Role> optionalRole = roleRepository.findByName(testRole);

        assertFalse(optionalRole.isPresent());
    }
}
