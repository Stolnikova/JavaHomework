package shop;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, "UTF-8");

        Category electronics = new Category(1, "Електроніка");
        Category smartphones = new Category(2, "Смартфони");
        Category accessories = new Category(3, "Аксесуари");

        Catalog catalog = new Catalog();
        catalog.addProduct(new Product(1, "Ноутбук", 19999.99, "Високопродуктивний ноутбук для роботи та ігор", electronics));
        catalog.addProduct(new Product(2, "Смартфон", 12999.50, "Смартфон з великим екраном та високою автономністю", smartphones));
        catalog.addProduct(new Product(3, "Навушники", 2499.00, "Бездротові навушники з шумозаглушенням", accessories));
        catalog.addProduct(new Product(4, "Планшет", 9999.00, "Легкий планшет для навчання та відео", electronics));
        catalog.addProduct(new Product(5, "Чохол для смартфона", 399.00, "Силіконовий чохол", accessories));

        Cart cart = new Cart();
        OrderHistory history = new OrderHistory("orders.txt");

        while (true) {
            System.out.println("\nВиберіть опцію:");
            System.out.println("1 - Переглянути список товарів");
            System.out.println("2 - Додати товар до кошика");
            System.out.println("3 - Переглянути кошик");
            System.out.println("4 - Зробити замовлення");
            System.out.println("5 - Видалити товар з кошика");
            System.out.println("6 - Переглянути історію замовлень");
            System.out.println("7 - Пошук товару за назвою");
            System.out.println("8 - Пошук товарів за категорією");
            System.out.println("0 - Вийти");

            int choice = readInt(scanner);
            switch (choice) {
                case 1:
                    printProducts(catalog.getProducts());
                    break;
                case 2: {
                    System.out.println("Введіть ID товару для додавання до кошика:");
                    Product product = catalog.findById(readInt(scanner));
                    if (product != null) {
                        cart.addProduct(product);
                        System.out.println("Додано до кошика: " + product.getName());
                    } else {
                        System.out.println("Товар з таким ID не знайдено");
                    }
                    break;
                }
                case 3:
                    System.out.println(cart);
                    break;
                case 4:
                    if (cart.isEmpty()) {
                        System.out.println("Кошик порожній. Додайте товари перед оформленням замовлення.");
                    } else {
                        Order order = new Order(history.nextOrderId(), cart);
                        history.addOrder(order);
                        System.out.println("Замовлення оформлено:");
                        System.out.println(order);
                        System.out.println("Замовлення збережено у файл " + history.getFileName());
                        cart.clear();
                    }
                    break;
                case 5:
                    if (cart.isEmpty()) {
                        System.out.println("Кошик порожній.");
                        break;
                    }
                    System.out.println(cart);
                    System.out.println("Введіть ID товару для видалення з кошика:");
                    if (cart.removeProductById(readInt(scanner))) {
                        System.out.println("Товар видалено з кошика.");
                    } else {
                        System.out.println("У кошику немає товару з таким ID.");
                    }
                    break;
                case 6:
                    System.out.println(history);
                    break;
                case 7:
                    System.out.println("Введіть назву (або її частину):");
                    printProducts(catalog.searchByName(scanner.nextLine()));
                    break;
                case 8:
                    System.out.println("Введіть назву категорії (або її частину):");
                    printProducts(catalog.searchByCategory(scanner.nextLine()));
                    break;
                case 0:
                    System.out.println("Дякуємо, що використовували наш магазин!");
                    return;
                default:
                    System.out.println("Невідома опція. Спробуйте ще раз.");
                    break;
            }
        }
    }

    private static int readInt(Scanner scanner) {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Введіть число:");
            }
        }
    }

    private static void printProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("Товари не знайдено.");
            return;
        }
        for (Product product : products) {
            System.out.println(product);
        }
    }
}
