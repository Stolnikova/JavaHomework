package com.shop.order.processing.internal;

import com.shop.order.model.Clothing;
import com.shop.order.model.Electronics;
import com.shop.order.model.Order;
import com.shop.order.model.Product;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PriceCalculator {

    public static double discountRate(Order<? extends Product> order) {
        Product product = order.getProduct();
        if (product instanceof Electronics electronics && electronics.getWarrantyMonths() >= 24) {
            return 0.05;
        }
        if (product instanceof Clothing && order.getQuantity() >= 3) {
            return 0.10;
        }
        return 0;
    }

    public static double finalTotal(Order<? extends Product> order) {
        double total = order.getTotal() * (1 - discountRate(order));
        return Math.round(total * 100) / 100.0;
    }
}
