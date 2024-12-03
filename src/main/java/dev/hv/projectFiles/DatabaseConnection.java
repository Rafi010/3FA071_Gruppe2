package dev.hv.projectFiles;

import dev.hv.model.IDatebaseConnection;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DatabaseConnection implements IDatebaseConnection {
    // Die Verbindung wird lokal gespeichert, sodass alle DB-Interaktionen nur innerhalb dieser Klasse stattfinden können
    private Connection connection;

    // Öffnet eine Verbindung zu MySQL (verwendet die URL in der Properties-Datei, die nicht mit der hv-Datenbank verbindet)
    @Override
    public IDatebaseConnection openConnection(Properties properties) {
        final String userName = System.getProperty("user.name");
        final String home = System.getProperty("user.home");
        try {
            // Lädt die Schlüssel-Wert-Paare in das Properties-Objekt
            properties.load(new FileReader(Util.getRightSystemPath(home + "\\hv.properties")));
            // Holt die benötigten Werte aus der Properties-Datei
            final String dburl = properties.getProperty(userName + ".db.url");
            final String dbuser = properties.getProperty(userName + ".db.user");
            final String dbpw = properties.getProperty(userName + ".db.pw");
            // Verwendet die Werte, um die Verbindung zu erstellen und speichert sie
            this.connection = DriverManager.getConnection(dburl, dbuser, dbpw);
            System.out.println("Mit MySQL verbunden");


        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    // Öffnet eine Verbindung zu MySQL (verwendet die URL in der Properties-Datei, die mit der hv-Datenbank verbindet)
    public void openHvConnection(Properties properties) {
        String userName = System.getProperty("user.name");
        final String home = System.getProperty("user.home");
        try {
            // Lädt die Schlüssel-Wert-Paare in das Properties-Objekt
            properties.load(new FileReader(Util.getRightSystemPath(home + "\\hv.properties")));
            // Holt die benötigten Werte aus der Properties-Datei
            String dburl = properties.getProperty(userName + ".db.url_db");
            String dbuser = properties.getProperty(userName + ".db.user");
            String dbpw = properties.getProperty(userName + ".db.pw");
            // Verwendet die Werte, um die Verbindung zu erstellen und speichert sie
            this.connection = DriverManager.getConnection(dburl, dbuser, dbpw);
            System.out.println("Mit der Datenbank verbunden: hv");
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createDatabase() {
        if (connection == null) {
            throw new IllegalStateException("Keine offene Datenbankverbindung");
        }
        Util.executeSQL(connection, "dateien/sql/create_db_hv.sql");
    }

    @Override
    public void createAllTables() {
        if (connection == null) {
            throw new IllegalStateException("Keine offene Datenbankverbindung");
        }
        Util.executeSQL(connection, "dateien/sql/create_table.sql");
    }

    @Override
    public void truncateAllTables() {
        if (connection == null) {
            throw new IllegalStateException("Keine offene Datenbankverbindung");
        }
        Util.executeSQL(connection, "dateien/sql/truncate_table.sql");
    }

    @Override
    public void removeAllTables() {
        if (connection == null) {
            throw new IllegalStateException("Keine offene Datenbankverbindung");
        }
        Util.executeSQL(connection, "dateien/sql/remove_table.sql");
    }

    @Override
    public void closeConnection() {
        try {
            connection.close();
            System.out.println("Verbindung geschlossen");
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void fillDatabase() {
        if (connection == null) {
            throw new IllegalStateException("Keine offene Datenbankverbindung");
        }
        Util.executeSQL(connection, "dateien/sql/load_csv_file_in_table.sql");
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
