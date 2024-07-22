package by.powerssolutions.hesfintech.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SuccessResponse extends BaseResponse {

    @Schema(description = "Success response message", example = "Message about any success")
    private String message;

    @JsonIgnore
    private Object object;

    public SuccessResponse(HttpStatus status, String message, Object object) {
        super(status.value());
        this.message = message;
        this.object = object;
    }
}
