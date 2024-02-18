package database;

public class DatabaseDataReader {
    public String getDatabaseName() {
        return "test";
    }

    public String getDatabasePassword() {
        return "secret";
    }

    public String getDatabaseUrl() {
        return "jdbc:mysql://localhost:3306/test";
    }

    public String getDatabaseUser() {
        return "root";
    }
}
