package dev.hv.test;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {

    /*public static void main(String [] args){
        getConnection("hv");
    }*/


    private static Connection con = null;

    private Util(){


    }

    public static Connection getConnection(final String db){
        if (con == null){
            try{
                final Properties prop = new Properties();
                final String home = System.getProperty("user.home");
                prop.load(new FileReader(db + ".properties"));
                final String dburl = prop.getProperty("DBURL");
                final String dbuser = prop.getProperty("DBUSER");
                final String dbpw = prop.getProperty("DBPW");
                System.out.println(home);

                con = DriverManager.getConnection(dburl, dbuser, dbpw);
            }catch (SQLException | IOException e){
                throw new RuntimeException(e);
            }
        }
        return con;
    }

    public static void close(final AutoCloseable obj){
        if (obj != null){
            try{
                obj.close();
            } catch (final Exception e) {
                //ignore
            }
        }

    }
}
