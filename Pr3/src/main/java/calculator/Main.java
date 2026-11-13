package calculator;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, "UTF-8");
        System.out.println("Калькулятор. Операції: +  -  *  /  sqrt (квадратний корінь)");

        while (true) {
            System.out.println("\nВведіть перше число (або 'вихід' для завершення):");
            if (!scanner.hasNextLine()) {
                break;
            }
            String first = scanner.nextLine().trim();
            if (first.equalsIgnoreCase("вихід") || first.equalsIgnoreCase("exit")) {
                break;
            }

            System.out.println("Введіть операцію (+, -, *, /, sqrt):");
            String operation = scanner.hasNextLine() ? scanner.nextLine().trim() : "";

            String second = null;
            if (Operation.find(operation).map(Operation::isBinary).orElse(false)) {
                System.out.println("Введіть друге число:");
                second = scanner.hasNextLine() ? scanner.nextLine().trim() : "";
            }

            System.out.println(handleRequest(first, operation, second));
        }
        System.out.println("Дякуємо, що скористалися калькулятором!");
    }

    public static String handleRequest(String firstInput, String operationInput, String secondInput) {
        try {
            Operation operation = Operation.fromSymbol(operationInput);
            double a = InputParser.parseNumber(firstInput);
            double b = operation.isBinary() ? InputParser.parseNumber(secondInput) : 0;
            double result = Calculator.calculate(operation, a, b);
            return "Результат: " + format(result);
        } catch (ArithmeticException e) {
            return "Помилка обчислення: " + e.getMessage();
        } catch (InvalidInputException e) {
            return "Некоректні дані: " + e.getMessage();
        } finally {
            System.out.println("Обробку запиту завершено.");
        }
    }

    static String format(double value) {
        if (value == Math.rint(value) && Math.abs(value) < 1e15) {
            return String.valueOf((long) value);
        }
        String s = String.format(java.util.Locale.ROOT, "%.10f", value);
        return s.replaceAll("0+$", "").replaceAll("\\.$", "");
    }
}
