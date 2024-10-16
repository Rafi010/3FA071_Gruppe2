package dev.hv.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class DatabaseConnectionInfo {

    private static final String DB_NAME = "hv";

    public static void main(String[] args) {
        printDatabaseInfo();
    }

    public static void printDatabaseInfo() {
        try {
            final Connection con = Util.getConnection(DB_NAME);
            System.out.println("... connected");
            final DatabaseMetaData meta = con.getMetaData();
            printDriverInfo(meta);
            printDatabaseInfo(meta);
            Util.close(con);
        } catch (final SQLException e) {
            System.out.println("Fehler: " + e.getMessage());
        }
    }

    private static void printDriverInfo(DatabaseMetaData meta) throws SQLException {
        System.out.format("Driver: %s %d.%d\n",
                meta.getDriverName(),
                meta.getDriverMajorVersion(),
                meta.getDriverMinorVersion()
        );
    }

    private static void printDatabaseInfo(DatabaseMetaData meta) throws SQLException {
        System.out.format("DB: %s %d.%d (%s)\n",
                meta.getDatabaseProductName(),
                meta.getDatabaseMajorVersion(),
                meta.getDatabaseMinorVersion(),
                meta.getDatabaseProductVersion()
        );
    }
}