package calculator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class InputParser {

    public static double parseNumber(String input) throws InvalidInputException {
        if (input == null || input.trim().isEmpty()) {
            throw new InvalidInputException("Число не введено");
        }
        String text = input.trim().replace(',', '.');
        double value;
        try {
            value = Double.parseDouble(text);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("'" + input.trim() + "' не є числом", e);
        }
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new InvalidInputException("'" + input.trim() + "' не є скінченним числом");
        }
        return value;
    }
}
