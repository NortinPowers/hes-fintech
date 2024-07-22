package by.powerssolutions.hesfintech.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class BaseDto implements Serializable {

    @Min(1)
    @Schema(description = "Identifier", example = "1")
    private Long id;
}
