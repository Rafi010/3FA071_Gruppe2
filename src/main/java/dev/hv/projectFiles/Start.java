package dev.hv.projectFiles;

import dev.hv.projectFiles.DAO.daoImplementation.CustomerDaoImpl;
import dev.hv.projectFiles.DAO.daoImplementation.ReadingDaoImpl;
import dev.hv.projectFiles.DAO.daoInterfaces.CustomerDao;
import dev.hv.projectFiles.DAO.daoInterfaces.ReadingDao;
import dev.hv.projectFiles.DAO.entities.Reading;
import dev.hv.projectFiles.DAO.entities.User;
import dev.hv.rest.Server;

import java.util.Properties;

public class Start {
   public static void main(String[] args) {
      // Properties-Objekt erstellen, das als Parameter verwendet wird
      Properties properties = new Properties();
      // Datenbankverbindung-Objekt erstellen, das für die Kommunikation mit der Datenbank genutzt wird
      DatabaseConnection connection = DatabaseConnection.getInstance();

      // Verbindung zu MySQL öffnen
      connection.openConnection(properties);
      // Tabellen erstellen und mit Daten füllen
      connection.createAllTables();
      // daten in DB laden
      connection.fillDatabase();

      // CustomerDao erstellen
      CustomerDao<User> customerDao = new CustomerDaoImpl(connection.getConnection());
      // ReadingDao erstellen
      ReadingDao<Reading> readingDao = new ReadingDaoImpl(connection.getConnection());
      // Verbindung schließen
      connection.closeConnection();
      // server starten
      Server.startServer("http://localhost:8080/");

   }
}