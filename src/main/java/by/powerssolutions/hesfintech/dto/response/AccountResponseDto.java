package by.powerssolutions.hesfintech.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Account of User")
public class AccountResponseDto {

    @Schema(description = "Account balance", example = "50.00")
    private BigDecimal balance;

    @Schema(description = "Account status", example = "true")
    private Boolean status;

    @Schema(description = "Username", example = "clientA")
    private String username;
}
