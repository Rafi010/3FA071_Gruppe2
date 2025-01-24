package dev.hv.projectFiles;

import dev.hv.model.IDatebaseConnection;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection implements IDatebaseConnection {
    // Die Verbindung wird lokal gespeichert, sodass alle DB-Interaktionen nur innerhalb dieser Klasse stattfinden können
    // singleton
    private static Connection connection = null;
    private static DatabaseConnection instance;


    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public static synchronized void resetInstance() {
        instance = null;
    }

    // Öffnet eine Verbindung zu MySQL (verwendet die URL in der Properties-Datei, die nicht mit der hv-Datenbank verbindet)
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

    @Override
    public void createAllTables() {
        if (connection == null) {
            throw new IllegalStateException("Keine offene Datenbankverbindung");
        }
        Util.executeSQL(connection, "src/main/resources/sql/create_table.sql");
    }

    @Override
    public void truncateAllTables() {
        if (connection == null) {
            throw new IllegalStateException("Keine offene Datenbankverbindung");
        }
        Util.executeSQL(connection, "src/main/resources/sql/truncate_table.sql");
    }

    @Override
    public void removeAllTables() {
        if (connection == null) {
            throw new IllegalStateException("Keine offene Datenbankverbindung");
        }
        Util.executeSQL(connection, "src/main/resources/sql/remove_table.sql");
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
        Util.executeSQL(connection, "src/main/resources/sql/load_csv_file_in_table.sql");
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                openConnection(new Properties());
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception at egtConnection: " + e);
        }
    }

    public void setConnection (Connection connection){
        DatabaseConnection.connection = connection;
    }
}
