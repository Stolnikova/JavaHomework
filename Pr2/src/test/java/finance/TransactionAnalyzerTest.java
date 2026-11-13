package finance;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class TransactionAnalyzerTest {

    @Test
    public void testCalculateTotalBalance() {
        Transaction transaction1 = new Transaction("01-01-2023", 100.0, "Дохід");
        Transaction transaction2 = new Transaction("02-01-2023", -50.0, "Витрата");
        Transaction transaction3 = new Transaction("03-01-2023", 150.0, "Дохід");
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3);

        double result = TransactionAnalyzer.calculateTotalBalance(transactions);

        Assertions.assertEquals(200.0, result, "Розрахунок загального балансу неправильний");
    }

    @Test
    public void testCountTransactionsByMonth() {
        Transaction transaction1 = new Transaction("01-02-2023", 50.0, "Дохід");
        Transaction transaction2 = new Transaction("15-02-2023", -20.0, "Витрата");
        Transaction transaction3 = new Transaction("05-03-2023", 100.0, "Дохід");
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3);

        int countFeb = TransactionAnalyzer.countTransactionsByMonth(transactions, "02-2023");
        int countMar = TransactionAnalyzer.countTransactionsByMonth(transactions, "03-2023");

        Assertions.assertEquals(2, countFeb, "Кількість транзакцій за лютий неправильна");
        Assertions.assertEquals(1, countMar, "Кількість транзакцій за березень неправильна");
    }

    @Test
    public void testFindTopExpenses() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("01-01-2024", 50000, "Зарплата"),
                new Transaction("02-01-2024", -100, "A"),
                new Transaction("03-01-2024", -1200, "B"),
                new Transaction("04-01-2024", -300, "C"),
                new Transaction("05-01-2024", -9000, "D"),
                new Transaction("06-01-2024", -50, "E"),
                new Transaction("07-01-2024", -700, "F"),
                new Transaction("08-01-2024", -4000, "G"),
                new Transaction("09-01-2024", -20, "H"),
                new Transaction("10-01-2024", -650, "I"),
                new Transaction("11-01-2024", -2500, "J"),
                new Transaction("12-01-2024", -10, "K"),
                new Transaction("13-01-2024", -80, "L"));

        List<Transaction> top = TransactionAnalyzer.findTopExpenses(transactions);

        Assertions.assertEquals(10, top.size(), "Має бути рівно 10 витрат");
        Assertions.assertEquals("D", top.get(0).getDescription(), "Першою має бути найбільша витрата");
        Assertions.assertEquals("E", top.get(9).getDescription(), "Останньою має бути 10-та за розміром витрата");
        for (int i = 1; i < top.size(); i++) {
            Assertions.assertTrue(top.get(i - 1).getAmount() <= top.get(i).getAmount(),
                    "Витрати мають бути відсортовані від найбільшої");
        }
        Assertions.assertTrue(top.stream().allMatch(t -> t.getAmount() < 0), "Доходи не мають потрапляти у витрати");
        Assertions.assertTrue(top.stream().noneMatch(t -> t.getDescription().equals("H")
                || t.getDescription().equals("K")), "Дрібніші витрати мають бути відсічені");
    }

    @Test
    public void testFindTopExpensesWhenLessThanTen() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("01-01-2024", -100, "A"),
                new Transaction("02-01-2024", 500, "Дохід"),
                new Transaction("03-01-2024", -300, "B"));

        List<Transaction> top = TransactionAnalyzer.findTopExpenses(transactions);

        Assertions.assertEquals(2, top.size());
        Assertions.assertEquals("B", top.get(0).getDescription());
    }

    @Test
    public void testLargestAndSmallestExpenseInPeriod() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("30-11-2023", -99999, "Поза періодом"),
                new Transaction("01-12-2023", -500, "Межа початку"),
                new Transaction("15-12-2023", -7000, "Велика"),
                new Transaction("20-12-2023", 80000, "Дохід"),
                new Transaction("31-12-2023", -200, "Межа кінця"),
                new Transaction("01-01-2024", -1, "Поза періодом"));
        LocalDate from = LocalDate.of(2023, 12, 1);
        LocalDate to = LocalDate.of(2023, 12, 31);

        Optional<Transaction> largest = TransactionAnalyzer.findLargestExpense(transactions, from, to);
        Optional<Transaction> smallest = TransactionAnalyzer.findSmallestExpense(transactions, from, to);

        Assertions.assertEquals("Велика", largest.get().getDescription());
        Assertions.assertEquals("Межа кінця", smallest.get().getDescription());
    }

    @Test
    public void testNoExpensesInPeriod() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("10-12-2023", 80000, "Зарплата"));
        LocalDate from = LocalDate.of(2023, 12, 1);
        LocalDate to = LocalDate.of(2023, 12, 31);

        Assertions.assertFalse(TransactionAnalyzer.findLargestExpense(transactions, from, to).isPresent());
        Assertions.assertFalse(TransactionAnalyzer.findSmallestExpense(transactions, from, to).isPresent());
    }

    @Test
    public void testSumExpensesByCategoryAndMonth() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("05-12-2023", -1000, "Сільпо"),
                new Transaction("10-12-2023", 80000, "Зарплата"),
                new Transaction("20-12-2023", -500, "Аптека"),
                new Transaction("05-01-2024", -2500, "Сільпо"));

        Map<String, Double> byCategory = TransactionAnalyzer.sumExpensesByCategory(transactions);
        Map<String, Double> byMonth = TransactionAnalyzer.sumExpensesByMonth(transactions);

        Assertions.assertEquals(3500.0, byCategory.get("Сільпо"));
        Assertions.assertEquals(500.0, byCategory.get("Аптека"));
        Assertions.assertFalse(byCategory.containsKey("Зарплата"), "Доходи не є витратами");
        Assertions.assertEquals("Сільпо", byCategory.keySet().iterator().next(), "Спочатку найбільша категорія");

        Assertions.assertEquals(Arrays.asList("12-2023", "01-2024"), Arrays.asList(byMonth.keySet().toArray()),
                "Місяці мають іти в хронологічному порядку");
        Assertions.assertEquals(1500.0, byMonth.get("12-2023"));
        Assertions.assertEquals(2500.0, byMonth.get("01-2024"));
    }
}
