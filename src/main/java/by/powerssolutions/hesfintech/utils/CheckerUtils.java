package by.powerssolutions.hesfintech.utils;

import by.powerssolutions.hesfintech.exception.CustomIncorrectInputException;
import by.powerssolutions.hesfintech.exception.CustomNoContentException;
import java.math.BigDecimal;
import java.util.Collection;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CheckerUtils {

    /**
     * Проверяет, что переданный список объектов не является пустым. В случае, если список пуст,
     * выбрасывается исключение {@link CustomNoContentException}, созданное на основе переданного класса.
     *
     * @param objects Список объектов для проверки.
     * @param clazz   Класс объектов, для которого выполняется проверка.
     * @throws CustomNoContentException Если список объектов пуст.
     */
    public static void checkList(Collection<?> objects, Class<?> clazz) {
        if (objects.isEmpty()) {
            throw CustomNoContentException.of(clazz);
        }
    }

    public static void checkAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw CustomIncorrectInputException.of(amount);
        }
    }
}
