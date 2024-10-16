import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ListTables {

    public static void main(String[] args) {
        String url = "jdbc:mariadb://localhost:3306/hv?allowMultiQueries=true";  // Datenbank URL
        String user = "root";  // Datenbank Benutzername
        String password = "test123";  // Datenbank Passwort

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            listTables(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void listTables(Connection conn) {
        try {
            // Hol dir die Meta-Daten der Datenbank
            DatabaseMetaData metaData = conn.getMetaData();

            // Optional: Gib das Schema an, in dem deine Tabellen liegen (z.B. "my_schema").
            String schemaPattern = "dein_schema"; // Falls notwendig, ersetze mit deinem Schema
            String[] types = {"TABLE"};

            ResultSet rs = metaData.getTables(null, schemaPattern, "%", types);

            System.out.println("Eigene Tabellen in der Datenbank:");
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");

                // Filtere bekannte Systemtabellen aus
                if (!tableName.startsWith("mysql") && !tableName.startsWith("sys") && !tableName.startsWith("performance_schema")) {
                    System.out.println(tableName);
                }
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}