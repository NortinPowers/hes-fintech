package by.powerssolutions.hesfintech.utils;

import by.powerssolutions.hesfintech.exception.CustomNoContentException;
import java.util.Collection;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CheckerUtil {

    /**
     * Проверяет, что переданный объект не является {@code null}. В случае, если объект равен {@code null},
     * выбрасывается исключение {@link IllegalArgumentException} с указанным сообщением.
     *
     * @param t       Объект для проверки.
     * @param message Сообщение, которое будет использовано в исключении, если объект равен {@code null}.
     * @param <T>     Тип объекта.
     * @throws IllegalArgumentException Если объект {@code null}.
     */
    public static <T> void checkIllegalArgument(T t, String message) {
        if (t == null) {
            throw new IllegalArgumentException(message);
        }
    }

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
}
