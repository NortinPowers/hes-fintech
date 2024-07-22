package by.powerssolutions.hesfintech.service;

import by.powerssolutions.hesfintech.domain.User;
import by.powerssolutions.hesfintech.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    /**
     * Загружает данные о пользователе по его имени пользователя.
     *
     * @param username Имя пользователя для поиска.
     * @return Детали пользователя, упакованные в объект {@link CustomUserDetails}.
     * @throws UsernameNotFoundException Если пользователь с указанным именем не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByUsername(username);
        return new CustomUserDetails(user);
    }
}
