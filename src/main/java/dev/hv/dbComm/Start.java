package dev.hv.dbComm;

import java.util.Properties;

public class Start {
   public static void main(String[] args) {
      Properties properties = new Properties();
      CreateProperties createProp = new CreateProperties();
      DatabaseConnection connection = new DatabaseConnection();

      createProp.Create();
      connection.openConnection(properties);

      connection.createDatabase();

      connection.closeConnection();
      connection.openHvConnection(properties);

      connection.createAllTables();
      connection.fillDatabase();

      connection.closeConnection();
   }
}
