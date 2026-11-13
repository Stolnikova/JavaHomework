package shop;

import java.util.ArrayList;
import java.util.List;

public class Catalog {
    private final List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public Product findById(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    public List<Product> searchByName(String query) {
        String q = query.trim().toLowerCase();
        List<Product> result = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(q)) {
                result.add(product);
            }
        }
        return result;
    }

    public List<Product> searchByCategory(String query) {
        String q = query.trim().toLowerCase();
        List<Product> result = new ArrayList<>();
        for (Product product : products) {
            Category category = product.getCategory();
            if (category != null && category.getName().toLowerCase().contains(q)) {
                result.add(product);
            }
        }
        return result;
    }
}
