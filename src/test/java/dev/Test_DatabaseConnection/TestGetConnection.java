package dev.Test_DatabaseConnection;

import dev.hv.projectFiles.DatabaseConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Testklasse für die Überprüfung der Methode getConnection in DatabaseConnection.
 */
public class TestGetConnection {

    private DatabaseConnection databaseConnection;

    /**
     * Setzt die Testumgebung vor jedem Test auf.
     * Initialisiert die Instanz von DatabaseConnection.
     */
    @BeforeEach
    public void setUp() {
        databaseConnection = new DatabaseConnection();
    }

    /**
     * Testet, ob die getConnection-Methode eine nicht-null Verbindung zurückgibt.
     */
    @Test
    public void testGetConnectionNotNull() {
        Connection connection = databaseConnection.getConnection();

        // Überprüft, ob die Verbindung nicht null ist.
        assertNotNull(connection, "Die Verbindung sollte nicht null sein");
    }
}