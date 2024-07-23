package by.powerssolutions.hesfintech.service;

import by.powerssolutions.hesfintech.dto.response.AccountResponseDto;
import by.powerssolutions.hesfintech.dto.response.AccountShortResponseDto;
import by.powerssolutions.hesfintech.model.BaseResponse;
import java.math.BigDecimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface AccountService {

    /**
     * Возвращает страницу содержащую список счетов пользователей в зависимости от переданного параметра.
     *
     * @param pageable Объект определяющий номер страницы и размер списка счетов.
     * @return {@link ResponseEntity} с Page объектов {@link AccountResponseDto}.
     */
    Page<AccountResponseDto> getAll(Pageable pageable);

    /**
     * Блокирует счет пользователя по идентификатору.
     *
     * @param id Идентификатор счета пользователя.
     * @return Ответ с сообщением об успешной блокировки счета пользователя.
     */
    ResponseEntity<BaseResponse> blockAccount(Long id);

    /**
     * Активирует счет пользователя по идентификатору.
     *
     * @param id Идентификатор счета пользователя.
     * @return Ответ с сообщением об успешной активации счета пользователя.
     */
    ResponseEntity<BaseResponse> activateAccount(Long id);

    /**
     * Возвращает информацию о счета пользователя.
     *
     * @return Ответ содержащий информацию о счете пользователя.
     */
    ResponseEntity<AccountShortResponseDto> getAccountByUsername(String username);

    /**
     * Пополняет счет пользователя на указанную суммы.
     *
     * @param username Имя пользователя.
     * @param amount   Сумма пополнения счета.
     * @return Ответ с сообщением об успешном пополнении счета и текущий баланс.
     */
    ResponseEntity<BaseResponse> refillAccountByUsername(String username, BigDecimal amount);

    /**
     * Снимает средства со счета пользователя на указанную суммы.
     *
     * @param username Имя пользователя.
     * @param amount   Сумма снятия счета.
     * @return Ответ с сообщением об успешном снятии со счета либо недостатке средств для снятия указанной суммы и текущий баланс.
     */
    ResponseEntity<BaseResponse> withdrawAccountByUsername(String username, BigDecimal amount);

    /**
     * Создает для пользователя новый счет, если у него еще нет открытого счета.
     *
     * @param username Имя пользователя.
     * @return Ответ с сообщением об успешном открытии счета.
     */
    ResponseEntity<BaseResponse> createUserAccount(String username);
}
