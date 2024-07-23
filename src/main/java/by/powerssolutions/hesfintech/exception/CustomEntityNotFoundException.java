package by.powerssolutions.hesfintech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomEntityNotFoundException extends RuntimeException {

    public CustomEntityNotFoundException(String message) {
        super(message);
    }

    public static CustomEntityNotFoundException of(Class<?> clazz, Object field) {
        return new CustomEntityNotFoundException(clazz.getSimpleName() + " - " + field + " - does not exist");
    }
}
