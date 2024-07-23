package by.powerssolutions.hesfintech.util;

import static by.powerssolutions.hesfintech.util.TestConstant.ROLE_ID;
import static by.powerssolutions.hesfintech.util.TestConstant.ROLE_NAME;

import by.powerssolutions.hesfintech.domain.Role;
import by.powerssolutions.hesfintech.domain.User;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class RoleTestBuilder {

    @Builder.Default
    private Long id = ROLE_ID;

    @Builder.Default
    private String name = ROLE_NAME;

    @Builder.Default
    private List<User> users = new ArrayList<>();

    public Role buildRole() {
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        role.setUsers(users);
        return role;
    }
}
