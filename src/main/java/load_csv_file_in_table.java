import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class load_csv_file_in_table {

    public static void main(String[] args) {

        String url = "jdbc:mariadb://localhost:3306/hv?allowMultiQueries=true";  // Datenbank URL
        String user = "root";  // Datenbank Benutzername
        String password = "test123";  // Datenbank Passwort
        String filePath = "dateien/sql/load_csv_file_in_table.sql";  // Pfad zur SQL-Datei


        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // SQL aus der Datei lesen
            String sql = readSQLFromFile(filePath);

            if (sql != null) {
                // SQL ausführen
                executeSQL(conn, sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Methode zum Lesen des SQL-Befehls aus einer Datei
    private static String readSQLFromFile(String filePath) {
        StringBuilder sql = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                sql.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return sql.toString();
    }

    // Methode zum Ausführen des SQL-Befehls
    private static void executeSQL(Connection conn, String sql) {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
            System.out.println("SQL-Befehl erfolgreich ausgeführt.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
