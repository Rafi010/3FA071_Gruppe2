import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class SQLFileExecutor {

    public static void main(String[] args) {
        String url = "jdbc:mariadb://localhost:3306/hv?allowMultiQueries=true";  // Datenbank URL
        String user = "root";  // Datenbank Benutzername
        String password = "test123";  // Datenbank Passwort
        String sqlFilePath = "dateien/sql/create_table.sql";  // Pfad zur SQL-Datei

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             BufferedReader br = new BufferedReader(new FileReader(sqlFilePath))) {

            StringBuilder sql = new StringBuilder();
            String line;

            // Datei zeilenweise lesen und SQL-Anweisungen aufbauen
            while ((line = br.readLine()) != null) {
                sql.append(line).append("\n"); // jede Zeile zur SQL-Anweisung hinzufügen

                // Wenn Semikolon gefunden, dann SQL-Anweisung ausführen
                if (line.trim().endsWith(";")) {
                    stmt.execute(sql.toString()); // SQL-Anweisung ausführen
                    sql.setLength(0); // StringBuilder zurücksetzen
                }
            }

            System.out.println("SQL-Befehle erfolgreich ausgeführt.");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
