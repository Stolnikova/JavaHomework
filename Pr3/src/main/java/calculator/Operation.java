package calculator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum Operation {
    ADD("+", "Додавання", true),
    SUBTRACT("-", "Віднімання", true),
    MULTIPLY("*", "Множення", true),
    DIVIDE("/", "Ділення", true),
    SQRT("sqrt", "Квадратний корінь", false);

    private final String symbol;
    private final String title;
    private final boolean binary;

    public static Optional<Operation> find(String input) {
        if (input == null) {
            return Optional.empty();
        }
        String s = input.trim().toLowerCase();
        for (Operation op : values()) {
            if (op.symbol.equals(s)) {
                return Optional.of(op);
            }
        }
        if (s.equals("x") || s.equals("х") || s.equals("×")) {
            return Optional.of(MULTIPLY);
        }
        if (s.equals(":") || s.equals("÷")) {
            return Optional.of(DIVIDE);
        }
        if (s.equals("√") || s.equals("корінь")) {
            return Optional.of(SQRT);
        }
        return Optional.empty();
    }

    public static Operation fromSymbol(String input) throws InvalidInputException {
        return find(input).orElseThrow(() ->
                new InvalidInputException("Невідома операція: '" + input + "'. Доступні: +, -, *, /, sqrt"));
    }
}
