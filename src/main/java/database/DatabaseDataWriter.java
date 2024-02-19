package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.UUID;

public class DatabaseDataWriter {
    /**
     * Додає виробника та сувенір до бази даних
     * @param connection з'єднання з базою даних
     * @param manufacturer  дані про виробника
     * @param souvenir дані про сувенір
     */
    public void addManufacturerAndSouvenir(Connection connection, String[] manufacturer, Object[] souvenir) {
        try {

            UUID manufacturerId = UUID.randomUUID();
            String manufacturerIdString = manufacturerId.toString().substring(0,8); // Convert UUID to string

            // Додаємо виробника
            String addManufacturerQuery = "INSERT INTO manufacturers (id, name, country) VALUES (?, ?, ?)";
            try (PreparedStatement manufacturerStatement = connection.prepareStatement(addManufacturerQuery)) {
                manufacturerStatement.setString(1, manufacturerIdString);
                manufacturerStatement.setString(2, manufacturer[0]);
                manufacturerStatement.setString(3, manufacturer[1]);
                int rowsAffected = manufacturerStatement.executeUpdate();
                if (rowsAffected == 0) {
                    throw  new SQLException("Виробника " + manufacturer[0] + " не додано.");
                } else {
                    System.out.println("Виробника " + manufacturer[0] + " додано.");
                }
            }

            // Додаємо сувенір
            String addSouvenirQuery = "INSERT INTO souvenirs (id, name, manufacturer_s_details, date, price) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement souvenirStatement = connection.prepareStatement(addSouvenirQuery)) {
                souvenirStatement.setString(1, UUID.randomUUID().toString().substring(0,8));
                souvenirStatement.setString(2, souvenir[0].toString());
                souvenirStatement.setString(3,  manufacturerIdString);
                souvenirStatement.setString(4, souvenir[1].toString());
                souvenirStatement.setDouble(5, Double.parseDouble(souvenir[2].toString()));
                int rowsAffected2 = souvenirStatement.executeUpdate();
                if (rowsAffected2 == 0) {
                    throw  new SQLException("Сувенір " + souvenir[0] + " не додано.");
                } else {
                    System.out.println("Сувенір " + souvenir[0] + " додано.");
                }
            }
        } catch (SQLException | NumberFormatException e) {
            System.err.println("Помилка при доданні виробника та сувеніра до бази даних: " + e.getMessage());
        }
        catch (Exception e){
            System.err.println("Щось пішло не так: " + e.getMessage());
        }
    }


    /**
     * Видаляє виробника і його сувеніри з бази даних
     * @param connection з'єднання з базою даних
     * @param manufacturer назва виробника
     */
    public void deleteManufacturer(Connection connection, String manufacturer) {

        try{
            PreparedStatement statement = connection.prepareStatement("DELETE FROM manufacturers WHERE name = ?"); // використовується для виконання SQL-запитів з параметрами.
            statement.setString(1, manufacturer);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw  new SQLException("Виробника " + manufacturer + " не знайдено.");
            } else {
                System.out.println("Виробника " + manufacturer + " видалено.");
            }
            statement.close();
        }
        catch(SQLException e){
            System.err.println("Помилка при видаленні виробника з бази даних: " + e.getMessage());
        }

    }
}
