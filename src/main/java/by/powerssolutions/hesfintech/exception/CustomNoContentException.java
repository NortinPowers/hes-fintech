package by.powerssolutions.hesfintech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.GONE)
public class CustomNoContentException extends RuntimeException {

    public CustomNoContentException(String message) {
        super(message);
    }

    public static CustomNoContentException of(Class<?> clazz) {
        return new CustomNoContentException("There are no " + clazz.getSimpleName().toLowerCase() + " objects");
    }
}
