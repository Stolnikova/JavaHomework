package finance;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

class TransactionReportGeneratorTest {

    @Test
    public void testStarsOneSymbolPerUnit() {
        Assertions.assertEquals("*****", TransactionReportGenerator.stars(5000, 1000));
        Assertions.assertEquals("********", TransactionReportGenerator.stars(7850, 1000));
        Assertions.assertEquals("*", TransactionReportGenerator.stars(300, 1000));
        Assertions.assertEquals("", TransactionReportGenerator.stars(0, 1000));
    }

    @Test
    public void testBuildChart() {
        Map<String, Double> sums = new LinkedHashMap<>();
        sums.put("Сільпо", 3000.0);
        sums.put("Кава", 600.0);

        String chart = TransactionReportGenerator.buildChart("Витрати по категоріях", sums, 1000);

        Assertions.assertTrue(chart.startsWith("Витрати по категоріях (* = 1000 грн)"));
        Assertions.assertTrue(chart.contains("Сільпо | *** 3000 грн"));
        Assertions.assertTrue(chart.contains("Кава   | * 600 грн"));
    }
}
