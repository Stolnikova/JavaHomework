package finance;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String filePath = "https://informer.com.ua/dut/java/pr2.csv";

        List<Transaction> transactions = TransactionCSVReader.readTransactions(filePath);

        System.out.println("Транзакції з файлу:");
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
        System.out.println();

        double totalBalance = TransactionAnalyzer.calculateTotalBalance(transactions);
        TransactionReportGenerator.printBalanceReport(totalBalance);

        String monthYear = "01-2024";
        int transactionsCount = TransactionAnalyzer.countTransactionsByMonth(transactions, monthYear);
        TransactionReportGenerator.printTransactionsCountByMonth(monthYear, transactionsCount);
        System.out.println();

        List<Transaction> topExpenses = TransactionAnalyzer.findTopExpenses(transactions);
        TransactionReportGenerator.printTopExpensesReport(topExpenses);
        System.out.println();

        LocalDate from = LocalDate.of(2023, 12, 1);
        LocalDate to = LocalDate.of(2023, 12, 31);
        TransactionReportGenerator.printLargestAndSmallestExpenses(from, to,
                TransactionAnalyzer.findLargestExpense(transactions, from, to),
                TransactionAnalyzer.findSmallestExpense(transactions, from, to));
        System.out.println();

        double unit = 1000;
        TransactionReportGenerator.printExpensesByCategoryChart(
                TransactionAnalyzer.sumExpensesByCategory(transactions), unit);
        System.out.println();
        TransactionReportGenerator.printExpensesByMonthChart(
                TransactionAnalyzer.sumExpensesByMonth(transactions), unit);
    }
}
