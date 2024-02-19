package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Клас для з'єднання з базою даних
 */
public class Database {
    Connection connection = null;
    public Connection connect(String db, String user, String pass ) throws SQLException {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/"+db,
                    user, pass);
        } catch (SQLException e) {
            throw e;
        }
        return connection;
    }
    public  void closeConnection(){
        try {
            connection.close();
        } catch (SQLException e) {
         System.err.println("Помилка при закритті з'єднання з базою даних: " + e.getMessage());
        }
    }

}
