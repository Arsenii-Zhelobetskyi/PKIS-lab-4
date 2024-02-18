package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    Connection connection = null;

    public Connection connect() throws SQLException {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/test",
                    "root", "secret");
        } catch (SQLException e) {
            throw e; // Re-throwing the exception to be handled by the caller if necessary
        }
        return connection;
    }
}
