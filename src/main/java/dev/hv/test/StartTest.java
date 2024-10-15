package dev.hv.test;

public class StartTest {
   public static void main(String[] args) {
      CreateProperties createProp = new CreateProperties();
      DatabaseConnection connection = new DatabaseConnection();

      createProp.Start();
      connection.openConnection(null);
      connection.createAllTables();
   }
}
