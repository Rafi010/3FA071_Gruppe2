package dev.hv.test;

public class StartTest {

   public static void main(String[] args) {
      StartTest startTest = new StartTest();
      startTest.initializeDatabase(new CreateProperties(), new DatabaseConnection());
   }

   private void initializeDatabase(CreateProperties createProperties, DatabaseConnection connection) {
      createProperties.Start();
      connection.openConnection(null);
      connection.createAllTables();
   }
}