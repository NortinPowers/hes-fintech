package by.powerssolutions.hesfintech.mapper;

import by.powerssolutions.hesfintech.domain.BaseDomain;
import by.powerssolutions.hesfintech.dto.BaseDto;
import by.powerssolutions.hesfintech.mapper.config.CustomMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CustomMapperConfig.class)
public interface BaseMapper {

    /**
     * Преобразует объект {@link BaseDomain} в объект передачи данных (DTO).
     *
     * @param baseDomain объект для преобразования.
     * @return Объект {@link BaseDto}.
     */
    BaseDto convertToDto(BaseDomain baseDomain);
}
