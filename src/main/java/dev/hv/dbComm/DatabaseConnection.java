package dev.hv.dbComm;

import dev.hv.model.IDatebaseConnection;
import dev.hv.test.Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseConnection implements IDatebaseConnection {
    private Connection connection;


    @Override
    public IDatebaseConnection openConnection(Properties properties){

        connection = Util.getConnection("hv");
        //TODO DATENBANKÖFFNUNG IMPLEMENTIERUNG

        return this;
    }

    @Override
    public void createAllTables() {
        if (connection == null) {
            throw new IllegalStateException("No open database connection");
        }

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SHOW TABLES");

        } catch (SQLException e) {
            e.printStackTrace();  // Prints detailed information about the exception
        }
    }

    @Override
    public void truncateAllTables(){
        //TODO
    }

    @Override
    public void removeAllTables(){
        //TODO
    }

    @Override
    public void closeConnection(){
        //TODO
    }

    public static void main(String[] args){
        DatabaseConnection dbConnection = new DatabaseConnection();
        dbConnection.openConnection(null);
        dbConnection.createAllTables();
    }
}
