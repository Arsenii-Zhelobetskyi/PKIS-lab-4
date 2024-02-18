package database;


import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import java.util.ArrayList;
import java.util.List;
public class DatabaseDataReader {
    public void getSouvenirsByManufacturer(Connection connection, String manufacturer) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT id FROM manufacturers WHERE name = ?");
            statement.setString(1, manufacturer);
            ResultSet resultSet = statement.executeQuery();

            int id = 0;
            // Переміщуємо курсор на перший рядок (якщо такий є)
            if (resultSet.next()) {
                id = resultSet.getInt("id"); // Отримуємо значення id з ResultSet

            } else {
                System.out.println("Manufacturer " + manufacturer + " not found.");
            }


            statement = connection.prepareStatement(
                    "SELECT * FROM souvenirs WHERE manufacturer_s_details = ?");
            statement.setInt(1, id);
            resultSet= statement.executeQuery();


            AsciiTable at = new AsciiTable();

            at.addRule();
            at.addRow("ID", "Name", "Date", "Price");
            while(resultSet.next()) {
                at.addRule();
                at.addRow(resultSet.getInt("id"), resultSet.getString("name"),resultSet.getDate("date"), resultSet.getDouble("price"));
            }

            at.addRule();
            CWC_LongestLine cwc = new CWC_LongestLine();
            at.getRenderer().setCWC(cwc);

            System.out.println(at.render());



            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            System.err.println("Помилка при виконанні запиту до бази даних: " + e.getMessage());
        }
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
