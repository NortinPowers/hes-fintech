package by.powerssolutions.hesfintech.util;

import static by.powerssolutions.hesfintech.util.TestConstant.USER_ID;
import static by.powerssolutions.hesfintech.util.TestConstant.USER_PASSWORD;
import static by.powerssolutions.hesfintech.util.TestConstant.USER_USERNAME;

import by.powerssolutions.hesfintech.domain.Role;
import by.powerssolutions.hesfintech.domain.User;
import by.powerssolutions.hesfintech.dto.request.UserRegistrationDto;
import by.powerssolutions.hesfintech.dto.response.UserDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class UserTestBuilder {

    @Builder.Default
    private Long id = USER_ID;

    @Builder.Default
    private String username = USER_USERNAME;

    @Builder.Default
    private String password = USER_PASSWORD;

    @Builder.Default
    private Role role = RoleTestBuilder.builder().build().buildRole();

    public User buildUser() {
        User user = new User();
        user.setId(id);
        user.setPassword(password);
        user.setUsername(username);
        user.setRole(role);
        return user;
    }

    public UserDto buildUserDto() {
        UserDto user = new UserDto();
        user.setUsername(username);
        user.setRole(role.getName());
        return user;
    }

    public UserRegistrationDto buildUserRegistrationDto() {
        UserRegistrationDto user = new UserRegistrationDto();
        user.setUsername(username);
        user.setPassword(password);
        user.setVerifyPassword(password);
        return user;
    }
}
