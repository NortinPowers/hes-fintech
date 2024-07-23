package by.powerssolutions.hesfintech.controller;

import static by.powerssolutions.hesfintech.utils.Constants.SECURITY_SWAGGER;
import static by.powerssolutions.hesfintech.utils.ControllerUtils.getUsername;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import by.powerssolutions.hesfintech.dto.response.AccountResponseDto;
import by.powerssolutions.hesfintech.dto.response.AccountShortResponseDto;
import by.powerssolutions.hesfintech.model.BaseResponse;
import by.powerssolutions.hesfintech.model.ExceptionResponse;
import by.powerssolutions.hesfintech.model.SuccessResponse;
import by.powerssolutions.hesfintech.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
@SecurityRequirement(name = SECURITY_SWAGGER)
@Tag(name = "User", description = "User`s management API")
public class UserController {

    private final AccountService accountService;

    /**
     * Возвращает информацию о счете пользователя.
     *
     * @return Ответ содержащий информацию о счета пользователя.
     */
    @GetMapping
    @Operation(
            summary = "Returns the user's account",
            description = "Get news by specifying its id. Access is restricted. The response is a news with id, time, title and text ",
            tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = AccountResponseDto.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<AccountShortResponseDto> getUserAccount() {
        return accountService.getAccountByUsername(getUsername());
    }

    /**
     * Пополняет счет пользователя на указанную суммы.
     *
     * @param amount Сумма пополнения счета.
     * @return Ответ с сообщением об успешном пополнении счета и текущий баланс.
     */
    @PatchMapping("/refill/{amount}")
    @Operation(
            summary = "Top up user account",
            description = "Replenishes the user's account. Access is restricted. Returns a message about successful replenishment of the account and the balance",
            tags = "path"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = SuccessResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> refillAccount(@PathVariable BigDecimal amount) {
        return accountService.refillAccountByUsername(getUsername(), amount);
    }

    /**
     * Снимает средства со счета пользователя на указанную суммы.
     *
     * @param amount Сумма снятия счета.
     * @return Ответ с сообщением об успешном снятии со счета либо недостатке средств для снятия указанной суммы и текущий баланс.
     */
    @PatchMapping("/withdraw/{amount}")
    @Operation(
            summary = "Withdraws money from the user's account",
            description = "Withdraws money from the user's account. Access is restricted. If the balance is sufficient, it displays a message about the successful withdrawal of funds and the current balance. If there is a shortage of funds, it displays a corresponding notification",
            tags = "path"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = SuccessResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> withdrawAccount(@PathVariable BigDecimal amount) {
        return accountService.withdrawAccountByUsername(getUsername(), amount);
    }

    /**
     * Создает для пользователя новый счет, если у него еще нет открытого счета.
     *
     * @return Ответ с сообщением об успешном открытии счета.
     */
    @Operation(
            summary = "Creates an account for the user",
            description = "Creates an account for the user. Access is restricted. Returns a message about the successful creation of an account or informs that an account already exists for the user",
            tags = "post"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = SuccessResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    @PostMapping("/new/account")
    public ResponseEntity<BaseResponse> createUserAccount() {
        return accountService.createUserAccount(getUsername());
    }
}
