package calculator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

class MainTest {

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private PrintStream originalOut;

    @BeforeEach
    public void captureOutput() {
        originalOut = System.out;
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    public void restoreOutput() {
        System.setOut(originalOut);
    }

    private String printed() {
        return output.toString(StandardCharsets.UTF_8);
    }

    @Test
    public void testSuccessfulRequest() {
        Assertions.assertEquals("Результат: 5", Main.handleRequest("2", "+", "3"));
        Assertions.assertEquals("Результат: 2.5", Main.handleRequest("5", "/", "2"));
        Assertions.assertEquals("Результат: 3", Main.handleRequest("9", "sqrt", null));
    }

    @Test
    public void testDivisionByZeroMessage() {
        Assertions.assertEquals("Помилка обчислення: Ділення на нуль неможливе",
                Main.handleRequest("10", "/", "0"));
    }

    @Test
    public void testTextInsteadOfNumberMessage() {
        Assertions.assertEquals("Некоректні дані: 'abc' не є числом",
                Main.handleRequest("abc", "+", "1"));
        Assertions.assertEquals("Некоректні дані: 'два' не є числом",
                Main.handleRequest("1", "*", "два"));
    }

    @Test
    public void testOwnExceptionMessages() {
        Assertions.assertTrue(Main.handleRequest("-4", "sqrt", null).startsWith("Некоректні дані: Квадратний корінь"));
        Assertions.assertTrue(Main.handleRequest("1", "%", "2").startsWith("Некоректні дані: Невідома операція"));
        Assertions.assertEquals("Некоректні дані: Число не введено", Main.handleRequest("", "+", "2"));
    }

    @Test
    public void testFinallyIsPrintedForSuccessAndErrors() {
        Main.handleRequest("2", "+", "2");
        Main.handleRequest("1", "/", "0");
        Main.handleRequest("x", "+", "1");

        String text = printed();
        int count = text.split("Обробку запиту завершено\\.", -1).length - 1;
        Assertions.assertEquals(3, count, "finally має спрацювати для кожного запиту");
    }

    @Test
    public void testFormat() {
        Assertions.assertEquals("5", Main.format(5.0));
        Assertions.assertEquals("-2.5", Main.format(-2.5));
        Assertions.assertEquals("0.3", Main.format(0.1 + 0.2));
    }
}
