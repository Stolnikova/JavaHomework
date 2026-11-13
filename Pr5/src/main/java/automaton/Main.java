package automaton;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        for (String example : new String[]{"abcTESTabc", "abcTES"}) {
            System.out.println("\"" + example + "\" -> " + TestWordAutomaton.recognize(example));
        }

        Scanner scanner = new Scanner(System.in, "UTF-8");
        TestWordAutomaton automaton = new TestWordAutomaton();
        while (true) {
            System.out.println("\nВведіть рядок (порожній рядок — вихід):");
            if (!scanner.hasNextLine()) {
                break;
            }
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                break;
            }
            List<State> states = automaton.trace(line);
            System.out.println("Стани: " + states.stream().map(State::getLabel).collect(Collectors.joining(" -> ")));
            System.out.println("Результат: " + automaton.getState()
                    + (automaton.getState() == State.F ? " (слово TEST знайдено)" : " (слово TEST не знайдено)"));
        }
    }
}
