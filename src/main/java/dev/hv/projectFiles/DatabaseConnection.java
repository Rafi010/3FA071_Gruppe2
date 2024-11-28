package dev.hv.projectFiles;

import dev.hv.model.IDatebaseConnection;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;

public class DatabaseConnection implements IDatebaseConnection {
    //connection is saved locally so all db interaction can happen only within this class
    private Connection connection;

    //opens a connection to mySql (uses the url in the properties file wich does not connect to the hv database)
    @Override
    public IDatebaseConnection openConnection(){
        try {
            Dotenv dotenv = null;
            try {
                dotenv = Dotenv.configure().load();
                this.connection = DriverManager.getConnection(dotenv.get("MYSQL_URL"));
            } catch (Exception e) {
                System.out.println("No .env file found. Proceeding with environment variables.");
                String url = "jdbc:mariadb://database.railway.internal:3306/hv";
                this.connection = DriverManager.getConnection(url, "railway", "e2LBmjn0l~PjnOkGhn5sGGnGhWETLk_Z");
            }
            System.out.println("Connected to MySql");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public void createAllTables() {
        if (connection == null) {
            throw new IllegalStateException("No open database connection");
        }
        Util.executeSQL(connection, "dateien/sql/create_table.sql");
    }

    @Override
    public void truncateAllTables(){
        if (connection == null) {
            throw new IllegalStateException("No open database connection");
        }
        Util.executeSQL(connection, "dateien/sql/truncate_table.sql");
    }

    @Override
    public void removeAllTables(){
        if (connection == null) {
            throw new IllegalStateException("No open database connection");
        }
        Util.executeSQL(connection, "dateien/sql/remove_table.sql");
    }

    @Override
    public void closeConnection(){
        try {
            connection.close();
            System.out.println("Connection closed");
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void fillDatabase(){
        if (connection == null) {
            throw new IllegalStateException("No open database connection");
        }
        Util.executeSQL(connection, "dateien/sql/load_csv_file_in_table.sql");
    }

    public Connection getConnection() {
        return connection;
    }
}
