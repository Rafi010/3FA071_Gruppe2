package dev.hv.test;

import dev.hv.dbComm.CreateProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testklasse zur Überprüfung der Erstellung und des Inhalts einer Konfigurationsdatei im Properties-Format.
 * Verwendet JUnit für die Einrichtung der Testumgebung und die Ausführung der Testfälle.
 */
public class TestCreateProperties {

    private Path configFilePath;

    /**
     * Einrichtungsmethode, die vor jedem Testfall ausgeführt wird.
     * Definiert den Pfad zur Konfigurationsdatei.
     */
    @BeforeEach
    public void setUp() {
        String resourcesDir = "src/test/resources";
        configFilePath = Paths.get(resourcesDir, "test_hv.properties");
    }

    /**
     * Testet, ob die Start-Methode der CreateProperties-Klasse die Konfigurationsdatei erfolgreich erstellt.
     */
    @Test
    public void testStartCreatesConfigFile() throws IOException {
        String userName = System.getProperty("user.name");

        // Setzt den Pfad zur Konfigurationsdatei in der CreateProperties-Klasse, falls erforderlich
        // Diese Zeile hängt von Ihrer Implementierung der CreateProperties-Klasse ab
        // new CreateProperties().setConfigPath(configFilePath.toString());

        // Aufruf der Start-Methode
        new CreateProperties().Create();

        // Überprüft, ob die Konfigurationsdatei erstellt wurde bzw. bereits existiert
        assertTrue(Files.exists(configFilePath), "Config file should be created or already exist");

        // Lädt die Eigenschaften aus der Datei
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(configFilePath.toFile())) {
            properties.load(input);
        }

        // Überprüft die geladenen Eigenschaften
        assertEquals("jdbc:mariadb://localhost:3306/hv?allowMultiQueries=true", properties.getProperty(userName + ".db.url"));
        assertEquals("root", properties.getProperty(userName + ".db.user"));
        assertEquals("", properties.getProperty(userName + ".db.pw"));
    }
}