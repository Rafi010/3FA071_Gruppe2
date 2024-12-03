package dev.hv.projectFiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Klasse, die für das Erstellen einer Properties-Datei für die Datenbankkonfiguration verantwortlich ist.
 * Diese Properties-Datei gibt die URL, den Benutzer und das Passwort für die Datenbank an.
 */

public class CreateProperties {

    private static final Logger log = LoggerFactory.getLogger(CreateProperties.class);

    public void Create(){

        // Erhalte den Dateipfad der Properties-Datei
        String homeDir = System.getProperty("user.home");
        Path configFilePath = Paths.get(homeDir, "hv.properties");
        // Hole den Benutzernamen des PCs
        String userName = System.getProperty("user.name");

        // Erstelle die Schlüssel, unter denen die Informationen in der Datei gespeichert werden
        final String URL = userName + ".db.url";
        final String URL_DB = userName + ".db.url_db";
        final String USER = userName + ".db.user";
        final String PW = userName + ".db.pw";

        // Kommentar zu Beginn der Datei
        String DatabaseComment = "Datenbankkonfiguration für " + userName;

        // Überprüfen, ob die Konfigurationsdatei bereits existiert
        if (Files.exists(configFilePath)) {
            System.out.println("Konfigurationsdatei existiert bereits unter " + configFilePath);
        } else {
            // Erstelle das Properties-Objekt
            Properties config = new Properties();

            // Füge die Werte den Schlüsseln in der Datei hinzu
            config.setProperty(URL, "jdbc:mariadb://localhost:3306/?allowMultiQueries=true");
            config.setProperty(URL_DB, "jdbc:mariadb://localhost:3306/hv?allowMultiQueries=true");
            config.setProperty(USER, "root");
            config.setProperty(PW, "");

            // Speichere die Properties in der Datei
            try (FileOutputStream output = new FileOutputStream(configFilePath.toFile())) {
                config.store(output, DatabaseComment);
                System.out.println("Konfigurationsdatei erstellt unter " + configFilePath);
            } catch (IOException e) {
                log.error("Fehler: ", e);
            }
        }
    }
}
