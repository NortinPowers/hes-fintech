package by.powerssolutions.hesfintech.service;

import by.powerssolutions.hesfintech.dto.response.AccountResponseDto;
import by.powerssolutions.hesfintech.dto.response.AccountShortResponseDto;
import by.powerssolutions.hesfintech.model.BaseResponse;
import java.math.BigDecimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface AccountService {

    Page<AccountResponseDto> getAll(Pageable pageable);

    ResponseEntity<BaseResponse> blockAccount(Long id);

    ResponseEntity<BaseResponse> activateAccount(Long id);

    ResponseEntity<AccountShortResponseDto> getAccountByUsername(String username);

    ResponseEntity<BaseResponse> refillAccountByUsername(String username, BigDecimal amount);

    ResponseEntity<BaseResponse> withdrawAccountByUsername(String username, BigDecimal amount);
}
