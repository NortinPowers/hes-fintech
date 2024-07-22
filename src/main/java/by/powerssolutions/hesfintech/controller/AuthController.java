package by.powerssolutions.hesfintech.controller;

import static by.powerssolutions.hesfintech.utils.Constants.USER;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.CREATION_MESSAGE;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.getSuccessResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import by.powerssolutions.hesfintech.dto.request.JwtRequest;
import by.powerssolutions.hesfintech.dto.request.UserRegistrationDto;
import by.powerssolutions.hesfintech.dto.response.JwtResponse;
import by.powerssolutions.hesfintech.model.BaseResponse;
import by.powerssolutions.hesfintech.model.ErrorValidationResponse;
import by.powerssolutions.hesfintech.model.ExceptionResponse;
import by.powerssolutions.hesfintech.model.SuccessResponse;
import by.powerssolutions.hesfintech.service.AuthService;
import by.powerssolutions.hesfintech.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "User`s management API")
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    /**
     * Создает JWT-токен на основе данных из запроса аутентификации.
     *
     * @param request Запрос с данными о пользователе (имя пользователя и пароль).
     * @return Ответ со сгенерированным JWT-токеном.
     * @throws BadCredentialsException Если предоставленные учетные данные недействительны.
     */
    @PostMapping
    @Operation(
            summary = "Create user token",
            description = "Get a jwt-token by credentials. The response is jwt-token",
            tags = "post"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = JwtRequest.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorValidationResponse.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<JwtResponse> createAuthToken(@RequestBody @Valid JwtRequest request) throws BadCredentialsException {
        String token = authService.getToken(request);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    /**
     * Создает нового пользователя на основе данных о регистрации.
     *
     * @param userRegistrationDto Данные о регистрации пользователя.
     * @return Ответ с сообщением об успешном создании пользователя.
     */
    @PostMapping("/registration")
    @Operation(
            summary = "Create new user",
            description = "Create new user. The response is a message about the successful creation of a user",
            tags = "post"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = SuccessResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorValidationResponse.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> createNewUser(@RequestBody @Valid UserRegistrationDto userRegistrationDto) {
        userService.save(userRegistrationDto);
        return ResponseEntity.ok(getSuccessResponse(CREATION_MESSAGE, USER));
    }
}
