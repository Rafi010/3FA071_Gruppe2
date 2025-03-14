package dev.TestDao.CustomerDaoImpl;

import dev.BaseTest;
import dev.hv.model.ICustomer;
import dev.hv.projectFiles.DAO.daoImplementation.CustomerDaoImpl;
import dev.hv.projectFiles.DAO.daoInterfaces.CustomerDao;
import dev.hv.projectFiles.DAO.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TestCustomerGetAllUsers extends BaseTest {

    @BeforeEach
    public void initiate() {
        connection.createAllTables();
    }

    @Test
    void testGetAllUsers() {
        int dbEntryCount = 0; // erstellte Datenbankeinträge
        for (int i = 0; i < 10; i++) {
            UUID randomUuid = UUID.randomUUID();
            Date date = Date.valueOf(LocalDate.now());

            try {
                dbEntryCount = dbEntryCount + saveUserInDb(connection.getConnection(), randomUuid, date, i);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        CustomerDao<Customer> customerDao = new CustomerDaoImpl(connection.getConnection());
        List<Customer> allCustomers = customerDao.getAllCustomers();
        assertEquals(10, dbEntryCount); // Wurden alle Datenbankeinträge erstellt
        assertEquals(10, allCustomers.size()); // Wurde alle erstellten Datenbankeinträge durch DAO in die Liste aufgenommen
    }

    private int saveUserInDb(Connection connection, UUID uuid, Date date, int i) throws SQLException {
        String query = "INSERT INTO kunde (UUID, Anrede, Vorname, Nachname, Geburtsdatum) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, uuid.toString()); // UUID
        pst.setString(2, ICustomer.Gender.M.toString()); // Anrede
        pst.setString(3, "Max" + i); // Vorname
        pst.setString(4, "Mustermann" + i);
        pst.setDate(5, date);
        return pst.executeUpdate(); // gibt Anzahl der veränderten einträge zurück in diesem fall immer eins
    }


}
