package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import java.util.ArrayList;
import java.util.List;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;


public class DatabaseDataReader {

    /**
     * функція для виводу результатів
     *
     * @param header- заголовок таблиці
     * @param list    - список з результатами
     */
    private void printTable(String[] header, List<Object[]> list) {

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
        // тут використовуємо конструкцію try-with-resources, щоб автоматично закрити ресурси

        try (PreparedStatement statement = connection.prepareStatement("SELECT id, name, date, price FROM souvenirs WHERE manufacturer_s_details = (SELECT id FROM manufacturers WHERE name = ?)")) {
            statement.setString(1, manufacturer); // Встановлюємо значення параметру

            try (ResultSet resultSet = statement.executeQuery()) { // Виконуємо запит

                List<Object[]> list = new ArrayList<>(); // Створюємо список для зберігання результатів
                while (resultSet.next()) { // заповнюємо список результатами
                    list.add(new Object[]{resultSet.getInt("id"), resultSet.getString("name"), resultSet.getDate("date"), resultSet.getDouble("price")});
                }
                if (list.isEmpty()) {
                    throw new SQLException("Виробника " + manufacturer + " не знайдено.");
                } else { // виводимо результати
                    printTable(new String[]{"ID", "Name", "Date", "Price"}, list);
                }
            }

        } catch (SQLException e) {

            System.err.println("Помилка при спробі вивести інформацію про сувеніри за заданим виробником: " + e.getMessage());
        }
    }

    public void getSouvenirsByCountry(Connection connection, String country) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT id, name, date, price FROM souvenirs WHERE manufacturer_s_details IN (SELECT id FROM manufacturers WHERE country = ?)")) {
            statement.setString(1, country);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Object[]> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(new Object[]{resultSet.getInt("id"), resultSet.getString("name"), resultSet.getDate("date"), resultSet.getDouble("price")});
                }
                if (list.isEmpty()) {
                    throw new SQLException("Сувенірів від виробників з країни " + country + " не знайдено.");

                } else {
                    printTable(new String[]{"ID", "Name", "Date", "Price"}, list);
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка при спробі вивести інформацію про сувеніри за заданою країною: " + e.getMessage());
        }
    }


    public void getManufacturersByPrice(Connection connection, double price) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT id, name, country FROM manufacturers WHERE id IN (SELECT manufacturer_s_details FROM souvenirs WHERE price < ?)")) {
            statement.setDouble(1, price);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Object[]> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(new Object[]{resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("country")});
                }
                if (list.isEmpty()) {
                    throw new SQLException("Виробників сувенірів з ціною менше ніж " + price + " не знайдено.");
                } else {
                    printTable(new String[]{"ID", "Name", "Country"}, list);
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка при спробі вивести інформацію про виробників за ціною: " + e.getMessage());
        }
    }



    public void getManufacturersByYear(Connection connection, String name, int year) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT id, name, country FROM manufacturers WHERE id IN (SELECT manufacturer_s_details FROM souvenirs WHERE name = ? AND YEAR(date) = ?)")) {
            statement.setString(1, name);
            statement.setInt(2, year);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Object[]> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(new Object[]{resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("country")});
                }
                if (list.isEmpty()) {
                    System.out.println("Виробників сувенірів з назвою " + name + " та роком " + year + " не знайдено.");
                } else {
                    printTable(new String[]{"ID", "Name", "Country"}, list);
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка при спробі вивести інформацію про виробників за роком: " + e.getMessage());
        }
    }
}
