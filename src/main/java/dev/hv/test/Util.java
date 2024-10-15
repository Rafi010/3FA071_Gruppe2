package dev.hv.test;

import org.nocrala.tools.texttablefmt.Table;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class Util {

    private static Connection con = null;
    private static Properties properties = new Properties();

    private Util() {
    }

    //This method checks whether the system is a Mac or Windows computer.
    private static String backOrForward() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("mac")) {
            return "/";
        }
        else if (os.contains("win")) {
            return "\\";
        }
        return "";
    }

    private static String getDatabaseProperty(String key){
        String userName = System.getProperty("user.name");
        return properties.getProperty(userName + ".db." + key);
    }

    public static Connection getConnection(final String db) {
        if (con == null) {
            try {
                final String home = System.getProperty("user.home");
                properties.load(new FileReader(home + backOrForward() + db + ".properties"));
                String dburl = getDatabaseProperty("url");
                String dbuser = getDatabaseProperty("user");
                String dbpw = getDatabaseProperty("pw");
                System.out.println(home);

                con = DriverManager.getConnection(dburl, dbuser, dbpw);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
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
