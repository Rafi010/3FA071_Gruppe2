package dev.TestDao.CustomerDaoImpl;

import dev.TestUtils;
import dev.hv.model.ICustomer;
import dev.hv.projectFiles.DAO.daoImplementation.CustomerDaoImpl;
import dev.hv.projectFiles.DAO.daoInterfaces.CustomerDao;
import dev.hv.projectFiles.DAO.entities.User;
import dev.hv.projectFiles.DatabaseConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TestCustomerGetAllUsers {

    private static DatabaseConnection databaseConnection;

    @BeforeAll
    static void setUp() throws SQLException {
        // Initialisiert die Datenquelle und DatabaseConnection vor jedem Test
        Connection connection = TestUtils.getTestDbConnection();
        databaseConnection = new DatabaseConnection();
        databaseConnection.setConnection(connection);
        databaseConnection.createAllTables();

    }

    @Test
    void testGetAllUsers() {
        int dbEntryCount = 0; // erstellte Datenbankeinträge
        for (int i = 0; i < 10; i++) {
            UUID randomUuid = UUID.randomUUID();
            Date date = Date.valueOf(LocalDate.now());

            try {
                dbEntryCount = dbEntryCount + saveUserInDb(databaseConnection.getConnection(), randomUuid, date, i);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        CustomerDao<User> customerDao = new CustomerDaoImpl(databaseConnection.getConnection());
        List<User> allUsers = customerDao.getAllUsers();
        assertEquals(10, dbEntryCount); // Wurden alle Datenbankeinträge erstellt
        assertEquals(10, allUsers.size()); // Wurde alle erstellten Datenbankeinträge durch DAO in die Liste aufgenommen
    }

    private int saveUserInDb(Connection connection, UUID uuid, Date date, int i) throws SQLException {
        String query = "INSERT INTO kunde (UUID, Anrede, Vorname, Nachname, Geburtsdatum) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, uuid.toString()); // UUID
        pst.setString(2, ICustomer.Gender.M.toString()); // Anrede
        pst.setString(3, "Max"+i); // Vorname
        pst.setString(4, "Mustermann"+i);
        pst.setDate(5, date);
        return pst.executeUpdate(); // gibt Anzahl der veränderten einträge zurück in diesem fall immer eins
    }


}
