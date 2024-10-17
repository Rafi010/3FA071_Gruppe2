package dev.hv.dbComm;

import java.util.Properties;

public class Start {
   public static void main(String[] args) {
      Properties properties = new Properties();
      CreateProperties createProp = new CreateProperties();
      DatabaseConnection connection = new DatabaseConnection();

      createProp.Start();
      connection.openConnection(properties);
      connection.createAllTables();
      connection.fillDatabase();
   }
}
