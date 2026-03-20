package Coding;

import java.util.Objects;

public class ProductDemo {
    private String id;
    private String name;
    private double price;

    public ProductDemo(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
         return "Product{id='" + id + "', name='" + name + "', price=" + price + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ProductDemo product = (ProductDemo) obj;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static void main(String[] args) {
        ProductDemo p1 = new ProductDemo("P001", "Laptop", 999.99);
        ProductDemo p2 = new ProductDemo("P001", "Phone", 599.99);

        System.out.println("p1 equals p2: " + p1.equals(p2));

        System.out.println("p1 hashCode: " + p1.hashCode());
        System.out.println("p2 hashCode: " + p2.hashCode());

        System.out.println(p1);
        System.out.println(p2);
    }
}

