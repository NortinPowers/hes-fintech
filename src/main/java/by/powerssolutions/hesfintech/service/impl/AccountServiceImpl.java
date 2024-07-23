package by.powerssolutions.hesfintech.service.impl;

import static by.powerssolutions.hesfintech.utils.CheckerUtils.checkAmount;
import static by.powerssolutions.hesfintech.utils.CheckerUtils.checkList;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.ACTIVATE_ACCOUNT_MESSAGE;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.BLOCK_ACCOUNT_MESSAGE;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.SUCCESS_ACCOUNT_CREATION_MESSAGE;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.SUCCESS_ACCOUNT_REFILL_MESSAGE;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.SUCCESS_ACCOUNT_WITHDRAW_MESSAGE;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.WITHDRAWAL_ERROR_MESSAGE;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.getSuccessResponse;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.setBalanceDisplay;

import by.powerssolutions.hesfintech.domain.Account;
import by.powerssolutions.hesfintech.domain.User;
import by.powerssolutions.hesfintech.dto.response.AccountResponseDto;
import by.powerssolutions.hesfintech.dto.response.AccountShortResponseDto;
import by.powerssolutions.hesfintech.exception.CustomAccountExistException;
import by.powerssolutions.hesfintech.exception.CustomEntityNotFoundException;
import by.powerssolutions.hesfintech.mapper.AccountMapper;
import by.powerssolutions.hesfintech.model.BaseResponse;
import by.powerssolutions.hesfintech.repository.AccountRepository;
import by.powerssolutions.hesfintech.service.AccountService;
import by.powerssolutions.hesfintech.service.UserService;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;
    private final AccountMapper mapper;
    private final UserService userService;

    @Override
    public Page<AccountResponseDto> getAll(Pageable pageable) {
        Page<AccountResponseDto> newsPage = repository.findAll(pageable)
                .map(mapper::toDto);
        checkList(newsPage.getContent(), Account.class);
        return newsPage;
    }

    @Override
    @Transactional
    public ResponseEntity<BaseResponse> blockAccount(Long id) {
        Optional<Account> accountOptional = repository.findById(id);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            account.setStatus(false);
            return ResponseEntity.ok(getSuccessResponse(BLOCK_ACCOUNT_MESSAGE, String.valueOf(account.getId())));
        } else {
            throw CustomEntityNotFoundException.of(Account.class, id);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<BaseResponse> activateAccount(Long id) {
        Optional<Account> accountOptional = repository.findById(id);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            account.setStatus(true);
            return ResponseEntity.ok(getSuccessResponse(ACTIVATE_ACCOUNT_MESSAGE, String.valueOf(account.getId())));
        } else {
            throw CustomEntityNotFoundException.of(Account.class, id);
        }
    }

    @Override
    public ResponseEntity<AccountShortResponseDto> getAccountByUsername(String username) {
        Optional<Account> accountOptional = repository.findByUsername(username);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            return ResponseEntity.ok(mapper.toShortDto(account));
        } else {
            throw CustomEntityNotFoundException.of(Account.class, username);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<BaseResponse> refillAccountByUsername(String username, BigDecimal amount) {
        checkAmount(amount);
        Optional<Account> accountOptional = repository.findByUsername(username);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            BigDecimal currentBalance = account.getBalance();
            BigDecimal updatedBalance = currentBalance.add(amount);
            account.setBalance(updatedBalance);
            return ResponseEntity.ok(getSuccessResponse(SUCCESS_ACCOUNT_REFILL_MESSAGE, setBalanceDisplay(updatedBalance)));
        } else {
            throw CustomEntityNotFoundException.of(Account.class, username);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<BaseResponse> withdrawAccountByUsername(String username, BigDecimal amount) {
        checkAmount(amount);
        Optional<Account> accountOptional = repository.findByUsername(username);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            BigDecimal currentBalance = account.getBalance();
            BigDecimal updatedBalance = currentBalance.subtract(amount);
            if (updatedBalance.compareTo(BigDecimal.ZERO) >= 0) {
                account.setBalance(updatedBalance);
                return ResponseEntity.ok(getSuccessResponse(SUCCESS_ACCOUNT_WITHDRAW_MESSAGE, setBalanceDisplay(updatedBalance)));
            } else {
                return ResponseEntity.ok(getSuccessResponse(WITHDRAWAL_ERROR_MESSAGE, setBalanceDisplay(currentBalance)));
            }
        } else {
            throw CustomEntityNotFoundException.of(Account.class, username);
        }
    }

    @Override
    public ResponseEntity<BaseResponse> createUserAccount(String username) {
        Optional<Account> accountOptional = repository.findByUsername(username);
        if (accountOptional.isEmpty()) {
            Account account = new Account();
            User user = userService.getUserByUsername(username);
            account.setUser(user);
            repository.save(account);
            return ResponseEntity.ok(getSuccessResponse(SUCCESS_ACCOUNT_CREATION_MESSAGE, username));
        } else {
            throw CustomAccountExistException.of(username);
        }
    }
}
