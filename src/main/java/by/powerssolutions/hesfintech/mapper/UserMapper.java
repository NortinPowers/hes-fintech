package by.powerssolutions.hesfintech.mapper;

import by.powerssolutions.hesfintech.domain.User;
import by.powerssolutions.hesfintech.dto.request.UserRegistrationDto;
import by.powerssolutions.hesfintech.dto.response.UserDto;
import by.powerssolutions.hesfintech.mapper.config.CustomMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CustomMapperConfig.class)
public interface UserMapper {

    /**
     * Преобразует данные о регистрации пользователя в объект доменной модели.
     *
     * @param userRegistrationDto Данные о регистрации пользователя.
     * @return Объект {@link User}, представляющий пользователя.
     */
    User toDomain(UserRegistrationDto userRegistrationDto);

    /**
     * Преобразует объект пользователя в объект передачи данных (DTO).
     *
     * @param user Пользователь для преобразования.
     * @return Объект {@link UserDto}, содержащий данные о пользователе.
     */
    @Mapping(target = "role", source = "role.name")
    UserDto toDto(User user);
}
