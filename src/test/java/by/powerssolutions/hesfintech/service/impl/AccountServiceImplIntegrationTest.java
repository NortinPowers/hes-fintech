package by.powerssolutions.hesfintech.service.impl;

import static by.powerssolutions.hesfintech.util.TestConstant.INCORRECT_ID;
import static by.powerssolutions.hesfintech.util.TestConstant.PAGE_NUMBER;
import static by.powerssolutions.hesfintech.util.TestConstant.PAGE_SIZE;
import static by.powerssolutions.hesfintech.util.TestConstant.USER_USERNAME;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.ACTIVATE_ACCOUNT_MESSAGE;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.BLOCK_ACCOUNT_MESSAGE;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.SUCCESS_ACCOUNT_CREATION_MESSAGE;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.SUCCESS_ACCOUNT_REFILL_MESSAGE;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.SUCCESS_ACCOUNT_WITHDRAW_MESSAGE;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.WITHDRAWAL_ERROR_MESSAGE;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.getSuccessResponse;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.setBalanceDisplay;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.powerssolutions.hesfintech.domain.Account;
import by.powerssolutions.hesfintech.domain.User;
import by.powerssolutions.hesfintech.dto.response.AccountResponseDto;
import by.powerssolutions.hesfintech.dto.response.AccountShortResponseDto;
import by.powerssolutions.hesfintech.exception.CustomAccountExistException;
import by.powerssolutions.hesfintech.exception.CustomEntityNotFoundException;
import by.powerssolutions.hesfintech.exception.CustomIncorrectInputException;
import by.powerssolutions.hesfintech.mapper.AccountMapper;
import by.powerssolutions.hesfintech.model.BaseResponse;
import by.powerssolutions.hesfintech.model.SuccessResponse;
import by.powerssolutions.hesfintech.repository.AccountRepository;
import by.powerssolutions.hesfintech.service.AccountService;
import by.powerssolutions.hesfintech.service.UserService;
import by.powerssolutions.hesfintech.util.AccountTestBuilder;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor
class AccountServiceImplIntegrationTest {

    private final AccountService accountService;

    @MockBean
    private AccountRepository repository;

    @MockBean
    private AccountMapper mapper;

    @MockBean
    private UserService userService;

    @Nested
    class getAllTest {

        @Test
        void getAllShouldReturnPageWithAccountResponseDtosList_whenAccountResponseDtosListIsNotEmpty() {
            Account account = AccountTestBuilder.builder()
                    .build()
                    .buildAccount();
            List<Account> accounts = List.of(account);
            AccountResponseDto responseDto = AccountTestBuilder.builder()
                    .build()
                    .buildAccountResponseDto();
            List<AccountResponseDto> expected = List.of(responseDto);
            PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
            PageImpl<Account> page = new PageImpl<>(accounts);

            when(repository.findAll(pageRequest))
                    .thenReturn(page);
            when(mapper.toDto(account))
                    .thenReturn(responseDto);

            Page<AccountResponseDto> actual = accountService.getAll(pageRequest);

            assertThat(actual.getContent())
                    .hasSize(expected.size())
                    .isEqualTo(expected);
        }

        @Test
        void getAllShouldThrowCustomNoContentException_whenAccountResponseDtosListIsEmpty() {
            Account account = AccountTestBuilder.builder()
                    .build()
                    .buildAccount();
            List<Account> accounts = List.of(account);
            AccountResponseDto responseDto = AccountTestBuilder.builder()
                    .build()
                    .buildAccountResponseDto();
            List<AccountResponseDto> expected = List.of(responseDto);
            PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
            PageImpl<Account> page = new PageImpl<>(accounts);

            when(repository.findAll(pageRequest))
                    .thenReturn(page);
            when(mapper.toDto(account))
                    .thenReturn(responseDto);

            Page<AccountResponseDto> actual = accountService.getAll(pageRequest);

            assertThat(actual.getContent())
                    .hasSize(expected.size())
                    .isEqualTo(expected);
        }
    }

    @Nested
    class blockAccountTest {

        @Test
        void blockAccountShouldReturnSuccessMessage_whenUserAccountBlocked() {
            Account account = AccountTestBuilder.builder()
                    .build()
                    .buildAccount();
            Long id = account.getId();
            ResponseEntity<BaseResponse> expected = ResponseEntity.ok(getSuccessResponse(BLOCK_ACCOUNT_MESSAGE, String.valueOf(account.getId())));

            when(repository.findById(id))
                    .thenReturn(Optional.of(account));

            ResponseEntity<BaseResponse> actual = accountService.blockAccount(id);

            Assert.assertEquals(((SuccessResponse) expected.getBody()).getMessage(), ((SuccessResponse) actual.getBody()).getMessage());
            Assert.assertEquals(expected.getBody().getStatus(), actual.getBody().getStatus());
        }

