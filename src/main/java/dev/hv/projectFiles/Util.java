package dev.hv.projectFiles;

import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.*;

public class Util {
    // Privater Konstruktor, um Instanziierung zu verhindern
    private Util() {
    }

    /**
     * Passt den angegebenen Dateipfad an das Betriebssystem an.
     * Ersetzt Backslashes durch das systemabhängige Trennzeichen.
     *
     * @param path Der ursprüngliche Dateipfad (z. B. "C:\\Users\\file.txt").
     * @return Der angepasste Dateipfad, basierend auf dem aktuellen Betriebssystem.
     */
    public static String getRightSystemPath(String path) {
        return path.replace("\\", File.separator);
    }

    /**
     * Führt ein SQL-Skript aus einer Datei aus.
     *
     * @param con      Die aktive SQL-Verbindung, über die das Skript ausgeführt wird.
     * @param filePath Der Pfad zur SQL-Datei, die das auszuführende Skript enthält.
     */
    public static void executeSQL(Connection con, String filePath) {
        try {
            // Initialisiert den ScriptRunner mit der übergebenen Verbindung
            ScriptRunner sr = new ScriptRunner(con);

            // Liest das SQL-Skript aus der Datei
            Reader reader = new BufferedReader(new FileReader(filePath));

            // Führt das Skript aus
            sr.runScript(reader);
        } catch (IOException e) {
            // Wirft eine RuntimeException, falls ein Fehler beim Lesen der Datei auftritt
            throw new RuntimeException(e);
        }
    }

}
