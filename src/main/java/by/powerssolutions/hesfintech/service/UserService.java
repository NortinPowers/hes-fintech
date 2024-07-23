package by.powerssolutions.hesfintech.service;

import by.powerssolutions.hesfintech.domain.User;
import by.powerssolutions.hesfintech.dto.request.UserRegistrationDto;

public interface UserService {

    /**
     * Получает пользователя по его имени пользователя.
     *
     * @param username Имя пользователя для поиска.
     * @return Пользователь с указанным именем пользователя или {@code null}, если пользователь не найден.
     */
    User getUserByUsername(String username);

    /**
     * Сохраняет данные о регистрации пользователя.
     *
     * @param userRegistrationDto Данные о регистрации пользователя для сохранения.
     */
    void save(UserRegistrationDto userRegistrationDto);

    /**
     * Проверяет, существует ли пользователь с указанным именем пользователя.
     *
     * @param username Имя пользователя для проверки.
     * @return {@code true}, если пользователь существует, в противном случае {@code false}.
     */
    boolean isUserExist(String username);
}
