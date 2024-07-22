package by.powerssolutions.hesfintech.controller;

import static by.powerssolutions.hesfintech.utils.Constants.SECURITY_SWAGGER;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
@SecurityRequirement(name = SECURITY_SWAGGER)
@Tag(name = "User", description = "User`s management API")
public class UserController {

    private final AccountService accountService;

    @GetMapping
    @Operation(
            summary = "Returns the user's account",
            description = "Get news by specifying its id. The response is a news with id, time, title and text ",
            tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = AccountResponseDto.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<AccountShortResponseDto> getUserAccount() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return accountService.getAccountByUsername(username);
    }

    @PatchMapping("/refill/{amount}")
    @Operation(
            summary = "Top up user account",
            description = "Replenishes the user's account. Returns a message about successful replenishment of the account and the balance",
            tags = "path"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = SuccessResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> refillAccount(@PathVariable BigDecimal amount) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return accountService.refillAccountByUsername(username, amount);
    }

    @PatchMapping("/withdraw/{amount}")
    @Operation(
            summary = "Withdraws money from the user's account",
            description = "Withdraws money from the user's account. If the balance is sufficient, it displays a message about the successful withdrawal of funds and the current balance. If there is a shortage of funds, it displays a corresponding notification",
            tags = "path"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = SuccessResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> withdrawAccount(@PathVariable BigDecimal amount) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return accountService.withdrawAccountByUsername(username, amount);
    }
}
