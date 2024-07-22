package by.powerssolutions.hesfintech.controller;

import static by.powerssolutions.hesfintech.utils.Constants.SECURITY_SWAGGER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import by.powerssolutions.hesfintech.dto.response.AccountResponseDto;
import by.powerssolutions.hesfintech.model.BaseResponse;
import by.powerssolutions.hesfintech.model.ExceptionResponse;
import by.powerssolutions.hesfintech.model.SuccessResponse;
import by.powerssolutions.hesfintech.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/accounts")
@RequiredArgsConstructor
@SecurityRequirement(name = SECURITY_SWAGGER)
@Tag(name = "Account", description = "Account`s management API")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    @Operation(
            summary = "Retrieves a page of accounts from the list of all accounts depending on the page",
            description = "Collect accounts from the list of all accounts. Default page size - 15 elements. Access is restricted. The answer is an array of accounts with balance, status and username for each of the array element",
            tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = AccountResponseDto.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "410", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})

    public ResponseEntity<Page<AccountResponseDto>> getAll(@Parameter(name = "Pageable parameters", example = "page=0&size=15&sort=created,asc")
                                                           @PageableDefault(size = 15)
                                                           @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(accountService.getAll(pageable));
    }

    @PatchMapping("/block/{id}")
    @Operation(
            summary = "Blocks the user's account",
            description = "Update the account status by specifying its id. Access is restricted. The response is a message about the successful changed a status",
            tags = "path"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = SuccessResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> blockAccount(@PathVariable("id") @Min(1) Long id) {
        return accountService.blockAccount(id);
    }

    @PatchMapping("/activate/{id}")
    @Operation(
            summary = "Activates the user's account",
            description = "Returns the user's account. The response is account with balance and status",
            tags = "path"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = SuccessResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> activateAccount(@PathVariable("id") @Min(1) Long id) {
        return accountService.activateAccount(id);
    }
}
