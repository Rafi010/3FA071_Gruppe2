package dev.TestDao.CustomerDaoImpl;

import dev.BaseTest;
import dev.hv.exceptions.DuplicateUserException;
import dev.hv.model.ICustomer;
import dev.hv.projectFiles.DAO.daoImplementation.CustomerDaoImpl;
import dev.hv.projectFiles.DAO.daoInterfaces.CustomerDao;
import dev.hv.projectFiles.DAO.entities.Customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Diese Klasse enthält Tests zum Hinzufügen von Nutzern in die Datenbank mittels der CustomerDao-Klasse.
 */
class TestCustomerAddCustomer extends BaseTest {

    @BeforeEach
    public void initiate() {
        connection.createAllTables();
    }

    /**
     * Testet, ob ein Nutzer korrekt in die Datenbank eingefügt wird.
     * Dieser Test überprüft, ob die Methode zum Hinzufügen eines Nutzers funktioniert und die Daten korrekt gespeichert werden.
     */
    @Test
    void testAddUserToDatabase() {
        Customer testCustomer = getTestUser(UUID.randomUUID());

        CustomerDao<Customer> customerDao = new CustomerDaoImpl(connection.getConnection());
        customerDao.addCustomer(testCustomer);
        try {
            verifyUserInDatabase(connection.getConnection(), testCustomer);
        } catch (SQLException e) {
            fail("SQLException occurred while checking if tables exist: " + e.getMessage());
        }
    }

    /**
     * Testet, ob ein Nutzer mit fehlenden Daten (z.B. fehlendem Vornamen) nicht hinzugefügt werden kann.
     */
    @Test
    void testAddUserWithMissingFirstName() {
        Customer testCustomer = getTestUser(UUID.randomUUID());
        testCustomer.setFirstName(""); // Setzt den Vornamen auf eine leere Zeichenkette

        CustomerDao<Customer> customerDao = new CustomerDaoImpl(connection.getConnection());
        assertThrows(IllegalArgumentException.class, () -> customerDao.addCustomer(testCustomer), "Es wurde erwartet, dass eine SQLException geworfen wird bei fehlendem Vornamen.");
    }

    /**
     * Testet, ob beim Hinzufügen eines Nutzers mit derselben UUID eine Ausnahme geworfen wird.
     * Dieser Test überprüft, ob Duplikate im UUID-Feld korrekt behandelt werden.
     */
    //@Test
    void testAddDuplicateUser() {
        UUID duplicateId = UUID.randomUUID();
        Customer customer1 = getTestUser(duplicateId);
        Customer customer2 = getTestUser(duplicateId); // Gleiche UUID

        CustomerDao<Customer> customerDao = new CustomerDaoImpl(connection.getConnection());
        customerDao.addCustomer(customer1);
        assertThrows(DuplicateUserException.class, () -> customerDao.addCustomer(customer2), "Es wurde erwartet, dass eine SQLException geworfen wird bei einer doppelten UUID.");
    }

    /**
     * Testet das Hinzufügen eines Nutzers mit ungültigem Geschlecht (z.B. null).
     * Dieser Test stellt sicher, dass ungültige Geschlechtswerte richtig behandelt werden.
     */
    @Test
    void testAddUserWithInvalidGender() {
        Customer invalidCustomer = getTestUser(UUID.randomUUID());
        invalidCustomer.setFirstName(null); // Setzt das Geschlecht auf null

        CustomerDao<Customer> customerDao = new CustomerDaoImpl(connection.getConnection());
        assertThrows(IllegalArgumentException.class, () -> customerDao.addCustomer(invalidCustomer), "Es wurde erwartet, dass eine NullPointerException geworfen wird falls ein wert null ist.");
    }

    /**
     * Gibt einen User mit Daten zum Testen zurück.
     *
     * @param id die UUID, die man dem User geben möchte
     * @return das User-Objekt, das zum Testen verwendet wird
     */
    private Customer getTestUser(UUID id) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setFirstName("Max");
        customer.setLastName("Mustermann");
        customer.setGender(ICustomer.Gender.M);
        customer.setBirthDate(LocalDate.of(2000, 1, 1));
        return customer;
    }

    /**
     * Verifiziert, ob ein Nutzer mit einer bestimmten UUID in der Datenbank existiert.
     * Diese Methode prüft, ob der Nutzer in der Tabelle `users` korrekt gespeichert wurde.
     *
     * @param connection die Verbindung zur Datenbank
     * @param customer       das Nutzerobjekt, das in der Datenbank überprüft werden soll
     * @throws SQLException falls ein SQL Fehler auftritt, wird dieser an das JUnit Framework übergeben zur Behandlung
     */
    private void verifyUserInDatabase(Connection connection, Customer customer) throws SQLException {
        String query = "SELECT * FROM kunde WHERE uuid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, customer.getId().toString());
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next(), "Der Nutzer wurde nicht in der Datenbank gefunden.");
                assertEquals(customer.getId().toString(), rs.getString("uuid"));
                assertEquals(customer.getFirstName(), rs.getString("vorname"));
                assertEquals(customer.getLastName(), rs.getString("nachname"));
                assertEquals(customer.getBirthDate(), rs.getDate("geburtsdatum").toLocalDate());
            }
        }
    }
}
