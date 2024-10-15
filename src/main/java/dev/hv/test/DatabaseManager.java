package dev.hv.test;

import java.sql.*;

public class DatabaseManager {
    public static void main(String[] args) {
        try {
            final Connection connection = Util.getConnection("hv");
            System.out.println("... connected");

            printDatabaseMetaData(connection);

            Statement statement = connection.createStatement();
            processTables(statement);

            int rowsInserted = statement.executeUpdate("INSERT INTO wasser (column2) VALUES (456)");
            System.out.println(rowsInserted);

            Util.close(statement);
            Util.close(connection);
        } catch (final SQLException e) {
            System.out.println("Fehler: " + e.getMessage());
        }
    }

    private static void printDatabaseMetaData(Connection connection) throws SQLException {
        final DatabaseMetaData meta = connection.getMetaData();
        System.out.format("Driver : %s %s.%s\n",
                meta.getDriverName(),
                meta.getDriverMajorVersion(),
                meta.getDriverMinorVersion());
        System.out.format("DB : %s %s.%s (%s)\n",
                meta.getDatabaseProductName(),
                meta.getDatabaseMajorVersion(),
                meta.getDatabaseMinorVersion(),
                meta.getDatabaseProductVersion());
    }

    private static void processTables(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SHOW TABLES");
        while (resultSet.next()) {
            String tableName = resultSet.getString(1);
            System.out.println(tableName);
            Util.printRs(resultSet);

            describeTable(statement, tableName);
        }
    }

    private static void describeTable(Statement statement, String tableName) throws SQLException {
        ResultSet rs = statement.executeQuery("DESCRIBE " + tableName);
        while (rs.next()) {
            String column = rs.getString(1);
            System.out.println(column);
        }
    }
}