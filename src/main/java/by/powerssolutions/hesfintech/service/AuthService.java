package by.powerssolutions.hesfintech.service;

import by.powerssolutions.hesfintech.dto.request.JwtRequest;
import by.powerssolutions.hesfintech.token.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenManager jwtTokenManager;

    /**
     * Получает JWT-токен на основе данных из запроса.
     *
     * @param request Запрос с данными о пользователе (имя пользователя и пароль).
     * @return Сгенерированный JWT-токен.
     */
    public String getToken(JwtRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        return jwtTokenManager.generateJwtToken(userDetails);
    }
}
