package automaton;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum State {
    S0("0"),
    S1("1"),
    S2("2"),
    S3("3"),
    F("F");

    private final String label;

    public State next(char symbol) {
        switch (this) {
            case S0:
                return restart(symbol);
            case S1:
                return symbol == 'E' ? S2 : restart(symbol);
            case S2:
                return symbol == 'S' ? S3 : restart(symbol);
            case S3:
                return symbol == 'T' ? F : S0;
            default:
                return F;
        }
    }

    private static State restart(char symbol) {
        return symbol == 'T' ? S1 : S0;
    }

    @Override
    public String toString() {
        return label;
    }
}
