package by.powerssolutions.hesfintech.dto.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountShortResponseDto {

    private BigDecimal balance;
    private Boolean status;
}
