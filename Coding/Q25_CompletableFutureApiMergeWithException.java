package Coding;

import java.util.concurrent.CompletableFuture;

public class Q25_CompletableFutureApiMergeWithException {
    static String fetchProducts() {
        sleep(1000);
        return "Products: [Laptop, Phone, Tablet]";
    }

    static String fetchReviews() {
        sleep(800);
        throw new RuntimeException("Reviews API failed");
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
                CompletableFuture.supplyAsync(Q25_CompletableFutureApiMergeWithException::fetchProducts)
                        .exceptionally(ex -> {
                            System.out.println("Products API error: " + ex.getMessage());
                            return "Products: []";
                        });

        CompletableFuture<String> reviewsFuture =
                CompletableFuture.supplyAsync(Q25_CompletableFutureApiMergeWithException::fetchReviews)
                        .exceptionally(ex -> {
                            System.out.println("Reviews API error: " + ex.getMessage());
                            return "Reviews: []";
                        });

        CompletableFuture<String> inventoryFuture =
                CompletableFuture.supplyAsync(Q25_CompletableFutureApiMergeWithException::fetchInventory)
                        .exceptionally(ex -> {
                            System.out.println("Inventory API error: " + ex.getMessage());
                            return "Inventory: []";
                        });

        CompletableFuture<String> mergedFuture =
                productsFuture.thenCombine(reviewsFuture,
                                (products, reviews) -> products + "\n" + reviews)
                        .thenCombine(inventoryFuture,
                                (partial, inventory) -> partial + "\n" + inventory);

        System.out.println("Fetching data with exception handling...");
        System.out.println(mergedFuture.join());
    }
}
