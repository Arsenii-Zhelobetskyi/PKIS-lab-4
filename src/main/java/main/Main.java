package main;

import database.Database;
import database.DatabaseDataReader;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Database db = new Database();
        Connection connection = null;
        try {
            connection = db.connect("souvenirs", "root", "root");
        } catch (SQLException e) {
            System.out.println("Не вдалось підключитись до бази даних! Помилка: " + e.getMessage());
            return;
        }

        DatabaseDataReader dataReader = new DatabaseDataReader();

        System.out.println("Введіть ім`я виробника ->");
        String manufacturer = "Harvey-Mann";
        //dataReader.getSouvenirsByManufacturer(connection,scanner.nextLine() );
        dataReader.getSouvenirsByManufacturer(connection,manufacturer );

        System.out.println("Введіть ім`я виробника ->");
        String country = "United States";
        dataReader.getSouvenirsByCountry(connection, country);

        System.out.println("Введіть ціню ->");
        double price = 20;
        dataReader.getManufacturersByPrice(connection, price);

        System.out.println("Введіть назву сувеніру ->");
        String name = "nulla";
        System.out.println("Введіть рік ->");
        int year = 1994;
        dataReader.getManufacturersByYear(connection, name, year);



        db.closeConnection();
    }
}