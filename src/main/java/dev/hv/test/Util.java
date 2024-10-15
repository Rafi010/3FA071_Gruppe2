package dev.hv.test;

import org.nocrala.tools.texttablefmt.Table;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class Util {
    private static volatile Connection con = null;
    private static final Properties properties = new Properties();
    private static final String FORWARD_SLASH = "/";
    private static final String BACKWARD_SLASH = "\\";

    private Util() {
        // Private constructor to prevent instantiation
    }

    private static String getFileSeparator() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("mac") ? FORWARD_SLASH : os.contains("win") ? BACKWARD_SLASH : "";
    }

    private static String getDatabaseProperty(String key) {
        String userName = System.getProperty("user.name");
        return properties.getProperty(userName + ".db." + key);
    }

    private static void loadProperties(String db) throws IOException {
        String home = System.getProperty("user.home");
        properties.load(new FileReader(home + getFileSeparator() + db + ".properties"));
    }

    public static Connection getConnection(final String db) {
        if (con == null) {
            synchronized (Util.class) {
                if (con == null) {
                    try {
                        loadProperties(db);
                        String dburl = getDatabaseProperty("url");
                        String dbuser = getDatabaseProperty("user");
                        String dbpw = getDatabaseProperty("pw");
                        con = DriverManager.getConnection(dburl, dbuser, dbpw);
                    } catch (SQLException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return con;
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
                t.addCell(rsmeta.getColumnLabel(i));
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