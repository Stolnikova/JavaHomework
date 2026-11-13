package calculator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class Calculator {

    public static double add(double a, double b) throws InvalidInputException {
        validate(a, b);
        return checkResult(a + b);
    }

    public static double subtract(double a, double b) throws InvalidInputException {
        validate(a, b);
        return checkResult(a - b);
    }

    public static double multiply(double a, double b) throws InvalidInputException {
        validate(a, b);
        return checkResult(a * b);
    }

    public static double divide(double a, double b) throws InvalidInputException {
        validate(a, b);
        if (b == 0) {
            throw new ArithmeticException("Ділення на нуль неможливе");
        }
        return checkResult(a / b);
    }

    public static double sqrt(double a) throws InvalidInputException {
        validate(a);
        if (a < 0) {
            throw new InvalidInputException("Квадратний корінь з від'ємного числа (" + a + ") не існує");
        }
        return Math.sqrt(a);
    }

    public static double calculate(Operation operation, double a, double b) throws InvalidInputException {
        switch (operation) {
            case ADD:
                return add(a, b);
            case SUBTRACT:
                return subtract(a, b);
            case MULTIPLY:
                return multiply(a, b);
            case DIVIDE:
                return divide(a, b);
            case SQRT:
                return sqrt(a);
            default:
                throw new InvalidInputException("Операція не підтримується: " + operation);
        }
    }

    private static void validate(double... values) throws InvalidInputException {
        for (double value : values) {
            if (Double.isNaN(value) || Double.isInfinite(value)) {
                throw new InvalidInputException("Некоректне число: " + value);
            }
        }
    }

    private static double checkResult(double result) {
        if (Double.isInfinite(result)) {
            throw new ArithmeticException("Переповнення: результат завеликий");
        }
        return result;
    }
}
