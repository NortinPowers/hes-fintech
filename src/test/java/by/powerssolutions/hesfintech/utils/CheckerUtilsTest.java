package by.powerssolutions.hesfintech.utils;

import static by.powerssolutions.hesfintech.utils.CheckerUtils.checkAmount;
import static by.powerssolutions.hesfintech.utils.CheckerUtils.checkList;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import by.powerssolutions.hesfintech.exception.CustomIncorrectInputException;
import by.powerssolutions.hesfintech.exception.CustomNoContentException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class CheckerUtilsTest {

    @Test
    public void checkListShouldThrowCustomNoContentException_whenPassedListIsEmpty() {
        List<String> list = new ArrayList<>();
        try {
            checkList(list, String.class);
            fail("Expected CustomNoContentException to be thrown");
        } catch (CustomNoContentException exception) {
            assertEquals("There are no " + String.class.getSimpleName().toLowerCase() + " objects", exception.getMessage());
        }
    }

    @Test
    void checkAmountShouldThrowCustomIncorrectInputException_whenCompareNegativeResult() {
        BigDecimal amount = new BigDecimal("-1");

        assertThrows(CustomIncorrectInputException.class, () -> {
            checkAmount(amount);
        });
    }
}
