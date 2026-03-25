package Coding;

public class Q14_OrderStatus {
    interface IStatusCode {
        int getCode();
        String getDescription();
    }

    enum OrderStatus implements IStatusCode {
        PENDING(0, "Order is pending"),
        PAID(1, "Payment received"),
        SHIPPED(2, "Order has been shipped"),
        DELIVERED(3, "Order delivered"),
        CANCELLED(-1, "Order cancelled");

        private final int code;
        private final String description;

        OrderStatus(int code, String description) {
            this.code = code;
            this.description = description;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }

    public static void main(String[] args) {
        // 1. iterate all enum values
        for (OrderStatus status : OrderStatus.values()) {
            System.out.println(
                    status + " | " +
                    status.getCode() + " | " +
                    status.getDescription()
            );
        }

        System.out.println("-----");

        // 2. get enum by name
        OrderStatus paid = OrderStatus.valueOf("PAID");
        System.out.println("Selected: " + paid);
        System.out.println("Code: " + paid.getCode());
        System.out.println("Description: " + paid.getDescription());
    }
}
