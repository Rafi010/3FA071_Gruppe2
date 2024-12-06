package dev.Test_DatabaseConnection;

import dev.hv.projectFiles.DatabaseConnection;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Diese Klasse ist eine JUnit-Testklasse, die verschiedene Tests für die Methode `createAllTables`
 * der Klasse `DatabaseConnection` durchführt.
 */
class Test_createAllTables {

    // Eine Instanz des zu testenden Objektes.
    private DatabaseConnection databaseConnection;

    /**
     * Diese Methode wird vor jedem einzelnen Test ausgeführt und initialisiert das `databaseConnection` Objekt.
     */
    @BeforeEach
    void setUp() {
        databaseConnection = new DatabaseConnection();
    }

    /**
     * Dieser Test überprüft, ob die Methode `createAllTables` eine `IllegalStateException`
     * auslöst, wenn die Verbindung null ist.
     */
    @Test
    void testCreateAllTables_NullConnection() {
        assertThrows(IllegalStateException.class, () -> databaseConnection.createAllTables());
    }

    /**
     * Dieser Test überprüft, ob die Methode `createAllTables` ordnungsgemäß funktioniert,
     * wenn eine gültige Verbindung besteht.
     * Er führt folgende Schritte durch:
     * 1. Initialisiert eine H2 In-Memory-Datenbank.
     * 2. Stellt eine Verbindung zur Datenbank her.
     * 3. Setzt die Verbindung der `databaseConnection` Instanz mittels Reflexion.
     * 4. Ruft die Methode `createAllTables` auf, um alle Tabellen zu erstellen.
     * 5. Überprüft, ob die Tabellen `heizung`, `wasser`, `strom` und `kunde` existieren.
     */
    @Test
    void testCreateAllTables_ValidConnection() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        try (Connection connection = dataSource.getConnection()) {
            // Setze die Connection direkt mit Reflexion
            setConnection(databaseConnection, connection);
            databaseConnection.createAllTables();

            assertTrue(tableExists(connection, "heizung"));
            assertTrue(tableExists(connection, "wasser"));
            assertTrue(tableExists(connection, "strom"));
            assertTrue(tableExists(connection, "kunde"));
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            fail("Database connection failed: " + e.getMessage());
        }
    }

    /**
     * Diese Hilfsmethode verwendet Java Reflection, um die private `connection`-Variable
     * der `DatabaseConnection` Instanz zu setzen.
     */
    private void setConnection(DatabaseConnection databaseConnection, Connection connection) throws NoSuchFieldException, IllegalAccessException {
        Field connectionField = DatabaseConnection.class.getDeclaredField("connection");
        connectionField.setAccessible(true);
        connectionField.set(databaseConnection, connection);
    }

    /**
     * Diese Hilfsmethode überprüft, ob eine bestimmte Tabelle in der Datenbank existiert,
     * indem sie die Metadaten der Verbindung abfragt.
     */
    private boolean tableExists(Connection connection, String tableName) throws SQLException {
        try (var resultSet = connection.getMetaData().getTables(null, null, tableName.toUpperCase(), null)) {
            return resultSet.next();
        }
    }
}