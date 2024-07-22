package by.powerssolutions.hesfintech.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@Entity
@FieldNameConstants
@Table(name = "roles")
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseDomain implements Serializable {

    private String name;

    @OneToMany
    private List<User> users;
}
