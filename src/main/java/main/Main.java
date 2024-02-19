package main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import database.Database;
import database.DatabaseDataReader;
import database.DatabaseDataWriter;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Створюємо об'єкт класу Scanner для зчитування введення користувача

        Database db = new Database(); // Створюємо об'єкт класу Database для з'єднання з базою даних
        DatabaseDataReader dataReader = new DatabaseDataReader(); // Створюємо об'єкт класу DatabaseDataReader для читання даних з бази даних
        DatabaseDataWriter dataWriter = new DatabaseDataWriter(); // Створюємо об'єкт класу DatabaseDataWriter для запису даних в базу даних
        Connection connection = null;
        try { // Встановлюємо з'єднання з базою даних
            connection = db.connect("souvenirs", "root", "root");
        } catch (SQLException e) {
            System.out.println("Не вдалось підключитись до бази даних! Помилка: " + e.getMessage());
            return;
        }

        int choice;
        try {
            do {
                // Виводимо меню
                System.out.println("Меню:");
                System.out.println("1. Вивести інформацію про сувеніри заданого виробника");
                System.out.println("2. Вивести інформацію про сувеніри заданої країни");
                System.out.println("3. Вивести інформацію про виробників, ціна на сувеніри яких менше заданої");
                System.out.println("4. Вивести інформацію про виробників заданого сувеніру, який виготовлений у заданому році");
                System.out.println("5. Видалити вказаного виробника та всі його сувеніри");
                System.out.println("6. Додати нового виробника та сувенір");
                System.out.println("7. Вийти з програми");
                System.out.print("Ваш вибір: ");

                // Отримуємо введений користувачем вибір
                choice = scanner.nextInt();
                scanner.nextLine();
                // Обробляємо вибір користувача
                switch (choice) {
                    case 1:
                        System.out.print("Введіть ім`я виробника ->");
                        dataReader.getSouvenirsByManufacturer(connection, scanner.nextLine()); //Howell Group
                        break;
                    case 2:
                        System.out.print("Введіть назву країни ->");
                        dataReader.getSouvenirsByCountry(connection, scanner.nextLine()); //United States
                        break;
                    case 3:
                        System.out.print("Введіть ціню ->");
                        dataReader.getManufacturersByPrice(connection, scanner.nextDouble());//20
                        break;
                    case 4:
                        System.out.print("Введіть назву сувеніру ->"); //eget
                        String name=scanner.nextLine();
                        System.out.print("Введіть рік ->");
                        int year = scanner.nextInt();
                        dataReader.getManufacturersByYear(connection, name, year);//2003
                        break;
                    case 5:
                        System.out.print("Введіть назву виробника ->");
                        dataWriter.deleteManufacturer(connection, scanner.nextLine());// "Borer, Yost and Ruecker"
                        break;
                    case 6:
                        System.out.print("Введіть назву виробника ->");
                        String name1 = scanner.nextLine();
                        System.out.print("Введіть країну виробника ->");
                        String country= scanner.nextLine();
                        System.out.print("Введіть назву сувеніру ->");
                        String name2 = scanner.nextLine();
                        System.out.print("Введіть дату виготовлення сувеніру(YYYY-MM-DD) ->");
                        String date = scanner.nextLine();
                        System.out.print("Введіть ціну ->");
                        double price = scanner.nextDouble();

                        dataWriter.addManufacturerAndSouvenir(connection, new String[]{name1, country}, new Object[]{name2, date, price});
                        break;
                    case 7:
                        break;

                    default:
                        System.out.println("Ви вибрали неправильну опцію");
                        break;
                }

            } while (choice != 7);
        } catch (Exception e) {
            System.err.println("Щось пішло не так: " + e.getMessage());
        }
        db.closeConnection();
    }
}