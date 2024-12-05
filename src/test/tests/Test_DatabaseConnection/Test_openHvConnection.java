package tests.Test_DatabaseConnection;

import dev.hv.projectFiles.DatabaseConnection;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Testklasse zur Überprüfung der Funktionalität der Methode openHvConnection
 * in der Klasse DatabaseConnection.
 *
 * Diese Testklasse überprüft, ob die Methode openHvConnection korrekt arbeitet,
 * indem sie eine Verbindung zur Datenbank herstellt und sicherstellt, dass keine
 * Ausnahmen während des Prozesses auftreten und dass die Verbindung nicht null ist.
 */
public class Test_openHvConnection {

    @Test
    public void testOpenHvConnection() {
        // Erstelle ein neues DatabaseConnection-Objekt
        DatabaseConnection databaseConnection = new DatabaseConnection();

        // Erstelle eine Properties-Instanz und setze manuell die nötigen Werte
        Properties properties = new Properties();
        properties.setProperty("testUser.db.url_db", "jdbc:mysql://localhost:3306/hv_db");
        properties.setProperty("testUser.db.user", "testUser");
        properties.setProperty("testUser.db.pw", "testPassword");

        // Simuliere System-Eigenschaften
        System.setProperty("user.name", "testUser");
        System.setProperty("user.home", "C:\\testHome");

        // Überprüfe, dass keine Ausnahme geworfen wird
        assertDoesNotThrow(() -> databaseConnection.openHvConnection(properties));

        // Überprüfe, ob eine Verbindung hergestellt wurde
        assertNotNull(databaseConnection.getConnection());
    }
}
