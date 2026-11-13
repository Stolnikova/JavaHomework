package finance;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

class TransactionCSVReaderTest {

    @Test
    public void testParseTransactionsFromLines() {
        List<Transaction> transactions = TransactionCSVReader.readTransactions(LineSource.fromLines(
                "05-12-2023,-7850,Сільпо",
                "10-12-2023,80000,Зарплата"));

        Assertions.assertEquals(2, transactions.size());
        Assertions.assertEquals(new Transaction("05-12-2023", -7850, "Сільпо"), transactions.get(0));
        Assertions.assertEquals(new Transaction("10-12-2023", 80000, "Зарплата"), transactions.get(1));
    }

    @Test
    public void testReadTransactionsFromFile(@TempDir Path tempDir) throws IOException {
        Path csv = tempDir.resolve("test.csv");
        Files.write(csv, Arrays.asList(
                "﻿07-12-2023,-1200,Аптека",
                "",
                "16-12-2023,25000,Фріланс"), StandardCharsets.UTF_8);

        List<Transaction> transactions = TransactionCSVReader.readTransactions(csv.toString());

        Assertions.assertEquals(2, transactions.size());
        Assertions.assertEquals("07-12-2023", transactions.get(0).getDate());
        Assertions.assertEquals(-1200.0, transactions.get(0).getAmount());
        Assertions.assertEquals("Фріланс", transactions.get(1).getDescription());
    }

    @Test
    public void testInvalidLinesAreSkipped() {
        List<Transaction> transactions = TransactionCSVReader.parseTransactions(Arrays.asList(
                "date,amount,description",
                "12-12-2023,не число,Помилка",
                "неповний рядок",
                "18-12-2023,-600,Кав'ярня"));

        Assertions.assertEquals(1, transactions.size());
        Assertions.assertEquals("Кав'ярня", transactions.get(0).getDescription());
    }

    @Test
    public void testDescriptionWithComma() {
        Transaction t = TransactionCSVReader.parseLine("20-12-2023,-7500,Подарунки, квіти");

        Assertions.assertEquals("Подарунки, квіти", t.getDescription());
        Assertions.assertEquals(-7500.0, t.getAmount());
    }

    @Test
    public void testMissingFileGivesEmptyList() {
        List<Transaction> transactions = TransactionCSVReader.readTransactions("немає-такого-файлу.csv");

        Assertions.assertTrue(transactions.isEmpty());
    }
}
