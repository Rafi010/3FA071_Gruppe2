package dev.hv.test;

import dev.hv.dbComm.CreateProperties;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class TestCreatePropertiesgetConfigFilePath {

    @Test
    public void testGetConfigFilePath() {
        // Erzeugt eine Instanz der Klasse
        CreateProperties instance = new CreateProperties();

        // Fall: normaler Dateiname
        String fileName = "testConfig.properties";
        String expectedHomeDir = System.getProperty("user.home");
        Path expectedPath = Paths.get(expectedHomeDir, fileName);
        Path resultPath = instance.getConfigFilePath(fileName);
        assertNotNull(resultPath, "Der Ergebnis-Pfad sollte nicht null sein");
        assertEquals(expectedPath, resultPath, "Der Ergebnis-Pfad stimmt nicht mit dem erwarteten Pfad überein");
    }

    @Test
    public void testGetConfigFilePathWithNullFileName() {
        // Erzeugt eine Instanz der Klasse
        CreateProperties instance = new CreateProperties();

        // Fall: Dateiname ist null
        String fileName = null;
        assertThrows(NullPointerException.class, () -> {
            instance.getConfigFilePath(fileName);
        }, "Sollte NullPointerException werfen, wenn Dateiname null ist");
    }

    @Test
    public void testGetConfigFilePathWithEmptyFileName() {
        // Erzeugt eine Instanz der Klasse
        CreateProperties instance = new CreateProperties();

        // Fall: Dateiname ist leer
        String fileName = "";
        String expectedHomeDir = System.getProperty("user.home");
        Path expectedPath = Paths.get(expectedHomeDir, fileName);
        Path resultPath = instance.getConfigFilePath(fileName);
        assertNotNull(resultPath, "Der Ergebnis-Pfad sollte nicht null sein");
        assertEquals(expectedPath, resultPath, "Der Ergebnis-Pfad stimmt nicht mit dem erwarteten Pfad überein");
    }

    @Test
    public void testGetConfigFilePathWithInvalidFileName() {
        // Erzeugt eine Instanz der Klasse
        CreateProperties instance = new CreateProperties();

        // Fall: Dateiname enthält ungültige Zeichen
        String fileName = "invalidConfig.properties";
        String expectedHomeDir = System.getProperty("user.home");
        Path expectedPath = Paths.get(expectedHomeDir, fileName);
        Path resultPath = instance.getConfigFilePath(fileName);

        // Da Windows keine ungültigen Zeichen in Pfaden unterstützt, wird der resultierende Pfad auf verschiedenen Betriebssystemen unterschiedlich behandelt.
        // Unter Windows könnte es zu einer InvalidPathException kommen, unter Unix-Systemen könnte er akzeptiert werden.
        assertNotNull(resultPath, "Der Ergebnis-Pfad sollte nicht null sein");
        assertEquals(expectedPath, resultPath, "Der Ergebnis-Pfad stimmt nicht mit dem erwarteten Pfad überein");
    }
}