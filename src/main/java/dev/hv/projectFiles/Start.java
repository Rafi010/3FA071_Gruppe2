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
      //create connection object used for database communication
      DatabaseConnection connection = new DatabaseConnection();

      connection.openConnection();
      //create the tables and fill them
      connection.removeAllTables();
      connection.createAllTables();
      connection.fillDatabase();

      //create the customer dao
      CustomerDao customerDao = new CustomerDaoImpl(connection.getConnection());
      //create the reading dao
      ReadingDao readingDao = new ReadingDaoImpl(connection.getConnection());

      User user1 = new User();
      user1.setId(UUID.fromString("e00b1287-40a9-452a-8277-018e1682f9e0"));
      user1.setFirstName("Rafi");
      user1.setLastName("Rauch");
      user1.setGender(ICustomer.Gender.M);
      user1.setBirthDate(LocalDate.of(2006, 5, 2));

      customerDao.addUser(user1);

      Server.startServer(baseUri);
      //close the connection
   }
}
