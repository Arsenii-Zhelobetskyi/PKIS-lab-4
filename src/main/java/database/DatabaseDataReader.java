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
import java.util.Objects;

public class DatabaseDataReader {

    interface Function {
        Object[] apply(ResultSet resultSet);
    }

    private void printTable(ResultSet resultSet, String[] header, List<Object[]> list) {

            AsciiTable at = new AsciiTable(); // виводимо результати
            at.addRule();
            at.addRow(header);// хедер таблиці
            for (Object[] row : list) {
                at.addRule();
                at.addRow(row);
            }

            at.addRule();// роздільник
            CWC_LongestLine cwc = new CWC_LongestLine(); // щоб таблиця мала правильні розміри
            at.getRenderer().setCWC(cwc);// щоб таблиця мала правильні розміри
            System.out.println(at.render());

    }

    /**
     * Вивести інформацію про сувеніри заданого виробника.
     *
     * @param connection   - з'єднання з базою даних
     * @param manufacturer - назва виробника
     */
    public void getSouvenirsByManufacturer(Connection connection, String manufacturer) {
        try {

            PreparedStatement statement = connection.prepareStatement("SELECT id FROM manufacturers WHERE name = ?"); // використовується для виконання SQL-запитів з параметрами.
            statement.setString(1, manufacturer); // Встановлюємо значення параметру
            ResultSet resultSet = statement.executeQuery(); // Виконуємо запит

            int id = 0; // id, щоб за ним вже знайти інформацію про сувеніри
            if (resultSet.next()) {
                id = resultSet.getInt("id"); // Отримуємо значення id з ResultSet

            } else {
                throw new SQLException("Виробника " + manufacturer + " не знайдено.");
            }


            statement = connection.prepareStatement("SELECT id,name,date,price FROM souvenirs WHERE manufacturer_s_details = ?"); //шукаємо інформацію про сувеніри за id виробника
            statement.setInt(1, id);
            resultSet = statement.executeQuery();


            List<Object[]> list = new ArrayList<>();

            while (resultSet.next()) {
                list.add(new Object[]{resultSet.getInt("id"), resultSet.getString("name"), resultSet.getDate("date"), resultSet.getDouble("price")});
            }

            printTable(resultSet, new String[]{"ID", "Name", "Date", "Price"}, list); // виводимо результати


            resultSet.close(); // очищаємо ресурси, щоб не займати пам'ять
            statement.close();

        } catch (SQLException e) { // обробляємо помилки
            System.err.println("Помилка при спробі вивести інформацію про сувеніри за заданим виробником: " + e.getMessage());
        }
    }

    public void getSouvenirsByCountry(Connection connection, String country) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT id,name,date,price FROM souvenirs WHERE manufacturer_s_details IN (SELECT id FROM manufacturers WHERE country = ?)");

            statement.setString(1, country); // Встановлюємо значення параметру
            ResultSet resultSet = statement.executeQuery(); // Виконуємо запит

            if (!resultSet.next()) {
                throw new SQLException("Виробників з країни " + country + " не знайдено.");
            }

            List<Object[]> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new Object[]{resultSet.getInt("id"), resultSet.getString("name"), resultSet.getDate("date"), resultSet.getDouble("price")});
            }
            printTable(resultSet, new String[]{"ID", "Name", "Date", "Price"}, list);  // виводимо результати


            resultSet.close(); // очищаємо ресурси, щоб не займати пам'ять
            statement.close();
        } catch (SQLException e) { // обробляємо помилки
            System.err.println("Помилка при спробі вивести інформацію про сувеніри за заданим виробником: " + e.getMessage());
        }

    }

    public  void getManufacturersByPrice(Connection connection, double price){
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT id,name,country FROM manufacturers WHERE id IN (SELECT manufacturer_s_details from souvenirs WHERE price < ?)");
            statement.setDouble(1, price); // Встановлюємо значення параметру
            ResultSet resultSet = statement.executeQuery(); // Виконуємо запит

            if (!resultSet.next()) {
                throw new SQLException("Сувенірів з ціною менше ніж" + price + " не знайдено.");
            }

            List<Object[]> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new Object[]{resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("country")});
            }
            printTable(resultSet, new String[]{"ID", "Name", "Country"}, list);  // виводимо результати


            resultSet.close(); // очищаємо ресурси, щоб не займати пам'ять
            statement.close();
        } catch (SQLException e) { // обробляємо помилки
            System.err.println("Помилка при спробі вивести інформацію про сувеніри за заданим виробником: " + e.getMessage());
        }
    }

}
