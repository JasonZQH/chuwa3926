package Coding;

import java.util.concurrent.CompletableFuture;

public class Q25_CompletableFutureApiMerge {
    static String fetchProducts() {
        sleep(1000);
        return "Products: [Laptop, Phone, Tablet]";
    }

    static String fetchReviews() {
        sleep(800);
        return "Reviews: [4.5, 4.8, 4.2]";
    }

     static String fetchInventory() {
        sleep(1200);
        return "Inventory: [23, 15, 40]";
    }


    static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } 
    }

    public static void main(String[] args) {
        CompletableFuture<String> productsFuture =
                CompletableFuture.supplyAsync(Q25_CompletableFutureApiMerge::fetchProducts);

        CompletableFuture<String> reviewsFuture =
                CompletableFuture.supplyAsync(Q25_CompletableFutureApiMerge::fetchReviews);

        CompletableFuture<String> inventoryFuture =
                CompletableFuture.supplyAsync(Q25_CompletableFutureApiMerge::fetchInventory);

        CompletableFuture<String> mergedFuture =
                productsFuture.thenCombine(reviewsFuture,
                                (products, reviews) -> products + "\n" + reviews)
                        .thenCombine(inventoryFuture,
                                (partial, inventory) -> partial + "\n" + inventory);

        System.out.println("Fetching data from 3 APIs...");
        System.out.println(mergedFuture.join());
    }
}
