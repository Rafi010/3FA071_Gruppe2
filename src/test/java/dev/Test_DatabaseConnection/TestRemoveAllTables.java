package dev.Test_DatabaseConnection;

import dev.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class TestRemoveAllTables extends BaseTest {

    @BeforeEach
    public void initiate(){
        connection.createAllTables();
    }

    @Test
    public void TestRemoveAllTables() {
        Connection conn = connection.getConnection();
        String[] expectedTables = {"heizung", "strom", "wasser", "kunde"};
        try {
            for (int i = 0; i < expectedTables.length; i++) {
                String query = "SELECT * FROM " + expectedTables[i];
                PreparedStatement stmt = conn.prepareStatement(query);
                System.out.println(stmt);
                ResultSet rs = stmt.executeQuery();
            }
        } catch(SQLException e){
            fail("SQLException occurred while checking if tables exist: " + e.getMessage());
        }

        connection.removeAllTables();

        try {
            for (int i = 0; i < expectedTables.length; i++) {
                String query = "SELECT * FROM " + expectedTables[i];
                PreparedStatement stmt = conn.prepareStatement(query);
                System.out.println(stmt);
                assertThrows(SQLException.class, () -> stmt.executeQuery(), "");
            }
        } catch(SQLException e){
            fail("SQLException occurred while checking if tables exist: " + e.getMessage());
        }
    }

}
