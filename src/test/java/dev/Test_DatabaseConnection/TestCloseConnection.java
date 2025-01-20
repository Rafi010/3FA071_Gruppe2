package dev.Test_DatabaseConnection;

import dev.BaseTest;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Diese Klasse testet die ordnungsgemäße Schließung einer Datenbankverbindung.
 */
class TestCloseConnection extends BaseTest {

    /**
     * Dieser Test überprüft, ob die closeConnection Methode ordnungsgemäß funktioniert.
     */
    @Test
    void testCloseConnection() {
        // Ensure the connection is not null and is open before closing
        assertNotNull(connection.getConnection(), "Connection should not be null before close");
        try {
            assertFalse(connection.getConnection().isClosed());
        } catch (SQLException e) {
            fail("SQLException occurred while checking if connection is closed: " + e.getMessage());
        }

        // Close the connection
        connection.closeConnection();

        // Verify that the original connection is closed
        try {
            assertTrue(connection.getConnection().isClosed());
        } catch (SQLException e) {
            fail("SQLException occurred while checking if connection is closed: " + e.getMessage());
        }
    }
}

