package dev.hv.test;

public class StartTest {
   public static void main(String[] args) {
      DatabaseConnection connection = new DatabaseConnection();
      connection.openConnection(null);
      connection.createAllTables();
   }
}
