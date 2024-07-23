package by.powerssolutions.hesfintech.controller;

import static by.powerssolutions.hesfintech.utils.ResponseUtils.SUCCESS_ACCOUNT_CREATION_MESSAGE;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.SUCCESS_ACCOUNT_REFILL_MESSAGE;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.WITHDRAWAL_ERROR_MESSAGE;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.getExceptionResponse;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.getSuccessResponse;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.setBalanceDisplay;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.powerssolutions.hesfintech.domain.Account;
import by.powerssolutions.hesfintech.dto.response.AccountShortResponseDto;
import by.powerssolutions.hesfintech.exception.CustomAccountExistException;
import by.powerssolutions.hesfintech.exception.CustomEntityNotFoundException;
import by.powerssolutions.hesfintech.exception.CustomIncorrectInputException;
import by.powerssolutions.hesfintech.model.BaseResponse;
import by.powerssolutions.hesfintech.model.ExceptionResponse;
import by.powerssolutions.hesfintech.service.AccountService;
import by.powerssolutions.hesfintech.util.AccountTestBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RequiredArgsConstructor
class UserControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper mapper;

    @MockBean
    private AccountService accountService;

    private String url;

    @Nested
    class getUserAccountTest {

        {
            url = "/api/user";
        }

        @Test
        @WithAnonymousUser
        void getUserAccountShouldReturnMessageAboutTokenNeeded_whenUserIsAnonymous() throws Exception {
            mockMvc.perform(get(url))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("error").value("To get access you need token"));

        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        void getUserAccountShouldReturnUserAccountInfo_whenUserIsUserWithAccount() throws Exception {
            AccountShortResponseDto responseDto = AccountTestBuilder.builder()
                    .build()
                    .buildAccountShortResponseDto();
            ResponseEntity<AccountShortResponseDto> responseEntity = ResponseEntity.ok(responseDto);

            when(accountService.getAccountByUsername("user"))
                    .thenReturn(responseEntity);

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(responseDto)));
        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        void getUserAccountShouldReturnExceptionResponse_whenUserIsUserWithoutAccount() throws Exception {
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(Account.class, "user");
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_FOUND,
                    exception.getMessage(),
                    exception
            );

            when(accountService.getAccountByUsername("user"))
                    .thenThrow(exception);

            mockMvc.perform(get(url))
                    .andExpect(status().isNotFound())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }

    @Nested
    class refillAccountTest {

        private String amountValue;
        private BigDecimal amount;

        {
            url = "/api/user/refill/";
            amount = BigDecimal.TEN;
            amountValue = String.valueOf(amount);
        }

        @Test
        @WithAnonymousUser
        void refillAccountShouldReturnMessageAboutTokenNeeded_whenUserIsAnonymous() throws Exception {
            mockMvc.perform(patch(url + amountValue))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("error").value("To get access you need token"));

        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        void refillAccountShouldReturnUserAccountInfo_whenUserIsUserWithAccountAndCorrectAmount() throws Exception {
            BigDecimal balance = BigDecimal.ONE;
            BigDecimal updatedBalance = balance.add(amount);
            ResponseEntity<BaseResponse> responseEntity = ResponseEntity.ok(getSuccessResponse(SUCCESS_ACCOUNT_REFILL_MESSAGE, setBalanceDisplay(updatedBalance)));

            when(accountService.refillAccountByUsername("user", amount))
                    .thenReturn(responseEntity);

            mockMvc.perform(patch(url + amountValue))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("message").value(String.format(SUCCESS_ACCOUNT_REFILL_MESSAGE, setBalanceDisplay(updatedBalance))));
        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        void refillAccountShouldReturnUserExceptionResponse_whenUserIsUserWithoutAccount() throws Exception {
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(Account.class, "user");
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_FOUND,
                    exception.getMessage(),
                    exception
            );

            when(accountService.refillAccountByUsername("user", amount))
                    .thenThrow(exception);

            mockMvc.perform(patch(url + amount))
                    .andExpect(status().isNotFound())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        void refillAccountShouldReturnUserExceptionResponse_whenUserIsUserWithAccountAndIncorrectAmount() throws Exception {
            amount = BigDecimal.valueOf(-5);
            amountValue = String.valueOf(amount);
            CustomIncorrectInputException exception = CustomIncorrectInputException.of(amount);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.BAD_REQUEST,
                    exception.getMessage(),
                    exception
            );

            when(accountService.refillAccountByUsername("user", amount))
                    .thenThrow(exception);

            mockMvc.perform(patch(url + amountValue))
                    .andExpect(status().isBadRequest())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }

    @Nested
    class withdrawAccountTest {

        private String amountValue;
        private BigDecimal amount;

        {
            url = "/api/user/withdraw/";
            amount = BigDecimal.TEN;
            amountValue = String.valueOf(amount);
        }

        @Test
        @WithAnonymousUser
        void withdrawAccountShouldReturnMessageAboutTokenNeeded_whenUserIsAnonymous() throws Exception {
            mockMvc.perform(patch(url + amountValue))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("error").value("To get access you need token"));

        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        void withdrawAccountShouldReturnUserAccountInfo_whenUserIsUserWithAccountAndCorrectAmount() throws Exception {
            BigDecimal balance = BigDecimal.valueOf(20);
            BigDecimal updatedBalance = balance.add(amount);
            ResponseEntity<BaseResponse> responseEntity = ResponseEntity.ok(getSuccessResponse(SUCCESS_ACCOUNT_REFILL_MESSAGE, setBalanceDisplay(updatedBalance)));

            when(accountService.withdrawAccountByUsername("user", amount))
                    .thenReturn(responseEntity);

            mockMvc.perform(patch(url + amountValue))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("message").value(String.format(SUCCESS_ACCOUNT_REFILL_MESSAGE, setBalanceDisplay(updatedBalance))));
        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        void withdrawAccountShouldReturnUserExceptionResponse_whenUserIsUserWithoutAccount() throws Exception {
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(Account.class, "user");
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_FOUND,
                    exception.getMessage(),
                    exception
            );

            when(accountService.withdrawAccountByUsername("user", amount))
                    .thenThrow(exception);

            mockMvc.perform(patch(url + amount))
                    .andExpect(status().isNotFound())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        void withdrawAccountShouldReturnUserExceptionResponse_whenUserIsUserWithAccountAndIncorrectAmount() throws Exception {
            amount = BigDecimal.valueOf(-5);
            amountValue = String.valueOf(amount);
            CustomIncorrectInputException exception = CustomIncorrectInputException.of(amount);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.BAD_REQUEST,
                    exception.getMessage(),
                    exception
            );

            when(accountService.withdrawAccountByUsername("user", amount))
                    .thenThrow(exception);

            mockMvc.perform(patch(url + amountValue))
                    .andExpect(status().isBadRequest())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        void withdrawAccountShouldReturnUserExceptionResponse_whenUserIsUserWithAccountButLowBalance() throws Exception {
            BigDecimal balance = BigDecimal.ONE;
            ResponseEntity<BaseResponse> responseEntity = ResponseEntity.ok(getSuccessResponse(WITHDRAWAL_ERROR_MESSAGE, setBalanceDisplay(balance)));

            when(accountService.withdrawAccountByUsername("user", amount))
                    .thenReturn(responseEntity);

            mockMvc.perform(patch(url + amountValue))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("message").value(String.format(WITHDRAWAL_ERROR_MESSAGE, setBalanceDisplay(balance))));
        }
    }

    @Nested
    class createUserAccountTest {

        {
            url = "/api/user/new/account";
        }

        @Test
        @WithAnonymousUser
        void createUserAccountShouldReturnMessageAboutTokenNeeded_whenUserIsAnonymous() throws Exception {
            mockMvc.perform(post(url))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("error").value("To get access you need token"));

        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        void createUserAccountShouldReturnUserSuccessMessage_whenUserAccountCreated() throws Exception {
            ResponseEntity<BaseResponse> responseEntity = ResponseEntity.ok(getSuccessResponse(SUCCESS_ACCOUNT_CREATION_MESSAGE, "user"));

            when(accountService.createUserAccount("user"))
                    .thenReturn(responseEntity);

            mockMvc.perform(post(url))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("message").value(String.format(SUCCESS_ACCOUNT_CREATION_MESSAGE, "user")));
        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        void createUserAccountShouldReturnUserExceptionResponse_whenUserAlreadyHaveAccount() throws Exception {
            CustomAccountExistException exception = new CustomAccountExistException("The account for the user user has already been opened.");
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.BAD_REQUEST,
                    exception.getMessage(),
                    exception
            );

            when(accountService.createUserAccount("user"))
                    .thenThrow(exception);

            mockMvc.perform(post(url))
                    .andExpect(status().isBadRequest())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }
}
