package calculator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OperationTest {

    @Test
    public void testKnownSymbols() throws InvalidInputException {
        Assertions.assertEquals(Operation.ADD, Operation.fromSymbol("+"));
        Assertions.assertEquals(Operation.SUBTRACT, Operation.fromSymbol("-"));
        Assertions.assertEquals(Operation.MULTIPLY, Operation.fromSymbol("*"));
        Assertions.assertEquals(Operation.DIVIDE, Operation.fromSymbol("/"));
        Assertions.assertEquals(Operation.SQRT, Operation.fromSymbol(" SQRT "));
    }

    @Test
    public void testAlternativeSymbols() throws InvalidInputException {
        Assertions.assertEquals(Operation.MULTIPLY, Operation.fromSymbol("x"));
        Assertions.assertEquals(Operation.MULTIPLY, Operation.fromSymbol("х"));
        Assertions.assertEquals(Operation.DIVIDE, Operation.fromSymbol(":"));
        Assertions.assertEquals(Operation.SQRT, Operation.fromSymbol("корінь"));
    }

    @Test
    public void testUnknownOperationThrowsInvalidInput() {
        Assertions.assertThrows(InvalidInputException.class, () -> Operation.fromSymbol("%"));
        Assertions.assertThrows(InvalidInputException.class, () -> Operation.fromSymbol("плюс"));
        Assertions.assertThrows(InvalidInputException.class, () -> Operation.fromSymbol(""));
        Assertions.assertThrows(InvalidInputException.class, () -> Operation.fromSymbol(null));
    }

    @Test
    public void testBinaryFlag() {
        Assertions.assertTrue(Operation.DIVIDE.isBinary());
        Assertions.assertFalse(Operation.SQRT.isBinary());
    }
}
