package dev.hv.projectFiles;

import dev.hv.model.ICustomer;
import dev.hv.model.IReading;
import dev.hv.projectFiles.DAO.daoImplementation.CustomerDaoImpl;
import dev.hv.projectFiles.DAO.daoImplementation.ReadingDaoImpl;
import dev.hv.projectFiles.DAO.daoInterfaces.CustomerDao;
import dev.hv.projectFiles.DAO.daoInterfaces.ReadingDao;
import dev.hv.projectFiles.DAO.entities.Reading;
import dev.hv.projectFiles.DAO.entities.User;
import dev.hv.rest.Server;

import java.time.LocalDate;
import java.util.Properties;
import java.util.UUID;

public class Start {
   public static void main(String[] args) {
      String port = System.getenv("PORT");
      if (port == null) {
         port = "8080";  // default port for local development
      }
      String baseUri = "http://0.0.0.0:" + port + "/";
      //create createProp object to create the properties file
      CreateProperties createProp = new CreateProperties();
      //create connection object used for database communication
      DatabaseConnection connection = new DatabaseConnection();

      //create the properties file
      createProp.Create();
      //open the general connection to mySql
      connection.openConnection();
      //create the hv database
      connection.createDatabase();
      //close the old connection and open the new one with the hv database
      connection.closeConnection();
      connection.openHvConnection(properties);
      //create the tables and fill them
      connection.createAllTables();
      connection.fillDatabase();

      //create the customer dao
      CustomerDao customerDao = new CustomerDaoImpl(connection.getConnection());
      //create the reading dao
      ReadingDao readingDao = new ReadingDaoImpl(connection.getConnection());

      Server.startServer("http://localhost:8080/");
      //close the connection
   }
}
