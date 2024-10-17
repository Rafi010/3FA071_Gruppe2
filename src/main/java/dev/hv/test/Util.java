package dev.hv.test;

import org.nocrala.tools.texttablefmt.Table;

import java.sql.*;

public class Util {
    private Util() {
    }

    public static String backOrForward() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("mac")) {
            return "/";
        }
        else if (os.contains("win")) {
            return "\\";
        }
        return "";
    }

    public static void close(final AutoCloseable obj) {
        if (obj != null) {
            try {
                obj.close();
            } catch (final Exception e) {
                //ignore
            }
        }

    }

    public static void printRs(final ResultSet rs) {
        try {
            final ResultSetMetaData rsmeta = rs.getMetaData();
            final int cols = rsmeta.getColumnCount();
            final Table t = new Table(cols);

            for (int i = 1; i <= cols; i++){
                final String label = rsmeta.getColumnLabel(i);
                t.addCell(label);
            }
            while(rs.next()){
                for (int i = 1; i <= cols; i++){
                    final Object obj = rs.getObject(i);
                    t.addCell(obj == null ? "" : obj.toString());
                }
            }
            System.out.println(t.render());
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
