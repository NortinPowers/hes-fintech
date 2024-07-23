package by.powerssolutions.hesfintech.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Access token")
public class JwtResponse {

    @Schema(description = "Jwt-token", example = "XXX.YYYY.ZZZZ")
    private String token;
}
