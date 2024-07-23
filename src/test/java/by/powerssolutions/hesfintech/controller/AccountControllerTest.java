package by.powerssolutions.hesfintech.controller;

import static by.powerssolutions.hesfintech.util.TestConstant.PAGE_NUMBER;
import static by.powerssolutions.hesfintech.util.TestConstant.PAGE_SIZE;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.ACTIVATE_ACCOUNT_MESSAGE;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.BLOCK_ACCOUNT_MESSAGE;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.DATA_SOURCE_LOOKUP_FAILURE_EXCEPTION_MESSAGE;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.ENTITY_NOT_FOUND_EXCEPTION_MESSAGE;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.getExceptionResponse;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.getSuccessResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.powerssolutions.hesfintech.domain.Account;
import by.powerssolutions.hesfintech.dto.response.AccountResponseDto;
import by.powerssolutions.hesfintech.exception.CustomNoContentException;
import by.powerssolutions.hesfintech.model.BaseResponse;
import by.powerssolutions.hesfintech.model.ExceptionResponse;
import by.powerssolutions.hesfintech.service.AccountService;
import by.powerssolutions.hesfintech.util.AccountTestBuilder;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RequiredArgsConstructor
class AccountControllerTest {

    private final MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    private String url;

    @Nested
    class getAllTest {

        {
            url = "/api/accounts";
        }

        @Test
        @WithAnonymousUser
        void getAllUserShouldReturnMessageAboutTokenNeeded_whenUserIsAnonymous() throws Exception {
            mockMvc.perform(get(url))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("error").value("To get access you need token"));

        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        void getAllUserShouldReturnAccessDeniedMessage_whenUserRoleIsUser() throws Exception {
            ExceptionResponse exceptionResponse = new ExceptionResponse(
                    FORBIDDEN,
                    "Access Denied",
                    "AccessDeniedException"
            );

            mockMvc.perform(get(url))
                    .andExpect(status().isForbidden())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(exceptionResponse.getStatus()),
                            jsonPath("$.message").value(exceptionResponse.getMessage()),
                            jsonPath("$.type").value(exceptionResponse.getType()));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void getAllShouldReturnExceptionResponse_whenCommentListIsEmpty() throws Exception {
            PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
            CustomNoContentException exception = CustomNoContentException.of(Account.class);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.GONE,
                    exception.getMessage(),
                    exception
            );

            when(accountService.getAll(pageRequest))
                    .thenThrow(exception);

            mockMvc.perform(get(url))
                    .andExpect(status().isGone())
                    .andExpectAll(
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void getAllUserShouldReturnAccountsList_whenUserIsAdmin() throws Exception {
            AccountResponseDto responseDto = AccountTestBuilder.builder()
                    .build()
                    .buildAccountResponseDto();
            List<AccountResponseDto> accounts = List.of(responseDto);
            PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
            PageImpl<AccountResponseDto> page = new PageImpl<>(accounts);

            when(accountService.getAll(pageRequest))
                    .thenReturn(page);

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.content").isArray(),
                            jsonPath("$.content[0].balance").value(responseDto.getBalance()),
                            jsonPath("$.content[0].status").value(responseDto.getStatus()),
                            jsonPath("$.content[0].username").value(responseDto.getUsername()));
        }
    }

    @Nested
    class blockAccountTest {

        private final Long id;

        {
            url = "/api/accounts/block/";
            id = 2L;
        }

