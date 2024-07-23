package by.powerssolutions.hesfintech.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.powerssolutions.hesfintech.domain.Role;
import by.powerssolutions.hesfintech.domain.User;
import by.powerssolutions.hesfintech.dto.request.UserRegistrationDto;
import by.powerssolutions.hesfintech.mapper.UserMapper;
import by.powerssolutions.hesfintech.repository.RoleRepository;
import by.powerssolutions.hesfintech.repository.UserRepository;
import by.powerssolutions.hesfintech.service.UserService;
import by.powerssolutions.hesfintech.util.RoleTestBuilder;
import by.powerssolutions.hesfintech.util.UserTestBuilder;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    private final User user;
    private final Role role;

    {
        role = RoleTestBuilder.builder()
                .build()
                .buildRole();
        user = UserTestBuilder.builder()
                .build()
                .buildUser();
    }

    @Nested
    class TestGetUserByUsername {

        @Test
        void getUserByUsernameShouldReturnUser() {
            when(userRepository.findByUsername(user.getUsername()))
                    .thenReturn(Optional.of(user));

            User actual = userService.getUserByUsername(user.getUsername());

            assertEquals(user, actual);
        }

        @Test
        void getUserByUsernameShouldThrowUsernameNotFoundException_whenUserNotFound() {
            when(userRepository.findByUsername(any()))
                    .thenThrow(new UsernameNotFoundException("not matter"));

            assertThrows(UsernameNotFoundException.class, () -> userService.getUserByUsername(any()));
        }
    }

    @Nested
    class TestSaveUser {

        private final UserRegistrationDto userRegistrationDto = UserTestBuilder.builder()
                .build()
                .buildUserRegistrationDto();

        @Test
        void saveShouldSaveUser() {
            user.setRole(null);
            String notEncryptedPassword = userRegistrationDto.getPassword();

            when(passwordEncoder.encode(user.getPassword()))
                    .thenReturn("encrypted password");
            when(userMapper.toDomain(userRegistrationDto))
                    .thenReturn(user);
            when(roleRepository.findByName(any()))
                    .thenReturn(Optional.of(role));
            when(userRepository.save(user))
                    .thenReturn(user);

            userService.save(userRegistrationDto);

            assertEquals(user.getRole(), role);
            assertNotEquals(notEncryptedPassword, userRegistrationDto.getPassword());
        }

        @Test
        void saveShouldThrowDataSourceLookupFailureException_whenRoleNotFound() {
            when(passwordEncoder.encode(user.getPassword()))
                    .thenReturn("encrypted password");
            when(userMapper.toDomain(userRegistrationDto))
                    .thenReturn(user);
            when(roleRepository.findByName(user.getRole()
                    .getName())).thenReturn(Optional.empty());

            assertThrows(DataSourceLookupFailureException.class, () -> userService.save(userRegistrationDto));

            verify(userRepository, never()).save(user);
        }
    }

    @Nested
    class TestIsUserExist {

        private Optional<User> optionalUser;

        @Test
        void isUserExistShouldReturnTrue_whenUserFound() {
            optionalUser = Optional.of(user);

            when(userRepository.findByUsername(any()))
                    .thenReturn(optionalUser);

            assertTrue(userService.isUserExist(any()));
        }

        @Test
        void isUserExistShouldReturnFalse_whenUserNotFound() {
            optionalUser = Optional.empty();

            when(userRepository.findByUsername(any()))
                    .thenReturn(optionalUser);

            assertFalse(userService.isUserExist(any()));
        }
    }
}
