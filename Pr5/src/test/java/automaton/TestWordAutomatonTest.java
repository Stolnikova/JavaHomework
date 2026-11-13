package automaton;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

class TestWordAutomatonTest {

    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @CsvSource({
            "abcTESTabc, F",
            "abcTES,     3",
            "T,          1",
            "TE,         2",
            "TES,        3",
            "TEST,       F",
            "abcT,       1",
            "abcTE,      2",
            "xyz,        0",
            "TESTabc,    F",
            "abcTEST,    F",
            "TESTTEST,   F",
            "TESTT,      F",
            "TESTX,      F",
            "TEX,        0",
            "TESX,       0",
            "TXST,       1",
            "TESE,       0",
            "TESS,       0"
    })
    void recognizesFinalState(String input, String expected) {
        Assertions.assertEquals(expected, TestWordAutomaton.recognize(input).getLabel());
    }

    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @CsvSource({
            "test,  0",
            "Test,  0",
            "TeST,  1",
            "tEST,  1",
            "TESt,  0",
            "T E S T, 1",
            "T-E-S-T, 1"
    })
    void isCaseSensitiveAndRequiresConsecutiveLetters(String input, String expected) {
        Assertions.assertEquals(expected, TestWordAutomaton.recognize(input).getLabel());
    }

    @ParameterizedTest
    @EmptySource
    void emptyStringStaysInStartState(String input) {
        Assertions.assertEquals(State.S0, TestWordAutomaton.recognize(input));
    }

    @Test
    void nullInputIsRejected() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> TestWordAutomaton.recognize(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"  TEST  ", "123TEST456", "!@#TEST$%^", "тестTESTтест", "\tTEST\n", "TEST TEST"})
    void findsWordSurroundedByAnySymbols(String input) {
        Assertions.assertTrue(TestWordAutomaton.containsTest(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ТЕСТ", "ТEST", "TЕST", "TEЅT"})
    void lookalikeLettersFromOtherAlphabetsAreNotTest(String input) {
        Assertions.assertFalse(TestWordAutomaton.containsTest(input));
    }

    @ParameterizedTest
    @ValueSource(chars = {'a', 'T', 'E', 'S', ' ', '0', '\n'})
    void finalStateIsAbsorbing(char symbol) {
        Assertions.assertEquals(State.F, State.F.next(symbol));
    }

    @ParameterizedTest(name = "{0} --{1}--> {2}")
    @MethodSource("transitions")
    void singleTransition(State from, char symbol, State to) {
        Assertions.assertEquals(to, from.next(symbol));
    }

    static Stream<Arguments> transitions() {
        return Stream.of(
                Arguments.of(State.S0, 'T', State.S1),
                Arguments.of(State.S0, 'E', State.S0),
                Arguments.of(State.S0, 'x', State.S0),
                Arguments.of(State.S1, 'E', State.S2),
                Arguments.of(State.S1, 'x', State.S0),
                Arguments.of(State.S2, 'S', State.S3),
                Arguments.of(State.S2, 'x', State.S0),
                Arguments.of(State.S3, 'T', State.F),
                Arguments.of(State.S3, 'x', State.S0),
                Arguments.of(State.F, 'x', State.F),
                Arguments.of(State.F, 'T', State.F)
        );
    }

    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @MethodSource("traces")
    void traceShowsEveryState(String input, List<String> expected) {
        List<String> actual = new TestWordAutomaton().trace(input).stream().map(State::getLabel).toList();
        Assertions.assertEquals(expected, actual);
    }

    static Stream<Arguments> traces() {
        return Stream.of(
                Arguments.of("", List.of("0")),
                Arguments.of("aTEST", Arrays.asList("0", "0", "1", "2", "3", "F")),
                Arguments.of("TEx", Arrays.asList("0", "1", "2", "0")),
                Arguments.of("TESTa", Arrays.asList("0", "1", "2", "3", "F", "F"))
        );
    }

    @Test
    void automatonCanBeReused() {
        TestWordAutomaton automaton = new TestWordAutomaton();
        Assertions.assertEquals(State.F, automaton.run("TEST"));
        Assertions.assertEquals(State.S3, automaton.run("TES"));
        Assertions.assertEquals(State.S0, automaton.run(""));
    }

    @Test
    void stateIsUpdatedSymbolBySymbol() {
        TestWordAutomaton automaton = new TestWordAutomaton();
        automaton.accept('T');
        Assertions.assertEquals(State.S1, automaton.getState());
        automaton.accept('E');
        Assertions.assertEquals(State.S2, automaton.getState());
        automaton.accept('S');
        Assertions.assertEquals(State.S3, automaton.getState());
        automaton.accept('T');
        Assertions.assertEquals(State.F, automaton.getState());
    }
}
