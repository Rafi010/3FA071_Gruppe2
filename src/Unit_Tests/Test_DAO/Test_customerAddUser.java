package Test_DAO;

import dev.hv.exceptions.DuplicateUserException;
import dev.hv.model.ICustomer;
import dev.hv.projectFiles.DAO.daoImplementation.CustomerDaoImpl;
import dev.hv.projectFiles.DAO.daoInterfaces.CustomerDao;
import dev.hv.projectFiles.DAO.entities.User;
import dev.hv.projectFiles.DatabaseConnection;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
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
class TestCustomerAddUser {

    private DatabaseConnection databaseConnection;

    /**
     * Diese Methode wird vor jedem einzelnen Test ausgeführt und initialisiert das `databaseConnection` Objekt.
     * Sie stellt sicher, dass eine neue Datenbankverbindung und eine leere Testdatenbank vor jedem Test vorhanden sind.
     */
    @BeforeEach
    void setUp() throws SQLException {
        // Initialisiert die Datenquelle und DatabaseConnection vor jedem Test
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        databaseConnection = new DatabaseConnection();

        Connection connection = dataSource.getConnection();
        databaseConnection.setConnection(connection);
        databaseConnection.createAllTables();

    }

    /**
     * Diese Methode wird nach jedem Test ausgeführt und löscht alle Tabellen der Testdatenbank.
     * Dies stellt sicher, dass jeder Test in einer sauberen Umgebung ausgeführt wird.
     */
    @AfterEach
    void tearDown() {
            databaseConnection.removeAllTables(); // Löscht alle Tabellen nach jedem Test
            databaseConnection.closeConnection();

    }

    /**
     * Testet, ob ein Nutzer korrekt in die Datenbank eingefügt wird.
     * Dieser Test überprüft, ob die Methode zum Hinzufügen eines Nutzers funktioniert und die Daten korrekt gespeichert werden.
     * @throws SQLException falls ein SQL Fehler auftritt, wird dieser an das JUnit Framework übergeben zur Behandlung
     */
    @Test
    void testAddUserToDatabase() throws SQLException {
        User testUser = getTestUser(UUID.randomUUID());

        CustomerDao<User> customerDao = new CustomerDaoImpl(databaseConnection.getConnection());
        customerDao.addUser(testUser);

        verifyUserInDatabase(databaseConnection.getConnection(), testUser);
    }

    /**
     * Testet, ob ein Nutzer mit fehlenden Daten (z.B. fehlendem Vornamen) nicht hinzugefügt werden kann.
     */
    @Test
    void testAddUserWithMissingFirstName() {
        User testUser = getTestUser(UUID.randomUUID());
        testUser.setFirstName(""); // Setzt den Vornamen auf eine leere Zeichenkette

        CustomerDao<User> customerDao = new CustomerDaoImpl(databaseConnection.getConnection());
        assertThrows(IllegalArgumentException.class, () -> customerDao.addUser(testUser), "Es wurde erwartet, dass eine SQLException geworfen wird bei fehlendem Vornamen.");
    }

    /**
     * Testet, ob beim Hinzufügen eines Nutzers mit derselben UUID eine Ausnahme geworfen wird.
     * Dieser Test überprüft, ob Duplikate im UUID-Feld korrekt behandelt werden.
     */
    @Test
    void testAddDuplicateUser() {
        UUID duplicateId = UUID.randomUUID();
        User user1 = getTestUser(duplicateId);
        User user2 = getTestUser(duplicateId); // Gleiche UUID

        CustomerDao<User> customerDao = new CustomerDaoImpl(databaseConnection.getConnection());
        customerDao.addUser(user1);
        assertThrows(DuplicateUserException.class, () -> customerDao.addUser(user2), "Es wurde erwartet, dass eine SQLException geworfen wird bei einer doppelten UUID.");
    }

    /**
     * Testet das Hinzufügen eines Nutzers mit ungültigem Geschlecht (z.B. null).
     * Dieser Test stellt sicher, dass ungültige Geschlechtswerte richtig behandelt werden.
     */
    @Test
    void testAddUserWithInvalidGender() {
        User invalidUser = getTestUser(UUID.randomUUID());
        invalidUser.setFirstName(null); // Setzt das Geschlecht auf null

        CustomerDao<User> customerDao = new CustomerDaoImpl(databaseConnection.getConnection());
        assertThrows(IllegalArgumentException.class, () -> customerDao.addUser(invalidUser), "Es wurde erwartet, dass eine NullPointerException geworfen wird falls ein wert null ist.");
    }

    /**
     * Gibt einen User mit Daten zum Testen zurück.
     * @param id die UUID, die man dem User geben möchte
     * @return das User-Objekt, das zum Testen verwendet wird
     */
    private User getTestUser(UUID id) {
        User user = new User();
        user.setId(id);
        user.setFirstName("Max");
        user.setLastName("Mustermann");
        user.setGender(ICustomer.Gender.M);
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        return user;
    }

    /**
     * Verifiziert, ob ein Nutzer mit einer bestimmten UUID in der Datenbank existiert.
     * Diese Methode prüft, ob der Nutzer in der Tabelle `users` korrekt gespeichert wurde.
     * @param connection die Verbindung zur Datenbank
     * @param user das Nutzerobjekt, das in der Datenbank überprüft werden soll
     * @throws SQLException falls ein SQL Fehler auftritt, wird dieser an das JUnit Framework übergeben zur Behandlung
     */
    private void verifyUserInDatabase(Connection connection, User user) throws SQLException {
        String query = "SELECT * FROM kunde WHERE uuid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getId().toString());
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next(), "Der Nutzer wurde nicht in der Datenbank gefunden.");
                assertEquals(user.getId().toString(), rs.getString("uuid"));
                assertEquals(user.getFirstName(), rs.getString("vorname"));
                assertEquals(user.getLastName(), rs.getString("nachname"));
                assertEquals(user.getBirthDate(), rs.getDate("geburtsdatum").toLocalDate());
            }
        }
    }
}
