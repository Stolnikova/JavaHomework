package shop;

import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
public class Order {
    private final int id;
    private final List<Product> products;
    private final double totalPrice;
    private final Date createdAt;
    @Setter
    private String status;

    public Order(int id, Cart cart) {
        this.id = id;
        this.products = new ArrayList<>(cart.getProducts());
        this.totalPrice = cart.getTotalPrice();
        this.createdAt = new Date();
        this.status = "Нове";
    }

    @Override
    public String toString() {
        String date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(createdAt);
        StringBuilder sb = new StringBuilder("Замовлення №").append(id)
                .append(" від ").append(date).append(":\n");
        for (Product product : products) {
            sb.append(product.toString()).append("\n");
        }
        sb.append("Загальна вартість: ").append(String.format("%.2f", totalPrice)).append("\n");
        sb.append("Статус: ").append(status);
        return sb.toString();
    }
}
