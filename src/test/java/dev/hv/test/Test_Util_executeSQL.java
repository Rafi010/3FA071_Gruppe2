package dev.hv.test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class Test_Util_executeSQL {

    private static Connection connection;

    @BeforeAll
    static void setUp() throws SQLException {
        // Verbindung zu einer In-Memory-Datenbank herstellen
        connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/?allowMultiQueries=true");
    }

    @Test
    void testExecuteSQL() {
        // Dateipfad für den Test festlegen
        String filePath = "src\\test\\test_sql_script.sql";

        // Eine einfache SQL-Datei erstellen
        createTestSQLFile(filePath, "CREATE TABLE Test (id INT PRIMARY KEY, name VARCHAR(255));");

        // Methode ausführen
        assertDoesNotThrow(() -> Util.executeSQL(connection, filePath));

        // Überprüfen, ob die Tabelle wirklich erstellt wurde
        assertTrue(doesTableExist("Test"));
    }

    @Test
    void testExecuteSQLWithIOException() {
        // Datei, die eine IOException auslösen wird
        String filePath = "invalid_path.sql";

        // Überprüfung, ob eine RuntimeException bei IOException geworfen wird
        assertThrows(RuntimeException.class, () -> Util.executeSQL(connection, filePath));
    }

    /**
     * Hilfsmethode zur Erstellung einer einfachen SQL-Datei
     */
    private void createTestSQLFile(String filePath, String content) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hilfsmethode zur Überprüfung, ob eine Tabelle in der Datenbank existiert
     */
    private boolean doesTableExist(String tableName) {
        try (var resultSet = connection.getMetaData().getTables(null, null, tableName, null)) {
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}