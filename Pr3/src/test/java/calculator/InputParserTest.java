package calculator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InputParserTest {

    @Test
    public void testValidNumbers() throws InvalidInputException {
        Assertions.assertEquals(42.0, InputParser.parseNumber("42"));
        Assertions.assertEquals(-3.5, InputParser.parseNumber("-3.5"));
        Assertions.assertEquals(2.5, InputParser.parseNumber("2,5"));
        Assertions.assertEquals(7.0, InputParser.parseNumber("  7  "));
    }

    @Test
    public void testTextInsteadOfNumber() {
        InvalidInputException e = Assertions.assertThrows(InvalidInputException.class,
                () -> InputParser.parseNumber("abc"));
        Assertions.assertEquals("'abc' не є числом", e.getMessage());
        Assertions.assertInstanceOf(NumberFormatException.class, e.getCause());
    }

    @Test
    public void testCyrillicTextInsteadOfNumber() {
        Assertions.assertThrows(InvalidInputException.class, () -> InputParser.parseNumber("п'ять"));
    }

    @Test
    public void testNumberWithLetters() {
        Assertions.assertThrows(InvalidInputException.class, () -> InputParser.parseNumber("12abc"));
        Assertions.assertThrows(InvalidInputException.class, () -> InputParser.parseNumber("1.2.3"));
    }

    @Test
    public void testEmptyInput() {
        Assertions.assertThrows(InvalidInputException.class, () -> InputParser.parseNumber(""));
        Assertions.assertThrows(InvalidInputException.class, () -> InputParser.parseNumber("   "));
        Assertions.assertThrows(InvalidInputException.class, () -> InputParser.parseNumber(null));
    }

    @Test
    public void testNaNAndInfinityAreNotAccepted() {
        Assertions.assertThrows(InvalidInputException.class, () -> InputParser.parseNumber("NaN"));
        Assertions.assertThrows(InvalidInputException.class, () -> InputParser.parseNumber("Infinity"));
        Assertions.assertThrows(InvalidInputException.class, () -> InputParser.parseNumber("1e400"));
    }
}
