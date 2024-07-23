package by.powerssolutions.hesfintech.util;

import static by.powerssolutions.hesfintech.util.TestConstant.ACCOUNT_BALANCE;
import static by.powerssolutions.hesfintech.util.TestConstant.ACCOUNT_ID;
import static by.powerssolutions.hesfintech.util.TestConstant.USER_USERNAME;

import by.powerssolutions.hesfintech.domain.Account;
import by.powerssolutions.hesfintech.domain.User;
import by.powerssolutions.hesfintech.dto.response.AccountResponseDto;
import by.powerssolutions.hesfintech.dto.response.AccountShortResponseDto;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class AccountTestBuilder {

    @Builder.Default
    private Long id = ACCOUNT_ID;

    @Builder.Default
    private BigDecimal balance = ACCOUNT_BALANCE;

    @Builder.Default
    private Boolean status = true;

    @Builder.Default
    private User user = UserTestBuilder.builder().build().buildUser();

    @Builder.Default
    private String username = USER_USERNAME;

    public Account buildAccount() {
        Account account = new Account();
        account.setId(id);
        account.setBalance(balance);
        account.setStatus(status);
        account.setUser(user);
        return account;
    }

    public AccountResponseDto buildAccountResponseDto() {
        AccountResponseDto account = new AccountResponseDto();
        account.setBalance(balance);
        account.setStatus(status);
        account.setUsername(username);
        return account;
    }

    public AccountShortResponseDto buildAccountShortResponseDto() {
        AccountShortResponseDto account = new AccountShortResponseDto();
        account.setBalance(balance);
        account.setStatus(status);
        return account;
    }
}
