package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    Connection connection = null;
    public Connection connect(String db, String user, String pass ) throws SQLException {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/"+db,
                    user, pass);
        } catch (SQLException e) {
            throw e; // Re-throwing the exception to be handled by the caller if necessary
        }
        return connection;
    }
    public  void closeConnection(){
        try {
            connection.close();
        } catch (SQLException e) {
         System.out.println("Помилка при закритті з'єднання з базою даних: " + e.getMessage());
        }
    }

}
