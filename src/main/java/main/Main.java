package main;

import  database.Database;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

          Database db = new Database();
          try {
                db.connect();
          } catch (SQLException e) {
              System.out.println("Не вдалось підключитись до бази даних! Помилка: "+ e.getMessage());
          }
    }
}