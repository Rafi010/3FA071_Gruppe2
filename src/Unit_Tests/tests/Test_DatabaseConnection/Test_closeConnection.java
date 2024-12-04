package tests.Test_DatabaseConnection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Diese Klasse testet die ordnungsgemäße Schließung einer Datenbankverbindung.
 */
class TestCloseConnection {

    private Connection connection;

    /**
     * Diese Methode wird vor jedem Test ausgeführt und stellt eine Verbindung zur Datenbank her.
     * @throws SQLException falls ein Fehler bei der Herstellung der Verbindung auftritt.
     */
    @BeforeEach
    void setUp() throws SQLException {
        // Verbindung zur In-Memory H2-Datenbank herstellen
        connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
    }

    /**
     * Diese Methode wird nach jedem Test ausgeführt und schließt die Datenbankverbindung.
     * @throws SQLException falls ein Fehler beim Schließen der Verbindung auftritt.
     */
    @AfterEach
    void tearDown() throws SQLException {
        // Wenn die Verbindung nicht null und noch offen ist, schließe sie
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * Dieser Test überprüft, ob die closeConnection Methode ordnungsgemäß funktioniert.
     */
    @Test
    void testCloseConnection() {
        // Erstellt eine Instanz von DatabaseConnection mit der aktuellen Verbindung
        DbConnector dbConnection = new DbConnector(connection);
        // Überprüft, dass die Methode closeConnection keine Ausnahme wirft
        assertDoesNotThrow(dbConnection::closeConnection);
        // Überprüft, ob die Verbindung geschlossen ist
        assertTrue(isConnectionClosed(connection));
    }

    /**
     * Diese Methode prüft, ob die gegebene Verbindung geschlossen ist.
     * @param connection die zu prüfende Verbindung.
     * @return true, wenn die Verbindung geschlossen ist, false andernfalls.
     */
    private boolean isConnectionClosed(Connection connection) {
        try {
            return connection == null || connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

}

/**
 * Diese Klasse stellt eine Verbindung zu einer Datenbank dar.
 * Sie bietet Methoden zum Schließen der Datenbankverbindung.
 */
class DbConnector {

    private final Connection connection;

    /**
     * Konstruktor, der die Datenbankverbindung initialisiert.
     * @param connection die zu verwendende Verbindung.
     */
    public DbConnector(Connection connection) {
        this.connection = connection;
    }

    /**
     * Diese Methode schließt die Datenbankverbindung.
     */
    public void closeConnection() {
        try {
            // Schließt die Verbindung und gibt eine Meldung aus
            connection.close();
            System.out.println("Connection closed");
        } catch (final Exception e) {
            // Wenn eine Ausnahme auftritt, wird sie als Laufzeitausnahme geworfen
            throw new RuntimeException(e);
        }
    }
}