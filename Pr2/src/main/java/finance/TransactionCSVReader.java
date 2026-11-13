package finance;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class TransactionCSVReader {

    public static List<Transaction> readTransactions(String filePath) {
        return readTransactions(LineSource.of(filePath));
    }

    public static List<Transaction> readTransactions(LineSource source) {
        try {
            return parseTransactions(source.readLines());
        } catch (IOException e) {
            System.out.println("Не вдалося прочитати дані: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static List<Transaction> parseTransactions(List<String> lines) {
        List<Transaction> transactions = new ArrayList<>();
        for (String line : lines) {
            Transaction transaction = parseLine(line);
            if (transaction != null) {
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    public static Transaction parseLine(String line) {
        if (line == null) {
            return null;
        }
        String clean = line.replace("﻿", "").trim();
        if (clean.isEmpty()) {
            return null;
        }
        String[] values = clean.split(",", 3);
        if (values.length < 3) {
            return null;
        }
        try {
            double amount = Double.parseDouble(values[1].trim());
            return new Transaction(values[0].trim(), amount, values[2].trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