        @Test
        void blockAccountShouldThrowCustomEntityNotFoundException_whenIncorrectId() {
            CustomEntityNotFoundException expectedException = CustomEntityNotFoundException.of(Account.class, INCORRECT_ID);

            when(repository.findById(INCORRECT_ID))
                    .thenReturn(Optional.empty());

            CustomEntityNotFoundException actualException = assertThrows(CustomEntityNotFoundException.class, () -> accountService.blockAccount(INCORRECT_ID));

            verify(mapper, never()).toDto(any(Account.class));
            assertEquals(expectedException.getMessage(), actualException.getMessage());
        }
    }

    @Nested
    class activateAccountTest {

        @Test
        void activateAccountShouldReturnSuccessMessage_whenUserAccountActivated() {
            Account account = AccountTestBuilder.builder()
                    .build()
                    .buildAccount();
            Long id = account.getId();
            ResponseEntity<BaseResponse> expected = ResponseEntity.ok(getSuccessResponse(ACTIVATE_ACCOUNT_MESSAGE, String.valueOf(account.getId())));

            when(repository.findById(id))
                    .thenReturn(Optional.of(account));

            ResponseEntity<BaseResponse> actual = accountService.activateAccount(id);

            Assert.assertEquals(((SuccessResponse) expected.getBody()).getMessage(), ((SuccessResponse) actual.getBody()).getMessage());
            Assert.assertEquals(expected.getBody().getStatus(), actual.getBody().getStatus());
        }

        @Test
        void activateAccountShouldThrowCustomEntityNotFoundException_whenIncorrectId() {
            CustomEntityNotFoundException expectedException = CustomEntityNotFoundException.of(Account.class, INCORRECT_ID);

            when(repository.findById(INCORRECT_ID))
                    .thenReturn(Optional.empty());

            CustomEntityNotFoundException actualException = assertThrows(CustomEntityNotFoundException.class, () -> accountService.activateAccount(INCORRECT_ID));

            verify(mapper, never()).toDto(any(Account.class));
            assertEquals(expectedException.getMessage(), actualException.getMessage());
        }
    }

    @Nested
    class getAccountByUsernameTest {

        @Test
        void getAccountByUsernameShouldReturnEntityOfAccountShortResponseDto_whenCalled() {
            Account account = AccountTestBuilder.builder()
                    .build()
                    .buildAccount();
            AccountShortResponseDto expected = AccountTestBuilder.builder()
                    .build()
                    .buildAccountShortResponseDto();

            when(repository.findByUsername(USER_USERNAME))
                    .thenReturn(Optional.of(account));
            when(mapper.toShortDto(account))
                    .thenReturn(expected);

            ResponseEntity<AccountShortResponseDto> actual = accountService.getAccountByUsername(USER_USERNAME);

            assertThat(actual.getBody())
                    .hasFieldOrPropertyWithValue(AccountShortResponseDto.Fields.balance, expected.getBalance())
                    .hasFieldOrPropertyWithValue(AccountShortResponseDto.Fields.status, expected.getStatus());
        }

        @Test
        void getAccountByUsernameShouldThrowCustomEntityNotFoundException_whenIncorrectUsername() {
            CustomEntityNotFoundException expectedException = CustomEntityNotFoundException.of(Account.class, USER_USERNAME);

            when(repository.findByUsername(USER_USERNAME))
                    .thenReturn(Optional.empty());

            CustomEntityNotFoundException actualException = assertThrows(CustomEntityNotFoundException.class, () -> accountService.getAccountByUsername(USER_USERNAME));

            verify(mapper, never()).toDto(any(Account.class));
            assertEquals(expectedException.getMessage(), actualException.getMessage());
        }
    }

    @Nested
    class refillAccountByUsernameTest {

        private BigDecimal amount;

        {
            amount = BigDecimal.TEN;
        }

        @Test
        void refillAccountByUsernameShouldReturnSuccessMessage_whenCalled() {
            Account account = AccountTestBuilder.builder()
                    .build()
                    .buildAccount();
            BigDecimal updatedBalance = account.getBalance().add(amount);
            ResponseEntity<BaseResponse> expected = ResponseEntity.ok(getSuccessResponse(SUCCESS_ACCOUNT_REFILL_MESSAGE, setBalanceDisplay(updatedBalance)));

            when(repository.findByUsername(USER_USERNAME))
                    .thenReturn(Optional.of(account));

            ResponseEntity<BaseResponse> actual = accountService.refillAccountByUsername(USER_USERNAME, amount);

            Assert.assertEquals(((SuccessResponse) expected.getBody()).getMessage(), ((SuccessResponse) actual.getBody()).getMessage());
            Assert.assertEquals(expected.getBody().getStatus(), actual.getBody().getStatus());
        }

        @Test
        void refillAccountByUsernameShouldThrowCustomEntityNotFoundException_whenIncorrectUsername() {
            CustomEntityNotFoundException expectedException = CustomEntityNotFoundException.of(Account.class, USER_USERNAME);

            when(repository.findByUsername(USER_USERNAME))
                    .thenReturn(Optional.empty());

            CustomEntityNotFoundException actualException = assertThrows(CustomEntityNotFoundException.class, () -> accountService.refillAccountByUsername(USER_USERNAME, amount));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
        }

