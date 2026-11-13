package finance;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class TransactionReportGenerator {

    public static void printBalanceReport(double totalBalance) {
        System.out.println("Загальний баланс: " + totalBalance);
    }

    public static void printTransactionsCountByMonth(String monthYear, int count) {
        System.out.println("Кількість транзакцій за " + monthYear + ": " + count);
    }

    public static void printTopExpensesReport(List<Transaction> topExpenses) {
        System.out.println("10 найбільших витрат:");
        for (Transaction expense : topExpenses) {
            System.out.println(expense.getDescription() + ": " + expense.getAmount());
        }
    }

    public static void printLargestAndSmallestExpenses(LocalDate from, LocalDate to,
                                                       Optional<Transaction> largest,
                                                       Optional<Transaction> smallest) {
        System.out.println("Витрати за період " + from + " — " + to + ":");
        if (!largest.isPresent()) {
            System.out.println("  За цей період витрат немає.");
            return;
        }
        System.out.println("  Найбільша: " + describe(largest.get()));
        System.out.println("  Найменша:  " + describe(smallest.get()));
    }

    public static String buildChart(String title, Map<String, Double> sums, double unit) {
        int labelWidth = 0;
        for (String label : sums.keySet()) {
            labelWidth = Math.max(labelWidth, label.length());
        }
        StringBuilder sb = new StringBuilder(title)
                .append(" (* = ").append(format(unit)).append(" грн)\n");
        for (Map.Entry<String, Double> entry : sums.entrySet()) {
            sb.append(String.format("%-" + labelWidth + "s | %s %s грн%n",
                    entry.getKey(), stars(entry.getValue(), unit), format(entry.getValue())));
        }
        return sb.toString();
    }

    public static void printExpensesByCategoryChart(Map<String, Double> byCategory, double unit) {
        System.out.print(buildChart("Витрати по категоріях", byCategory, unit));
    }

    public static void printExpensesByMonthChart(Map<String, Double> byMonth, double unit) {
        System.out.print(buildChart("Витрати по місяцях", byMonth, unit));
    }

    static String stars(double sum, double unit) {
        int count = (int) Math.round(sum / unit);
        if (count == 0 && sum > 0) {
            count = 1;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append('*');
        }
        return sb.toString();
    }

    private static String describe(Transaction t) {
        return t.getDescription() + " (" + t.getDate() + "): " + t.getAmount();
    }

    private static String format(double value) {
        return value == Math.rint(value) ? String.valueOf((long) value) : String.format("%.2f", value);
    }
}
