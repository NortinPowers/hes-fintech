package by.powerssolutions.hesfintech.repository;

import by.powerssolutions.hesfintech.domain.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Ищет счет пользователя по его имени.
     *
     * @param username Имя пользователя для поиска.
     * @return Объект {@link Optional<Account>}, содержащий счет пользователя или пустой, если счет не найден.
     */
    @Query("SELECT a FROM Account a JOIN a.user u WHERE u.username = :username")
    Optional<Account> findByUsername(String username);
}
