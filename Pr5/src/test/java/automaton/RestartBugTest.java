package automaton;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class RestartBugTest {

    private static final char[] ALPHABET = {'T', 'E', 'S', 'x'};

    @ParameterizedTest(name = "\"{0}\" -> F")
    @ValueSource(strings = {
            "TTEST",
            "TETEST",
            "TTTTEST",
            "xTETEST",
            "TEsTTEST",
            "TETETEST",
            "TTETEST",
            "abcTTESTabc",
            "TEXTTEST",
            "TETTEST",
            "TESTEST"
    })
    void wordAfterBrokenPrefixIsFound(String input) {
        Assertions.assertEquals(State.F, TestWordAutomaton.recognize(input));
    }

    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @CsvSource({
            "TT,      1",
            "TET,     1",
            "TTE,     2",
            "TETE,    2",
            "TETES,   3",
            "TTES,    3",
            "TEST,    F",
            "TESE,    0",
            "TESS,    0"
    })
    void stateAfterRestartOnT(String input, String expected) {
        Assertions.assertEquals(expected, TestWordAutomaton.recognize(input).getLabel());
    }

    @ParameterizedTest(name = "{0} --{1}--> {2}")
    @MethodSource("restartTransitions")
    void letterTRestartsTheWord(State from, char symbol, State to) {
        Assertions.assertEquals(to, from.next(symbol));
    }

    static Stream<Arguments> restartTransitions() {
        return Stream.of(
                Arguments.of(State.S0, 'T', State.S1),
                Arguments.of(State.S1, 'T', State.S1),
                Arguments.of(State.S2, 'T', State.S1),
                Arguments.of(State.S3, 'T', State.F)
        );
    }

    @ParameterizedTest
    @EnumSource(State.class)
    void wordTestLeadsToFinalStateFromAnyState(State start) {
        State state = start;
        for (char symbol : "TEST".toCharArray()) {
            state = state.next(symbol);
        }
        Assertions.assertEquals(State.F, state);
    }

    @ParameterizedTest(name = "усі рядки довжини {0}")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8})
    void matchesStringContainsForEveryString(int length) {
        for (String input : allStrings(length)) {
            Assertions.assertEquals(input.contains("TEST"), TestWordAutomaton.containsTest(input), input);
            if (!input.contains("TEST")) {
                Assertions.assertEquals(expectedState(input), TestWordAutomaton.recognize(input), input);
            }
        }
    }

    private static State expectedState(String input) {
        for (int length = 3; length >= 1; length--) {
            if (input.endsWith("TEST".substring(0, length))) {
                return State.values()[length];
            }
        }
        return State.S0;
    }

    private static List<String> allStrings(int length) {
        List<String> result = new ArrayList<>();
        result.add("");
        for (int i = 0; i < length; i++) {
            List<String> next = new ArrayList<>();
            for (String prefix : result) {
                for (char symbol : ALPHABET) {
                    next.add(prefix + symbol);
                }
            }
            result = next;
        }
        return result;
    }
}
