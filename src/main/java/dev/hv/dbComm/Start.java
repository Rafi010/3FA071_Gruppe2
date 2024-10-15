package dev.hv.dbComm;

public class Start {
   public static void main(String[] args) {
      CreateProperties createProp = new CreateProperties();
      DatabaseConnection connection = new DatabaseConnection();

      createProp.Start();
      connection.openConnection(null);
      connection.createAllTables();
   }
}
