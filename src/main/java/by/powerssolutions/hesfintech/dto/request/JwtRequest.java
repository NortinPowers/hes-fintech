package by.powerssolutions.hesfintech.dto.request;

import static by.powerssolutions.hesfintech.utils.Constants.PASSWORD_NOT_BLANK;
import static by.powerssolutions.hesfintech.utils.Constants.USERNAME_NOT_BLANK;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Entity of User")
public class JwtRequest {

    @NotBlank(message = USERNAME_NOT_BLANK)
    @Schema(description = "Username", example = "clientA")
    private String username;

    @NotBlank(message = PASSWORD_NOT_BLANK)
    @Schema(description = "Password", example = "rebel")
    private String password;
}
