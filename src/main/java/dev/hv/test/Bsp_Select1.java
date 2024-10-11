package dev.hv.test;


import java.sql.*;

public class Bsp_Select1 {

    public static void main(String[] args) {


        try {
            // Versucht eine Verbindung zur Datenbank herzustellen.
            final Connection con = Util.getConnection("hv");

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

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SHOW TABLES");
            final ResultSetMetaData md = rs.getMetaData();
            final int colcount = md.getColumnCount();
            while(rs.next()){
                String tabellenname = rs.getString(1);
                System.out.println(tabellenname);
                Util.printRs(rs);
                ResultSet rs2 = st.executeQuery("DESCRIBE " + tabellenname);
                final ResultSetMetaData md2 = rs.getMetaData();
                final int colcount2 = md2.getColumnCount();
                while(rs2.next()) {
                    String tabelle = rs2.getString(1);
                    System.out.println(tabelle);
                }
            }

            //int i0 = st.executeUpdate("ALTER TABLE wasser ADD column2 int");
            int i = st.executeUpdate("INSERT INTO wasser (column2) VALUES (456)");
            System.out.println(i);
            //boolean hasResults = st.execute(<SQL>);
            //ResultSet rs = st.getResultSet();

            //st.addBatch(<SQL>);
            //int[] ii = st.executeBatch();

            Util.close(st);
            // Schließt die Verbindung zur Datenbank.
            Util.close(con);
        } catch (final SQLException e) {
            // Fehlerbehandlung bei SQL-Ausnahmen.
            System.out.println("Fehler: " + e.getMessage());
        }
    }

}



