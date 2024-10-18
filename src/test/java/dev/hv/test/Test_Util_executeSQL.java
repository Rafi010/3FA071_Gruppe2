package dev.hv.test;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class Test_Util_executeSQL {
    private static final String DB_URL = "jdbc:h2:mem:testdb";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    private static Connection connection;

    @BeforeAll
    public static void setup() {
        try {
            connection = initializeDatabase(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to connect to the database");
        }
    }

    @Test
    @DisplayName("Test executeSQL Method")
    public void testExecuteSql() {
        String testSQLFilePath = "src/test/resources/test_script.sql";
        try {
            createTestSqlFile(testSQLFilePath);
            assertDoesNotThrow(() -> executeSqlScript(connection, testSQLFilePath));

            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT name FROM test_table WHERE id = 1");
                assertTrue(rs.next());
                assertEquals("Test", rs.getString("name"));
            }
        } catch (Exception e) {
            fail("Failed to query the database");
        } finally {
            new File(testSQLFilePath).delete();
        }
    }

    @AfterAll
    public static void cleanup() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection initializeDatabase(String url, String user, String password) throws Exception {
        Connection con = DriverManager.getConnection(url, user, password);
        try (Statement stmt = con.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY, name VARCHAR(50))");
        }
        return con;
    }

    public static void createTestSqlFile(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("INSERT INTO test_table (id, name) VALUES (1, 'Test');");
        }
    }

    public static void executeSqlScript(Connection con, String filePath) {
        try {
            ScriptRunner sr = new ScriptRunner(con);
            Reader reader = new BufferedReader(new FileReader(filePath));
            sr.runScript(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}