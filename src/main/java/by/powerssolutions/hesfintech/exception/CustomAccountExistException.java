package by.powerssolutions.hesfintech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomAccountExistException extends RuntimeException {

    public CustomAccountExistException(String message) {
        super(message);
    }

    public static CustomAccountExistException of(String username) {
        return new CustomAccountExistException("The account for the user " + username + " has already been opened.");
    }
}
