package calculator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CalculatorTest {


    @Test
    public void testAdd() throws InvalidInputException {
        Assertions.assertEquals(5.0, Calculator.add(2, 3));
        Assertions.assertEquals(-1.5, Calculator.add(-4, 2.5));
    }

    @Test
    public void testSubtract() throws InvalidInputException {
        Assertions.assertEquals(-1.0, Calculator.subtract(2, 3));
        Assertions.assertEquals(10.0, Calculator.subtract(7, -3));
    }

    @Test
    public void testMultiply() throws InvalidInputException {
        Assertions.assertEquals(6.0, Calculator.multiply(2, 3));
        Assertions.assertEquals(0.0, Calculator.multiply(123, 0));
    }

    @Test
    public void testDivide() throws InvalidInputException {
        Assertions.assertEquals(2.5, Calculator.divide(5, 2));
        Assertions.assertEquals(-3.0, Calculator.divide(9, -3));
        Assertions.assertEquals(0.0, Calculator.divide(0, 7));
    }

    @Test
    public void testSqrt() throws InvalidInputException {
        Assertions.assertEquals(3.0, Calculator.sqrt(9));
        Assertions.assertEquals(0.0, Calculator.sqrt(0));
    }

    @Test
    public void testCalculateChoosesOperation() throws InvalidInputException {
        Assertions.assertEquals(8.0, Calculator.calculate(Operation.ADD, 6, 2));
        Assertions.assertEquals(4.0, Calculator.calculate(Operation.SUBTRACT, 6, 2));
        Assertions.assertEquals(12.0, Calculator.calculate(Operation.MULTIPLY, 6, 2));
        Assertions.assertEquals(3.0, Calculator.calculate(Operation.DIVIDE, 6, 2));
        Assertions.assertEquals(4.0, Calculator.calculate(Operation.SQRT, 16, 999));
    }


    @Test
    public void testDivideByZeroThrowsArithmeticException() {
        ArithmeticException e = Assertions.assertThrows(ArithmeticException.class,
                () -> Calculator.divide(10, 0));
        Assertions.assertEquals("Ділення на нуль неможливе", e.getMessage());
    }

    @Test
    public void testDivideByNegativeZero() {
        Assertions.assertThrows(ArithmeticException.class, () -> Calculator.divide(10, -0.0));
    }

    @Test
    public void testZeroDividedByZero() {
        Assertions.assertThrows(ArithmeticException.class, () -> Calculator.divide(0, 0));
    }


    @Test
    public void testSqrtOfNegativeThrowsInvalidInput() {
        InvalidInputException e = Assertions.assertThrows(InvalidInputException.class,
                () -> Calculator.sqrt(-4));
        Assertions.assertTrue(e.getMessage().contains("від'ємного"));
    }

    @Test
    public void testNaNIsRejectedByEveryMethod() {
        double nan = Double.NaN;
        Assertions.assertThrows(InvalidInputException.class, () -> Calculator.add(nan, 1));
        Assertions.assertThrows(InvalidInputException.class, () -> Calculator.subtract(1, nan));
        Assertions.assertThrows(InvalidInputException.class, () -> Calculator.multiply(nan, 2));
        Assertions.assertThrows(InvalidInputException.class, () -> Calculator.divide(nan, 2));
        Assertions.assertThrows(InvalidInputException.class, () -> Calculator.sqrt(nan));
    }

    @Test
    public void testInfinityIsRejected() {
        Assertions.assertThrows(InvalidInputException.class,
                () -> Calculator.add(Double.POSITIVE_INFINITY, 1));
        Assertions.assertThrows(InvalidInputException.class,
                () -> Calculator.divide(1, Double.NEGATIVE_INFINITY));
    }


    @Test
    public void testOverflowThrowsArithmeticException() {
        Assertions.assertThrows(ArithmeticException.class,
                () -> Calculator.multiply(Double.MAX_VALUE, 2));
        Assertions.assertThrows(ArithmeticException.class,
                () -> Calculator.add(Double.MAX_VALUE, Double.MAX_VALUE));
    }

    @Test
    public void testDivisionCanOverflowToo() {
        Assertions.assertThrows(ArithmeticException.class,
                () -> Calculator.divide(Double.MAX_VALUE, 0.5));
    }
}
