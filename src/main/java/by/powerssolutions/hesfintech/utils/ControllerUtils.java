package by.powerssolutions.hesfintech.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class ControllerUtils {

    /**
     * Получает имя пользователя из контекста.
     *
     * @return Имя пользователя.
     */
    public static String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
