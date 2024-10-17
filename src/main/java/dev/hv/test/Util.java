package dev.hv.test;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.nocrala.tools.texttablefmt.Table;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.*;
import java.util.Properties;

public class Util {
    private Util() {
    }
    //This method checks whether the system is a Mac or Windows computer.
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

    public static void executeSQL(Connection con, String filePath){
        try {

            ScriptRunner sr = new ScriptRunner(con);
            Reader reader = new BufferedReader(new FileReader(filePath));
            sr.runScript(reader);

        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
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