        @Test
        void refillAccountByUsernameShouldThrowCustomIncorrectInputException_whenIncorrectAmount() {
            amount = BigDecimal.valueOf(-5);
            CustomIncorrectInputException expectedException = CustomIncorrectInputException.of(amount);

            CustomIncorrectInputException actualException = assertThrows(CustomIncorrectInputException.class, () -> accountService.refillAccountByUsername(USER_USERNAME, amount));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(repository, never()).findByUsername(any());
        }
    }

    @Nested
    class withdrawAccountByUsernameTest {

        private BigDecimal amount;

        {
            amount = BigDecimal.valueOf(5);
        }

        @Test
        void withdrawAccountByUsernameShouldReturnSuccessMessage_whenCalled() {
            Account account = AccountTestBuilder.builder()
                    .build()
                    .buildAccount();
            BigDecimal updatedBalance = account.getBalance().subtract(amount);
            ResponseEntity<BaseResponse> expected = ResponseEntity.ok(getSuccessResponse(SUCCESS_ACCOUNT_WITHDRAW_MESSAGE, setBalanceDisplay(updatedBalance)));

            when(repository.findByUsername(USER_USERNAME))
                    .thenReturn(Optional.of(account));

            ResponseEntity<BaseResponse> actual = accountService.withdrawAccountByUsername(USER_USERNAME, amount);

            Assert.assertEquals(((SuccessResponse) expected.getBody()).getMessage(), ((SuccessResponse) actual.getBody()).getMessage());
            Assert.assertEquals(expected.getBody().getStatus(), actual.getBody().getStatus());
        }

        @Test
        void withdrawAccountByUsernameShouldReturnErrorMessage_whenLowBalance() {
            Account account = AccountTestBuilder.builder()
                    .build()
                    .buildAccount();
            amount = BigDecimal.valueOf(100);
            ResponseEntity<BaseResponse> expected = ResponseEntity.ok(getSuccessResponse(WITHDRAWAL_ERROR_MESSAGE, setBalanceDisplay(account.getBalance())));

            when(repository.findByUsername(USER_USERNAME))
                    .thenReturn(Optional.of(account));

            ResponseEntity<BaseResponse> actual = accountService.withdrawAccountByUsername(USER_USERNAME, amount);

            Assert.assertEquals(((SuccessResponse) expected.getBody()).getMessage(), ((SuccessResponse) actual.getBody()).getMessage());
            Assert.assertEquals(expected.getBody().getStatus(), actual.getBody().getStatus());
        }

        @Test
        void withdrawAccountByUsernameShouldThrowCustomEntityNotFoundException_whenIncorrectUsername() {
            CustomEntityNotFoundException expectedException = CustomEntityNotFoundException.of(Account.class, USER_USERNAME);

            when(repository.findByUsername(USER_USERNAME))
                    .thenReturn(Optional.empty());

            CustomEntityNotFoundException actualException = assertThrows(CustomEntityNotFoundException.class, () -> accountService.withdrawAccountByUsername(USER_USERNAME, amount));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
        }

        @Test
        void withdrawAccountByUsernameShouldThrowCustomIncorrectInputException_whenIncorrectAmount() {
            amount = BigDecimal.valueOf(-5);
            CustomIncorrectInputException expectedException = CustomIncorrectInputException.of(amount);

            CustomIncorrectInputException actualException = assertThrows(CustomIncorrectInputException.class, () -> accountService.withdrawAccountByUsername(USER_USERNAME, amount));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(repository, never()).findByUsername(any());
        }
    }

    @Nested
    class createUserAccountTest {

        @Test
        void createUserAccountShouldReturnSuccessMessage_whenAccountCreated() {
            Account account = AccountTestBuilder.builder()
                    .withId(null)
                    .build()
                    .buildAccount();
            User user = account.getUser();
            String username = user.getUsername();
            ResponseEntity<SuccessResponse> expected = ResponseEntity.ok(getSuccessResponse(SUCCESS_ACCOUNT_CREATION_MESSAGE, username));

            when(repository.findByUsername(username))
                    .thenReturn(Optional.empty());
            when(userService.getUserByUsername(username))
                    .thenReturn(user);

            ResponseEntity<BaseResponse> actual = accountService.createUserAccount(username);

            Assert.assertEquals(((SuccessResponse) expected.getBody()).getMessage(), ((SuccessResponse) actual.getBody()).getMessage());
            Assert.assertEquals(expected.getBody().getStatus(), actual.getBody().getStatus());
            verify(repository).save(any(Account.class));
        }

        @Test
        void createUserAccountShouldThrowCustomAccountExistException_whenAccountAlreadyExist() {
            Account account = AccountTestBuilder.builder()
                    .withId(null)
                    .build()
                    .buildAccount();
            CustomAccountExistException expectedException = CustomAccountExistException.of(USER_USERNAME);

            when(repository.findByUsername(USER_USERNAME))
                    .thenReturn(Optional.of(account));

            CustomAccountExistException actualException = assertThrows(CustomAccountExistException.class, () -> accountService.createUserAccount(USER_USERNAME));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
        }
    }
}
