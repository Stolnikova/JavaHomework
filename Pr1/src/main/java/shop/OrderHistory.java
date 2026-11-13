package shop;

import lombok.Getter;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class OrderHistory {
    @Getter
    private final String fileName;
    private final List<Order> orders = new ArrayList<>();

    public OrderHistory(String fileName) {
        this.fileName = fileName;
    }

    public void addOrder(Order order) {
        orders.add(order);
        saveToFile(order);
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }

    public int nextOrderId() {
        return orders.size() + 1;
    }

    private void saveToFile(Order order) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(fileName, true), StandardCharsets.UTF_8))) {
            writer.write(order.toString());
            writer.newLine();
            writer.write("----------------------------------------");
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Не вдалося зберегти замовлення у файл: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        if (orders.isEmpty()) {
            return "Історія замовлень порожня.";
        }
        StringBuilder sb = new StringBuilder("Історія замовлень:\n");
        for (Order order : orders) {
            sb.append(order).append("\n\n");
        }
        return sb.toString().trim();
    }
}
