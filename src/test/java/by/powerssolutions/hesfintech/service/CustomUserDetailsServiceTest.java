package by.powerssolutions.hesfintech.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import by.powerssolutions.hesfintech.domain.User;
import by.powerssolutions.hesfintech.util.UserTestBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor
class CustomUserDetailsServiceTest {

    @Autowired
    private final CustomUserDetailsService customUserDetailsService;

    @MockBean
    private UserService userService;

    @Test
    void loadUserByUsernameShouldReturnUser_whenUserIsPresent() {
        User user = UserTestBuilder.builder()
                .build()
                .buildUser();

        when(userService.getUserByUsername(user.getUsername()))
                .thenReturn(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());

        assertEquals(userDetails.getUsername(), user.getUsername());

    }

    @Test
    void loadUserByUsernameShouldThrowUsernameNotFoundException_whenUserIsNotPresent() {
        User user = UserTestBuilder.builder()
                .build()
                .buildUser();

        when(userService.getUserByUsername(user.getUsername()))
                .thenThrow(new UsernameNotFoundException("User not found"));

        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(user.getUsername()));
    }
}
