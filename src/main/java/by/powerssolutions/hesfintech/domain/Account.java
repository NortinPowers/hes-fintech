package by.powerssolutions.hesfintech.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@Entity
@FieldNameConstants
@Table(name = "accounts")
@EqualsAndHashCode(callSuper = true)
public class Account extends BaseDomain implements Serializable {

    private BigDecimal balance;
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
