package dev.hv.dbComm;

import java.util.Properties;

public class Start {
   public static void main(String[] args) {
      //create properties object for use as parameter
      Properties properties = new Properties();
      //create createProp object to create the properties file
      CreateProperties createProp = new CreateProperties();
      //create connection object used for database communication
      DatabaseConnection connection = new DatabaseConnection();

      //create the properties file
      createProp.Create();
      //open the general connection to mySql
      connection.openConnection(properties);
      //create the hv database
      connection.createDatabase();
      //close the old connection and open the new one with the hv database
      connection.closeConnection();
      connection.openHvConnection(properties);
      //create the tables and fill them
      connection.createAllTables();
      connection.fillDatabase();
      //close the connection
      connection.closeConnection();
   }
}
