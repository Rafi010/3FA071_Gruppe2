package dev.hv.projectFiles;

import dev.hv.projectFiles.DAO.daoImplementation.CustomerDaoImpl;
import dev.hv.projectFiles.DAO.daoImplementation.ReadingDaoImpl;
import dev.hv.projectFiles.DAO.daoInterfaces.CustomerDao;
import dev.hv.projectFiles.DAO.daoInterfaces.ReadingDao;
import dev.hv.projectFiles.DAO.entities.Reading;
import dev.hv.projectFiles.DAO.entities.Customer;
import dev.hv.rest.Server;

import java.sql.SQLException;
import java.util.Properties;

public class Start {
   public static void main(String[] args) throws SQLException {
      // Properties-Objekt erstellen, das als Parameter verwendet wird
      Properties properties = new Properties();
      // Datenbankverbindung-Objekt erstellen, das für die Kommunikation mit der Datenbank genutzt wird
      DatabaseConnection connection = DatabaseConnection.getInstance();
      // Verbindung zu MySQL öffnen
      connection.openConnection(properties);
      // CustomerDao erstellen
      CustomerDao<Customer> customerDao = new CustomerDaoImpl(connection.getConnection());
      // ReadingDao erstellen
      ReadingDao<Reading> readingDao = new ReadingDaoImpl(connection.getConnection());
      // server starten
      Server.startServer("http://localhost:8080/");

   }
}