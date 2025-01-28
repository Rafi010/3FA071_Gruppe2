package dev.hv.projectFiles.DAO.daoImplementation;

import dev.hv.exceptions.DuplicateUserException;
import dev.hv.model.ICustomer;
import dev.hv.projectFiles.DAO.daoInterfaces.CustomerDao;
import dev.hv.projectFiles.DAO.entities.Customer;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Implementierung des CustomerDao-Interfaces für die Verwaltung von Kunden in der Datenbank.
 * Diese Klasse bietet Methoden zum Hinzufügen, Abrufen, Aktualisieren und Löschen von Benutzerdaten.
 */
public class CustomerDaoImpl implements CustomerDao<Customer> {

    private final Connection connection; // Datenbankverbindung
    private final Validator validator;
    /**
     * Konstruktor für CustomerDaoImpl.
     * @param connection die Datenbankverbindung, die von dieser DAO genutzt wird.
     */
    public CustomerDaoImpl(Connection connection) {
        this.connection = connection;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    /**
     * Methode, um einen Nutzer in der Datenbank zu erstellen.
     * @param customer das Nutzer-Objekt, das in die Datenbank eingefügt wird.
     */
    @Override
    public void addCustomer(Customer customer) throws NullPointerException{
        validateCustomer(customer);

        // Konvertieren des Geburtsdatums in das SQL-Format
        LocalDate birthDate = customer.getBirthDate();
        Date sqlDate = (birthDate == null) ? null : Date.valueOf(birthDate);

        try {
            // SQL-Query zum Einfügen eines neuen Nutzers
            String query = "INSERT INTO kunde (UUID, Anrede, Vorname, Nachname, Geburtsdatum) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, customer.getId().toString()); // UUID
            stmt.setString(2, customer.getGender().toString()); // Anrede
            stmt.setString(3, customer.getFirstName()); // Vorname
            stmt.setString(4, customer.getLastName()); // Nachname
            stmt.setDate(5, sqlDate); // Geburtsdatum
            stmt.executeUpdate(); // SQL-Query ausführen
        } catch (SQLException e) {
            if (e.getErrorCode() == 23505) {  // Assuming the unique constraint error code is 23505
                throw new DuplicateUserException("Duplicate UUID detected", e);
            }
            throw new RuntimeException("Database error", e);
        }
    }

    /**
     * Methode, um einen Benutzer aus der Datenbank anhand der ID zu bekommen.
     * @param id die ID, anhand der der Nutzer gesucht wird.
     * @return gibt den anhand der Datenbankdaten erstellten Nutzer zurück.
     */
    @Override
    public ICustomer getCustomerById(String id) {
        try {
            // SQL-Query zum Abrufen eines Nutzers anhand der UUID
            String query = "SELECT * FROM kunde WHERE uuid = ?";
            PreparedStatement stmt = connection.prepareStatement(query); // PreparedStatement erstellen
            stmt.setString(1, id); // ID in die Query einfügen
            ResultSet rs = stmt.executeQuery(); // Query ausführen

            // Ergebnis verarbeiten, wenn der Nutzer gefunden wird
            if (rs.next()) {
                Customer customer = new Customer();
                customer.setId(UUID.fromString(rs.getString("uuid"))); // UUID setzen
                customer.setGender(ICustomer.Gender.valueOf(rs.getString("anrede"))); // Anrede setzen
                customer.setFirstName(rs.getString("vorname")); // Vorname setzen
                customer.setLastName(rs.getString("nachname")); // Nachname setzen
                if (rs.getDate("geburtsdatum") == null) {
                    customer.setBirthDate(null);
                } else {
                    customer.setBirthDate(rs.getDate("geburtsdatum").toLocalDate()); // Geburtsdatum setzen
                }
                return customer;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // Fehlerausgabe im Fehlerfall
        }
        return null; // Rückgabe null, wenn kein Nutzer gefunden wurde
    }

    /**
     * Methode, um alle Nutzer aus der Datenbank als Liste auszugeben.
     * @return Liste mit den Nutzer-Objekten, die zuvor für jeden Eintrag erstellt wurden.
     */
    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>(); // Liste für die Nutzer erstellen
        try {
            // SQL-Query zum Abrufen aller Nutzer
            String query = "SELECT * FROM kunde";
            Statement stmt = connection.createStatement(); // Statement erstellen
            ResultSet rs = stmt.executeQuery(query); // Query ausführen

            // Ergebnis verarbeiten und Nutzerobjekte erstellen
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(UUID.fromString(rs.getString("uuid"))); // UUID setzen
                customer.setGender(ICustomer.Gender.valueOf(rs.getString("anrede"))); // Anrede setzen
                customer.setFirstName(rs.getString("vorname")); // Vorname setzen
                customer.setLastName(rs.getString("nachname")); // Nachname setzen
                customer.setBirthDate(rs.getDate("geburtsdatum").toLocalDate()); // Geburtsdatum setzen
                customers.add(customer); // Nutzer zur Liste hinzufügen
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // Fehlerausgabe im Fehlerfall
        }
        return customers; // Rückgabe der Nutzerliste
    }

    /**
     * Methode, um einen Nutzer aus der Datenbank zu entfernen.
     * @param id die ID des Nutzers, der aus der Datenbank entfernt wird.
     */
    @Override
    public void deleteCustomer(String id) {
        try {
            // SQL-Query zum Löschen eines Nutzers anhand der UUID
            String query = "DELETE FROM kunde WHERE uuid = ?";
            PreparedStatement stmt = connection.prepareStatement(query); // PreparedStatement erstellen
            stmt.setString(1, id); // ID in die Query einfügen
            stmt.executeUpdate(); // Query ausführen
        } catch (SQLException e) {
            throw new RuntimeException(e); // Fehlerausgabe im Fehlerfall
        }
    }

    /**
     * Methode, um einen Nutzer innerhalb der Datenbank zu aktualisieren.
     * @param customer das Nutzer-Objekt, das aktualisiert werden soll.
     */
    @Override
    public void updateCustomer(Customer customer) {
        validateCustomer(customer);

        // Konvertieren des Geburtsdatums in das SQL-Format
        LocalDate birthDate = customer.getBirthDate();
        Date sqlDate = Date.valueOf(birthDate);

        try {
            // SQL-Query zum Aktualisieren eines Nutzers
            String query = "UPDATE kunde SET anrede = ?, vorname = ?, nachname = ?, geburtsdatum = ? WHERE uuid = ?";
            PreparedStatement stmt = connection.prepareStatement(query); // PreparedStatement erstellen
            stmt.setString(1, customer.getGender().toString()); // Anrede
            stmt.setString(2, customer.getFirstName()); // Vorname
            stmt.setString(3, customer.getLastName()); // Nachname
            stmt.setDate(4, sqlDate); // Geburtsdatum
            stmt.setString(5, customer.getId().toString()); // UUID
            stmt.executeUpdate(); // Query ausführen
        } catch (SQLException e) {
            throw new RuntimeException(e); // Fehlerausgabe im Fehlerfall
        }
    }

    /**
     * Validiert einen Customer auf seine richtigkeit.
     * @param customer der Customer welches validiert wird
     */
    private void validateCustomer(Customer customer) {
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder("Validation failed for User:\n");

            // Add all validation failures to the error message
            for (ConstraintViolation<Customer> violation : violations) {
                sb.append("Property: ")
                        .append(violation.getPropertyPath())
                        .append(" - ")
                        .append(violation.getMessage()) // validation message
                        .append("\n");
            }

            // Throw an exception with the validation error message
            throw new IllegalArgumentException(sb.toString());
        }
    }
}
