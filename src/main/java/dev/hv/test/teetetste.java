package dev.hv.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class teetetste {


    public static void main(String[] args) {


        try {
            // Versucht eine Verbindung zur Datenbank herzustellen.
            final Connection con = DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/hv?allowMultiQueries=true", // URL der Datenbank
                    "root", // Benutzername
                    "Ag2x~pdA@xQi4DCs2vXThq4N3aSMivZ~"      // Passwort (leer)
            );

            // Bestätigt die erfolgreiche Verbindung.
            System.out.println("... connected");

            // Ruft Metadaten der Datenbank ab.
            final DatabaseMetaData meta = con.getMetaData();

            // Ausgabe des verwendeten Datenbanktreibers und dessen Version.
            System.out.format("Driver : %s %s.%s\n",
                    meta.getDriverName(),
                    meta.getDriverMajorVersion(),
                    meta.getDriverMinorVersion()
            );

            // Ausgabe der Datenbankinformationen wie Produktname und Version.
            System.out.format("DB : %s %s.%s (%s)\n",
                    meta.getDatabaseProductName(),
                    meta.getDatabaseMajorVersion(),
                    meta.getDatabaseMinorVersion(),
                    meta.getDatabaseProductVersion()
            );

            // Schließt die Verbindung zur Datenbank.
            con.close();
        } catch (final SQLException e) {
            // Fehlerbehandlung bei SQL-Ausnahmen.
            System.out.println("Fehler: " + e.getMessage());
        }
    }

}
