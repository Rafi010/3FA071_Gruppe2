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
      // CreateProperties-Objekt erstellen, um die Properties-Datei zu erzeugen
      CreateProperties createProp = new CreateProperties();
      // Datenbankverbindungs-Objekt erstellen, das für die Kommunikation mit der Datenbank genutzt wird
      DatabaseConnection connection = new DatabaseConnection();

      // Properties-Datei erstellen
      createProp.Create();
      // Allgemeine Verbindung zu MySQL öffnen
      connection.openConnection(properties);
      // Datenbank "hv" erstellen
      connection.createDatabase();

      // Alte Verbindung schließen und eine neue Verbindung mit der "hv"-Datenbank öffnen
      connection.closeConnection();
      connection.openHvConnection(properties);

      // Tabellen erstellen und mit Daten füllen
      connection.createAllTables();
      // daten in DB laden
      connection.fillDatabase();

      // CustomerDao erstellen
      CustomerDao<User> customerDao = new CustomerDaoImpl(connection.getConnection());
      // ReadingDao erstellen
      ReadingDao<Reading> readingDao = new ReadingDaoImpl(connection.getConnection());

      // server starten
      Server.startServer("http://localhost:8080/");
      // Verbindung schließen
      connection.closeConnection();
   }
}
