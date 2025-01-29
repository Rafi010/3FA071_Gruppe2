package dev.hv.projectFiles;

import dev.hv.model.IDatebaseConnection;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Diese Klasse bietet Methoden zum Öffnen, Schließen und Getten der Connection.
 * Außerdem noch zum Erstellen, Löschen und Füllen der Tabellen in der DB.
 */
public class DatabaseConnection implements IDatebaseConnection {
    // Die Verbindung wird lokal gespeichert, sodass alle DB-Interaktionen nur innerhalb dieser Klasse stattfinden können
    // singleton
    private static Connection connection = null;
    private static DatabaseConnection instance;

    /**
     * Konstruktor für CustomerDaoImpl.
     */
    public static synchronized DatabaseConnection getInstance() {
            if (instance == null) {
                instance = new DatabaseConnection();
            }
        return instance;
    }


    /**
     * Öffnet eine Verbindung zu MySQL
     *
     * @param properties die URL aus der Properties-Datei welche mit der DB verbindet
     */
    @Override
    public IDatebaseConnection openConnection(Properties properties) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return this;
        }

        final String userName = System.getProperty("user.name");
        final String home = System.getProperty("user.home");
        try {
            // Lädt die Schlüssel-Wert-Paare in das Properties-Objekt
            properties.load(new FileReader(Util.getRightSystemPath(home + "\\hv.properties")));
            // Holt die benötigten Werte aus der Properties-Datei
            final String dburl = properties.getProperty(userName + ".db.url");
            final String dbuser = properties.getProperty(userName + ".db.user");
            final String dbpw = properties.getProperty(userName + ".db.pw");
            connection = DriverManager.getConnection(dburl, dbuser, dbpw);

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Mit MySQL verbunden");
        return this;
    }

    /**
     * Erstellt alle vier Tabellen in der DB wenn noch nicht existent.
     */
    @Override
    public void createAllTables() {
        if (connection == null) {
            throw new IllegalStateException("Keine offene Datenbankverbindung");
        }
        Util.executeSQL(connection, "src/main/resources/sql/create_table.sql");
    }

    /**
     * Löscht alle daten aus den vier Tabellen.
     */
    @Override
    public void truncateAllTables() {
        if (connection == null) {
            throw new IllegalStateException("Keine offene Datenbankverbindung");
        }
        Util.executeSQL(connection, "src/main/resources/sql/truncate_table.sql");
    }

    /**
     * Löscht alle vier Tabellen.
     */
    @Override
    public void removeAllTables() {
        if (connection == null) {
            throw new IllegalStateException("Keine offene Datenbankverbindung");
        }
        Util.executeSQL(connection, "src/main/resources/sql/remove_table.sql");
    }

    /**
     * Schließt die Verbindung zur DB.
     */
    @Override
    public void closeConnection() {
        try {
            connection.close();
            System.out.println("Verbindung geschlossen");
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Füllt alle vier Tabellen mit Daten.
     * Es sollten alle Tabellen bereits vorhanden sein.
     *
     * @throws IllegalStateException falls noch keine DB Verbindung existiert.
     */
    public void fillDatabase() {
        if (connection == null) {
            throw new IllegalStateException("Keine offene Datenbankverbindung");
        }
        Util.executeSQL(connection, "src/main/resources/sql/load_csv_file_in_table.sql");
    }

    /**
     * gibt die datenbank verbindung zurück
     *
     * @return db connection
     */
    public Connection getConnection() {
            return connection;
    }
}
