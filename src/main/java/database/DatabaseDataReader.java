package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import java.util.ArrayList;
import java.util.List;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import java.util.function.Function;


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
     * універсальна функція, яка виконує SQL-запит до бази даних і обробляє результати.
     * @param connection    з'єднання з базою даних
     * @param query         SQL-запит, який треба виконати
     * @param tableHeader   Масив рядків, що містить назви стовпців таблиці
     * @param errorMessage  Повідомлення, яке виводиться, якщо результати запиту порожні
     * @param mapper        Лямбда-вираз, що конвертує результати запиту в об'єкти масиву
     * @param parameters    Параметри запиту
     */
    public void getDataByQuery(Connection connection, String query,String[] tableHeader, String errorMessage, Function<ResultSet, Object[]> mapper, Object... parameters) {

        try (PreparedStatement statement = connection.prepareStatement(query)) { // створюємо стейтмент, щоб зробити запит
            for (int i = 0; i < parameters.length; i++) { // заповнюємо параметри в запиті
                if (parameters[i] instanceof String) {
                    statement.setString(i + 1, (String) parameters[i]);
                } else if (parameters[i] instanceof Double) {
                    statement.setDouble(i + 1, (Double) parameters[i]);
                } else if (parameters[i] instanceof Integer) {
                    statement.setInt(i + 1, (Integer) parameters[i]);
                }
            }

            try (ResultSet resultSet = statement.executeQuery()) { // виконуємо запит
                List<Object[]> list = new ArrayList<>();
                while (resultSet.next()) {// розбираємо результати у список, щоб їх можна було відобразити у таблиці
                    list.add(mapper.apply(resultSet));
                }
                if (list.isEmpty()) {// якщо список пустий
                    throw new SQLException(errorMessage);
                } else {
                    printTable(tableHeader, list);//друкуємо таблицю
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка: " + e.getMessage());
        }
    }
    /**
     * Вивести інформацію про сувеніри заданого виробника.
     *
     * @param connection   - з'єднання з базою даних
     * @param manufacturer - назва виробника
     */
    public void getSouvenirsByManufacturer(Connection connection, String manufacturer) {
        getDataByQuery(connection,
                "SELECT id, name, date, price FROM souvenirs WHERE manufacturer_s_details = (SELECT id FROM manufacturers WHERE name = ?)",
                new String[] {"ID", "Name", "Date", "Price"},
                "Виробника " + manufacturer + " не знайдено.",
                resultSet -> {
                    try {
                        return new Object[]{resultSet.getString("id"), resultSet.getString("name"), resultSet.getDate("date"), resultSet.getDouble("price")};
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                },
                manufacturer
        );
    }

    /**
     * вивести інформацію про сувеніри, що виготовлені у заданій країні
     * @param connection    з'єднання з базою даних
     * @param country країна
     */
    public  void getSouvenirsByCountry(Connection connection, String country) {
        getDataByQuery(connection,
                "SELECT id, name, date, price FROM souvenirs WHERE manufacturer_s_details IN (SELECT id FROM manufacturers WHERE country = ?)",
                new String[] {"ID", "Name", "Date", "Price"},
                "Сувенірів від виробників з країни " + country + " не знайдено.",
                resultSet -> {
                    try {
                        return new Object[]{resultSet.getString("id"), resultSet.getString("name"), resultSet.getDate("date"), resultSet.getDouble("price")};
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                },
                country
        );
    }

    /**
     * Вивести інформацію про виробників, ціни на сувеніри яких менші за задану
     * @param connection  з'єднання з базою даних
     * @param price ціна
     */
    public void getManufacturersByPrice(Connection connection, double price) {
        getDataByQuery(connection,
                "SELECT id, name, country FROM manufacturers WHERE id IN (SELECT manufacturer_s_details FROM souvenirs WHERE price < ?)",
                new String[] {"ID", "Name", "Country"},
                "Виробників сувенірів з ціною менше ніж " + price + " не знайдено.",
                resultSet -> {
                    try {
                        return new Object[]{resultSet.getString("id"), resultSet.getString("name"), resultSet.getString("country")};
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                },
                price
                );
    }

    /**
     * Вивести інформацію про виробників заданого сувеніра, що вироблений у заданому році
     * @param connection з'єднання з бд
     * @param name назва сувеніру
     * @param year рік виробу сувеніру
     */
    public void getManufacturersByYear(Connection connection, String name, int year) {
        getDataByQuery(connection,
                "SELECT id, name, country FROM manufacturers WHERE id IN (SELECT manufacturer_s_details FROM souvenirs WHERE name = ? AND YEAR(date) = ?)",
                new String[] {"ID", "Name", "Country"},
                "Виробників сувенірів з назвою " + name + " та роком " + year + " не знайдено.",
                resultSet -> {
                    try {
                        return new Object[]{resultSet.getString("id"), resultSet.getString("name"), resultSet.getString("country")};
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                },
                name, year
        );
    }


}
