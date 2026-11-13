package automaton;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TestWordAutomaton {
    private State state = State.S0;

    public void accept(char symbol) {
        state = state.next(symbol);
    }

    public State run(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Вхідний рядок не може бути null");
        }
        state = State.S0;
        for (char symbol : input.toCharArray()) {
            accept(symbol);
        }
        return state;
    }

    public List<State> trace(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Вхідний рядок не може бути null");
        }
        List<State> states = new ArrayList<>();
        state = State.S0;
        states.add(state);
        for (char symbol : input.toCharArray()) {
            accept(symbol);
            states.add(state);
        }
        return states;
    }

    public static State recognize(String input) {
        return new TestWordAutomaton().run(input);
    }

    public static boolean containsTest(String input) {
        return recognize(input) == State.F;
    }
}
