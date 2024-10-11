package dev.hv.test;

import org.nocrala.tools.texttablefmt.Table;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class Util {

    /*public static void main(String [] args){
        getConnection("hv");
    }*/


    private static Connection con = null;

    private Util() {


    }

    public static Connection getConnection(final String db) {
        if (con == null) {
            try {
                final Properties prop = new Properties();
                final String home = System.getProperty("user.home");
                prop.load(new FileReader(db + ".properties"));
                final String dburl = prop.getProperty("DBURL");
                final String dbuser = prop.getProperty("DBUSER");
                final String dbpw = prop.getProperty("DBPW");
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
