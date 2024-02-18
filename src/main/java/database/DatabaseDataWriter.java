package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class DatabaseDataWriter {
    public void addManufacturerAndSouvenir(Connection connection, Object[] manufacturer, Object[] souvenir) {
try {
    Random random = new Random();
    int randomId= random.nextInt(99999);
    PreparedStatement statement = connection.prepareStatement("INSERT INTO manufacturers (id,name,country) VALUES ("+randomId+",?,?)"); // використовується для виконання SQL-запитів з параметрами.
    statement.setString(1,  manufacturer[0].toString());
    statement.setString(2,  manufacturer[1].toString());

    int rowsAffected = statement.executeUpdate();
    if (rowsAffected == 0) {
        System.out.println("Виробника " + manufacturer[0] + " не додано.");
    } else {
        System.out.println("Виробника " + manufacturer[0] + " додано.");
    }

    statement = connection.prepareStatement("INSERT INTO souvenirs (id,name,manufacturer_s_details,date,price) VALUES ("+random.nextInt(99999)+",?,"+randomId+",?,?)"); // використовується для виконання SQL-запитів з параметрами.

    statement.setString(1,  souvenir[0].toString());
    statement.setString(2,  souvenir[1].toString());
    statement.setString(3,  souvenir[2].toString());
    int rowsAffected2 = statement.executeUpdate();
    if (rowsAffected2 == 0) {
        System.out.println("Сувенір " + manufacturer[0] + " не додано.");
    } else {
        System.out.println("Сувенір " + manufacturer[0] + " додано.");
    }
    statement.close();
} catch (SQLException e){
    System.err.println("Помилка при доданны виробника до бази даних: " + e.getMessage());
}
    }
    public void deleteManufacturer(Connection connection, String manufacturer) {

        try{
            PreparedStatement statement = connection.prepareStatement("DELETE FROM manufacturers WHERE name = ?"); // використовується для виконання SQL-запитів з параметрами.
            statement.setString(1, manufacturer);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Виробника " + manufacturer + " не знайдено.");
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
