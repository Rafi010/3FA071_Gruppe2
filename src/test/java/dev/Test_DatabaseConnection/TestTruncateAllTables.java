package dev.Test_DatabaseConnection;

import dev.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testng.AssertJUnit.fail;

public class TestTruncateAllTables extends BaseTest {


    @BeforeEach
    public void initiate() {
        connection.createAllTables();
        connection.fillDatabase();
    }

    @Test
    public void TestTruncateAllTables() {
        Connection conn = connection.getConnection();
        String[] expectedTables = {"ablesung", "kunde"};
        for (String table : expectedTables) {
            String query = "SELECT COUNT(*) FROM " + table;
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int rowCount = rs.getInt(1);
                    if (rowCount > 0) {
                        System.out.println("The table '" + table + "' contains " + rowCount + " entries.");
                    } else {
                        System.out.println("The table '" + table + "' is empty.");
                    }
                }
            } catch (SQLException e) {
                fail("Error when retrieving the entries from the table " + table + ": " + e.getMessage());
            }
        }

        connection.truncateAllTables();


        for (String table : expectedTables) {
            String query = "SELECT COUNT(*) FROM " + table;
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int rowCount = rs.getInt(1);
                    assertEquals(0, rowCount, "The table '" + table + "' is not empty after Truncate.");
                }
            } catch (SQLException e) {
                fail("Error when retrieving entries from the table after truncate " + table + ": " + e.getMessage());
            }
        }
    }
    
}