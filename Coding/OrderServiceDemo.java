package Coding;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.text.NumberFormat;

class Product {
    private String id;
    private String name;
    private BigDecimal price;
    private String category;
    private boolean available;

    public Product(String id, String name, BigDecimal price, String category, boolean available) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public boolean isAvailable() {
        return available;
    }

    @Override
    public String toString() {
        return name + " (" + category + ", " + OrderProcessor.formatPrice(price) + ")";
    }
}

class Order {
    private String orderId;
    private LocalDateTime orderDate;
    private List<Product> items;
    private String customerEmail;

    public Order(String orderId, LocalDateTime orderDate, List<Product> items, String customerEmail) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.items = items;
        this.customerEmail = customerEmail;
    }

    public String getOrderId() {
        return orderId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public List<Product> getItems() {
        return items;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    @Override
    public String toString() {
        return "Order{id='" + orderId + "', customer='" + customerEmail + "'}";
    }
}

interface OrderProcessor {
    default BigDecimal calculateTotal(Order order) {
        return order.getItems().stream()
            .map(Product::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    static String formatPrice(BigDecimal price) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        return formatter.format(price);
    }

    void processOrder(Order order);
}

class OrderService implements OrderProcessor {

    @Override
    public void processOrder(Order order) {
        System.out.println("Processing order: " + order.getOrderId());
        System.out.println("Customer: " + order.getCustomerEmail());
        System.out.println("Total: " + OrderProcessor.formatPrice(calculateTotal(order)));
    }

    public List<Order> filterOrders(List<Order> orders, Predicate<Order> condition) {
        return orders.stream()
            .filter(condition)
            .collect(Collectors.toList());
    }

    public Map<String, List<Order>> groupOrdersByCategory(List<Order> orders) {
        return orders.stream()
            .filter(order -> !order.getItems().isEmpty())
            .collect(Collectors.groupingBy(order -> order.getItems().get(0).getCategory()));
    }

    public Optional<Order> findMostExpensiveOrder(List<Order> orders) {
        return orders.stream()
            .max(Comparator.comparing(this::calculateTotal));
    }
}

public class OrderServiceDemo {
    public static void main(String[] args) {
        Product laptop = new Product("P1", "Laptop", new BigDecimal("999.99"), "Electronics", true);
        Product mouse = new Product("P2", "Mouse", new BigDecimal("25.50"), "Electronics", true);
        Product book = new Product("P3", "Java Book", new BigDecimal("45.00"), "Books", true);
        Product chair = new Product("P4", "Chair", new BigDecimal("120.00"), "Furniture", true);

        Order order1 = new Order(
                "O1",
                LocalDateTime.now(),
                Arrays.asList(laptop, mouse),
                "a@example.com"
        );

        Order order2 = new Order(
                "O2",
                LocalDateTime.now(),
                Arrays.asList(book),
                "b@example.com"
        );

        Order order3 = new Order(
                "O3",
                LocalDateTime.now(),
                Arrays.asList(chair, mouse),
                "c@example.com"
        );

        List<Order> orders = Arrays.asList(order1, order2, order3);

        OrderService service = new OrderService();

        System.out.println("=== Process Orders ===");
        orders.forEach(service::processOrder);

        System.out.println("\n=== Orders with total > $100 ===");
        List<Order> filteredOrders = service.filterOrders(
                orders,
                order -> service.calculateTotal(order).compareTo(new BigDecimal("100")) > 0
        );
        filteredOrders.forEach(System.out::println);

        System.out.println("\n=== Group Orders By Category ===");
        Map<String, List<Order>> grouped = service.groupOrdersByCategory(orders);
        grouped.forEach((category, orderList) -> {
            System.out.println(category + ": " + orderList);
        });

        System.out.println("\n=== Most Expensive Order ===");
        Optional<Order> mostExpensive = service.findMostExpensiveOrder(orders);
        mostExpensive.ifPresent(order -> {
            System.out.println("Most expensive order: " + order.getOrderId());
            System.out.println("Total: " + OrderProcessor.formatPrice(service.calculateTotal(order)));
        });
    }
}
