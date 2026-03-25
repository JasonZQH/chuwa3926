package Coding;

public class Q16_DatabaseConnection {
    
    static class DatabaseConnection {
        private DatabaseConnection() {
            System.out.println("Database connection created");
        }

        private static class Singleton {
            private static final DatabaseConnection INSTANCE = new DatabaseConnection();
        }

        public static DatabaseConnection getInstance() {
            return Singleton.INSTANCE;
        }

        public void executeQuery(String sql) {
            System.out.println("Executing: " + sql);
        }
    }

    public static void main(String[] args) {
        DatabaseConnection db1 = DatabaseConnection.getInstance();
        DatabaseConnection db2 = DatabaseConnection.getInstance();

        System.out.println("Same object? " + (db1 == db2));

        db1.executeQuery("SELECT * FROM users");
    }

}
