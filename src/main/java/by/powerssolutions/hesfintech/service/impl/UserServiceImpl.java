package by.powerssolutions.hesfintech.service.impl;

import static by.powerssolutions.hesfintech.utils.Constants.USERNAME_NOT_FOUND_EXCEPTION_MESSAGE;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.DATA_SOURCE_LOOKUP_FAILURE_EXCEPTION_MESSAGE;

import by.powerssolutions.hesfintech.domain.Role;
import by.powerssolutions.hesfintech.domain.User;
import by.powerssolutions.hesfintech.dto.request.UserRegistrationDto;
import by.powerssolutions.hesfintech.mapper.UserMapper;
import by.powerssolutions.hesfintech.repository.RoleRepository;
import by.powerssolutions.hesfintech.repository.UserRepository;
import by.powerssolutions.hesfintech.service.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Получает пользователя по его имени пользователя.
     *
     * @param username Имя пользователя для поиска.
     * @return Пользователь с указанным именем пользователя или {@code null}, если пользователь не найден.
     */
    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND_EXCEPTION_MESSAGE));
    }

    /**
     * Сохраняет данные о регистрации пользователя.
     *
     * @param userRegistrationDto Данные о регистрации пользователя для сохранения.
     */
    @Override
    public void save(UserRegistrationDto userRegistrationDto) {
        userRegistrationDto.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        User user = userMapper.toDomain(userRegistrationDto);
        Optional<Role> optionalRole = roleRepository.findByName("ROLE_USER");
        if (optionalRole.isPresent()) {
            user.setRole(optionalRole.get());
            userRepository.save(user);
        } else {
            throw new DataSourceLookupFailureException(DATA_SOURCE_LOOKUP_FAILURE_EXCEPTION_MESSAGE);
        }
    }

    /**
     * Проверяет, существует ли пользователь с указанным именем пользователя.
     *
     * @param username Имя пользователя для проверки.
     * @return {@code true}, если пользователь существует, в противном случае {@code false}.
     */
    @Override
    public boolean isUserExist(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
