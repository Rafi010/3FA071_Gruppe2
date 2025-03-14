package dev.Test_DatabaseConnection;

import dev.BaseTest;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Testklasse für die Überprüfung der Methode getConnection in DatabaseConnection.
 */
public class TestGetConnection extends BaseTest {


    @Test
    public void testGetConnectionNotNull() {
        Connection conn = connection.getConnection();

        // Überprüft, ob die Verbindung nicht null ist.
        assertNotNull(conn, "Die Verbindung sollte nicht null sein");
    }
}