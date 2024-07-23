package by.powerssolutions.hesfintech.mapper;

import by.powerssolutions.hesfintech.domain.Account;
import by.powerssolutions.hesfintech.dto.response.AccountResponseDto;
import by.powerssolutions.hesfintech.dto.response.AccountShortResponseDto;
import by.powerssolutions.hesfintech.dto.response.UserDto;
import by.powerssolutions.hesfintech.mapper.config.CustomMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CustomMapperConfig.class)
public interface AccountMapper {

    /**
     * Преобразует данные о счете пользователя в объект доменной модели.
     *
     * @param dto Данные о счете пользователя.
     * @return Объект {@link Account}, представляющий счет пользователя.
     */
    Account toDomain(AccountResponseDto dto);

    /**
     * Преобразует объект счета пользователя в объект передачи данных (DTO).
     *
     * @param account Счет пользователя для преобразования.
     * @return Объект {@link UserDto}, содержащий данные о счете пользователе.
     */
    @Mapping(target = "username", source = "user.username")
    AccountResponseDto toDto(Account account);

    /**
     * Преобразует объект счета пользователя в объект передачи данных (DTO).
     *
     * @param account Счет пользователя для преобразования.
     * @return Объект {@link UserDto}, содержащий данные о счете пользователе.
     */
    AccountShortResponseDto toShortDto(Account account);
}
