package by.powerssolutions.hesfintech.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import by.powerssolutions.hesfintech.domain.User;
import by.powerssolutions.hesfintech.dto.request.JwtRequest;
import by.powerssolutions.hesfintech.security.CustomUserDetails;
import by.powerssolutions.hesfintech.token.JwtTokenManager;
import by.powerssolutions.hesfintech.util.UserTestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class AuthServiceIntegrationTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @MockBean
    private JwtTokenManager jwtTokenManager;

    @MockBean
    private AuthenticationManager authenticationManager;

    private final String username;
    private final JwtRequest request;
    private final User user;

    {
        user = UserTestBuilder.builder()
                .withRole(null)
                .build()
                .buildUser();
        username = user.getUsername();
        request = new JwtRequest();
        request.setUsername(username);
        request.setPassword(user.getPassword());
    }

    @Test
    public void getTokenShouldReturnToken_whenCalled() {
        UserDetails userDetails = new CustomUserDetails(user);
        String expectedToken = "testToken";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userDetailsService.loadUserByUsername(username))
                .thenReturn(userDetails);
        when(jwtTokenManager.generateJwtToken(userDetails))
                .thenReturn(expectedToken);

        String token = authService.getToken(request);

        assertEquals(token, expectedToken);
    }

    @Test
    public void getTokenShouldThrowBadCredentialsException_whenUserNotAuthentication() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("not matter"));

        assertThrows(BadCredentialsException.class, () -> authService.getToken(request));

        verify(authenticationManager, atLeastOnce()).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(userDetailsService);
        verifyNoInteractions(jwtTokenManager);
    }

    @Test
    public void getTokenShouldThrowUsernameNotFoundException_userNotFound() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userDetailsService.loadUserByUsername(username))
                .thenThrow(new UsernameNotFoundException("not matter"));

        assertThrows(UsernameNotFoundException.class, () -> authService.getToken(request));

        verifyNoInteractions(jwtTokenManager);
    }
}
