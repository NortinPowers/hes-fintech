package by.powerssolutions.hesfintech.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLES = "roles";
    public static final String USER = "User";
    public static final String USERNAME_NOT_FOUND_EXCEPTION_MESSAGE = "User not found";
    public static final String USERNAME_NOT_BLANK = "Enter username";
    public static final String PASSWORD_NOT_BLANK = "Enter password";
    public static final String USERNAME_PATTERN = "[a-zA-Z0-9]{3,30}";
    public static final String PASSWORD_PATTERN = "[a-zA-Z0-9]{4,30}";
    public static final String PASSWORD_NOT_MATCHING = "The entered passwords do not match";
    public static final String USER_EXIST = "User with this username already exist";
    public static final String SECURITY_SWAGGER = "bearerAuth";
}