        @Test
        @WithAnonymousUser
        void blockAccountUserShouldReturnMessageAboutTokenNeeded_whenUserIsAnonymous() throws Exception {
            mockMvc.perform(patch(url + id))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("error").value("To get access you need token"));

        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        void blockAccountUserShouldReturnAccessDeniedMessage_whenUserRoleIsUser() throws Exception {
            ExceptionResponse exceptionResponse = new ExceptionResponse(
                    FORBIDDEN,
                    "Access Denied",
                    "AccessDeniedException"
            );

            mockMvc.perform(patch(url + id))
                    .andExpect(status().isForbidden())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(exceptionResponse.getStatus()),
                            jsonPath("$.message").value(exceptionResponse.getMessage()),
                            jsonPath("$.type").value(exceptionResponse.getType()));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void blockAccountShouldReturnSuccessResponse_whenUserIsAdmin() throws Exception {
            ResponseEntity<BaseResponse> responseEntity = ResponseEntity.ok(getSuccessResponse(BLOCK_ACCOUNT_MESSAGE, String.valueOf(id)));

            when(accountService.blockAccount(id))
                    .thenReturn(responseEntity);

            mockMvc.perform(patch(url + id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("message").value(String.format(BLOCK_ACCOUNT_MESSAGE, String.valueOf(id))));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void blockAccountShouldReturnExceptionResponse_whenDbError() throws Exception {
            DataSourceLookupFailureException exception = new DataSourceLookupFailureException("it does not matter");
            ExceptionResponse response = getExceptionResponse(
                    INTERNAL_SERVER_ERROR,
                    DATA_SOURCE_LOOKUP_FAILURE_EXCEPTION_MESSAGE,
                    exception
            );

            doThrow(exception)
                    .when(accountService).blockAccount(any());

            mockMvc.perform(patch(url + id))
                    .andExpect(status().isInternalServerError())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void blockAccountShouldReturnExceptionResponse_whenUserNotFound() throws Exception {
            EntityNotFoundException exception = new EntityNotFoundException(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE);
            ExceptionResponse response = getExceptionResponse(
                    NOT_FOUND,
                    ENTITY_NOT_FOUND_EXCEPTION_MESSAGE,
                    exception
            );

            doThrow(exception)
                    .when(accountService).blockAccount(any());

            mockMvc.perform(patch(url + id))
                    .andExpect(status().isNotFound())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }

    @Nested
    class activateAccountTest {

        private final Long id;

        {
            url = "/api/accounts/activate/";
            id = 2L;
        }

        @Test
        @WithAnonymousUser
        void activateAccountUserShouldReturnMessageAboutTokenNeeded_whenUserIsAnonymous() throws Exception {
            mockMvc.perform(patch(url + id))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("error").value("To get access you need token"));

        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        void activateAccountUserShouldReturnAccessDeniedMessage_whenUserRoleIsUser() throws Exception {
            ExceptionResponse exceptionResponse = new ExceptionResponse(
                    FORBIDDEN,
                    "Access Denied",
                    "AccessDeniedException"
            );

            mockMvc.perform(patch(url + id))
                    .andExpect(status().isForbidden())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(exceptionResponse.getStatus()),
                            jsonPath("$.message").value(exceptionResponse.getMessage()),
                            jsonPath("$.type").value(exceptionResponse.getType()));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void activateAccountShouldReturnSuccessResponse_whenUserIsAdmin() throws Exception {
            ResponseEntity<BaseResponse> responseEntity = ResponseEntity.ok(getSuccessResponse(ACTIVATE_ACCOUNT_MESSAGE, String.valueOf(id)));

            when(accountService.activateAccount(id))
                    .thenReturn(responseEntity);

            mockMvc.perform(patch(url + id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("message").value(String.format(ACTIVATE_ACCOUNT_MESSAGE, String.valueOf(id))));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void activateAccountShouldReturnExceptionResponse_whenDbError() throws Exception {
            DataSourceLookupFailureException exception = new DataSourceLookupFailureException("it does not matter");
            ExceptionResponse response = getExceptionResponse(
                    INTERNAL_SERVER_ERROR,
                    DATA_SOURCE_LOOKUP_FAILURE_EXCEPTION_MESSAGE,
                    exception
            );

            doThrow(exception)
                    .when(accountService).activateAccount(any());

            mockMvc.perform(patch(url + id))
                    .andExpect(status().isInternalServerError())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void activateAccountShouldReturnExceptionResponse_whenUserNotFound() throws Exception {
            EntityNotFoundException exception = new EntityNotFoundException(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE);
            ExceptionResponse response = getExceptionResponse(
                    NOT_FOUND,
                    ENTITY_NOT_FOUND_EXCEPTION_MESSAGE,
                    exception
            );

            doThrow(exception)
                    .when(accountService).activateAccount(any());

            mockMvc.perform(patch(url + id))
                    .andExpect(status().isNotFound())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }
}
