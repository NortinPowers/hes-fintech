package by.powerssolutions.hesfintech.exception;

import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomIncorrectInputException extends RuntimeException {

    public CustomIncorrectInputException(String message) {
        super(message);
    }

    public static CustomIncorrectInputException of(BigDecimal value) {
        return new CustomIncorrectInputException("The operation is not allowed. An incorrect value was entered for the operation - " + String.valueOf(value));
    }
}
