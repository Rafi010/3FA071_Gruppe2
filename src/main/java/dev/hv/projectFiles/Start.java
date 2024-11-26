package dev.hv.projectFiles;

import dev.hv.model.ICustomer;
import dev.hv.model.IReading;
import dev.hv.projectFiles.DAO.daoImplementation.CustomerDaoImpl;
import dev.hv.projectFiles.DAO.daoImplementation.ReadingDaoImpl;
import dev.hv.projectFiles.DAO.daoInterfaces.CustomerDao;
import dev.hv.projectFiles.DAO.daoInterfaces.ReadingDao;
import dev.hv.projectFiles.DAO.entities.Reading;
import dev.hv.projectFiles.DAO.entities.User;

import java.time.LocalDate;
import java.util.Properties;
import java.util.UUID;

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

      //create the customer dao
      CustomerDao customerDao = new CustomerDaoImpl(connection.getConnection());
      //create the reading dao
      ReadingDao readingDao = new ReadingDaoImpl(connection.getConnection());

      //close the connection
      connection.closeConnection();
   }
}
