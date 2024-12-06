package dev.Test_DatabaseConnection;

import dev.TestUtils;
import dev.hv.projectFiles.DatabaseConnection;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Diese Klasse testet die ordnungsgemäße Schließung einer Datenbankverbindung.
 */
class TestCloseConnection {

    private static DatabaseConnection databaseConnection;

    /**
     * Diese Methode wird vor jedem Test ausgeführt und stellt eine Verbindung zur Datenbank her.
     * @throws SQLException falls ein Fehler bei der Herstellung der Verbindung auftritt.
     */
    @BeforeAll
    static void setUp() throws SQLException {
        // Initialisiert die Datenquelle und DatabaseConnection vor jedem Test
        Connection connection = TestUtils.getTestDbConnection();
        databaseConnection = new DatabaseConnection();
        databaseConnection.setConnection(connection);
    }

    /**
     * Dieser Test überprüft, ob die closeConnection Methode ordnungsgemäß funktioniert.
     */
    @Test
    void testCloseConnection() {
        // Ensure the connection is not null and is open before closing
        assertNotNull(databaseConnection.getConnection(), "Connection should not be null before close");
        try {
            assertFalse(databaseConnection.getConnection().isClosed());
        } catch (SQLException e) {
            fail("SQLException occurred while checking if connection is closed: " + e.getMessage());
        }

        // Close the connection
        databaseConnection.closeConnection();

        // Verify that the original connection is closed
        try {
            assertTrue(databaseConnection.getConnection().isClosed());
        } catch (SQLException e) {
            fail("SQLException occurred while checking if connection is closed: " + e.getMessage());
        }
    }
}

