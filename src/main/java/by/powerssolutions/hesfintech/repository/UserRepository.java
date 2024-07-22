package by.powerssolutions.hesfintech.repository;

import by.powerssolutions.hesfintech.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Ищет пользователя по его имени пользователя.
     *
     * @param username Имя пользователя для поиска.
     * @return Объект {@link Optional<User>}, содержащий пользователя с указанным именем или пустой, если пользователь не найден.
     */
    Optional<User> findByUsername(String username);
}
