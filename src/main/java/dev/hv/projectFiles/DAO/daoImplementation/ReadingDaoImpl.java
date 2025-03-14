package dev.hv.projectFiles.DAO.daoImplementation;

import dev.hv.model.IReading;
import dev.hv.projectFiles.DAO.daoInterfaces.CustomerDao;
import dev.hv.projectFiles.DAO.daoInterfaces.ReadingDao;
import dev.hv.projectFiles.DAO.entities.Reading;
import dev.hv.projectFiles.DAO.entities.Customer;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.constraints.Length;

import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Implementierung des ReadingDao-Interfaces für die Verwaltung von Messwerten (Readings) in der Datenbank.
 * Diese Klasse bietet Methoden zum Hinzufügen, Abrufen, Aktualisieren und Löschen von Messwertdaten.
 */
public class ReadingDaoImpl implements ReadingDao<Reading> {
    private final Connection connection; // Verbindung zur Datenbank
    private final CustomerDao<Customer> customerDao; // DAO für Kunden
    private final Validator validator;

    /**
     * Konstruktor, der die Datenbankverbindung initialisiert.
     * @param connection die Datenbankverbindung
     */
    public ReadingDaoImpl(Connection connection) {
        this.connection = connection;
        this.customerDao = new CustomerDaoImpl(connection); // Initialisierung des CustomerDao
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    /**
     * Fügt einen neuen Messwert in die Datenbank ein.
     * @param reading das Messwert-Objekt, das hinzugefügt werden soll
     */
    @Override
    public void addReading(IReading reading) {

        validateReading(reading);

        try {
            String zaehlerstandColumn = "";
            // Bestimmung der Spalte basierend auf der Zählerart
            switch (reading.getKindOfMeter()) {
                case STROM -> zaehlerstandColumn = "zaehlerstand_in_kwh";
                case WASSER -> zaehlerstandColumn = "zaehlerstand_in_m3";
                case HEIZUNG -> zaehlerstandColumn = "zaehlerstand_in_mwh";
                case UNBEKANNT -> {
                    return; // Falls die Art unbekannt ist, keine Aktion
                }
            }

            String metre = reading.getKindOfMeter().toString().toLowerCase();
            // SQL-Query für das Einfügen eines neuen Messwerts
            String query = "INSERT INTO " + metre + " (uuid, kundenid, zaehlernummer, datum, " + zaehlerstandColumn + ", kommentar) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, reading.getId().toString());
            stmt.setString(2, reading.getCustomer().getId().toString()); // Kunden-ID
            stmt.setString(3, reading.getMeterId()); // Zählernummer
            stmt.setDate(4, Date.valueOf(reading.getDateOfReading())); // Datum
            stmt.setDouble(5, reading.getMeterCount()); // Zählerstand
            stmt.setString(6, reading.getComment()); // Kommentar
            stmt.executeUpdate(); // SQL ausführen
        } catch (SQLException | NullPointerException e) {
            System.out.println("Es wurden nicht alle erforderlichen Werte des Objekts erfüllt.\nEs wurde nicht in die Datenbank gespeichert.");

        }
    }

    /**
     * Holt einen Messwert aus der Datenbank basierend auf der Zählerart und ID.
     * @param kindOfMeter die Art des Zählers
     * @param id die ID des Messwerts
     * @return das gefundene Reading-Objekt oder null, falls nicht gefunden
     */
    @Override
    public Reading getReadingById(IReading.KindOfMeter kindOfMeter, String id) {
        if (kindOfMeter == IReading.KindOfMeter.UNBEKANNT) return null; // Kein Abruf für unbekannte Zählerart

        String zaehlerstandColumn = "";
        // Bestimmung der Spalte basierend auf der Zählerart
        switch (kindOfMeter) {
            case STROM -> zaehlerstandColumn = "zaehlerstand_in_kwh";
            case WASSER -> zaehlerstandColumn = "zaehlerstand_in_m3";
            case HEIZUNG -> zaehlerstandColumn = "zaehlerstand_in_mwh";
        }

        String metre = kindOfMeter.toString().toLowerCase();
        try {
            String query = "SELECT * FROM " + metre + " WHERE uuid = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Reading reading = new Reading();
                reading.setId(UUID.fromString(rs.getString("uuid"))); // Kunden-ID
                reading.setComment(rs.getString("kommentar")); // Kommentar
                reading.setCustomer(customerDao.getCustomerById(rs.getString("kundenid"))); // Kundenobjekt abrufen
                reading.setDateOfReading(rs.getDate("datum").toLocalDate()); // Datum
                reading.setKindOfMeter(kindOfMeter); // Zählerart
                reading.setMeterCount(rs.getDouble(zaehlerstandColumn)); // Zählerstand
                reading.setMeterId(rs.getString("zaehlernummer")); // Zählernummer
                return reading;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // Falls kein Messwert gefunden wurde
    }

    /**
     * Holt alle Messwerte einer bestimmten Zählerart aus der Datenbank.
     * @param kindOfMeter die Art des Zählers
     * @return Liste mit Reading-Objekten
     */
    @Override
    public List<Reading> getAllReadings(IReading.KindOfMeter kindOfMeter) {
        List<Reading> readings = new ArrayList<>();

        if (kindOfMeter == IReading.KindOfMeter.UNBEKANNT) {
            List<IReading.KindOfMeter> metreList = Arrays.asList(IReading.KindOfMeter.STROM, IReading.KindOfMeter.WASSER, IReading.KindOfMeter.HEIZUNG);
            for (int i = 0; i < metreList.size(); i++) {
                IReading.KindOfMeter currentMeter = metreList.get(i);

                String zaehlerstandColumn = "";

                switch (kindOfMeter) {
                    case STROM -> zaehlerstandColumn = "zaehlerstand_in_kwh";
                    case WASSER -> zaehlerstandColumn = "zaehlerstand_in_m3";
                    case HEIZUNG -> zaehlerstandColumn = "zaehlerstand_in_mwh";
                }
                String metre = currentMeter.toString().toLowerCase();
                try {
                    String query = "SELECT * FROM " + metre;
                    PreparedStatement stmt = connection.prepareStatement(query);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        Reading reading = new Reading();
                        reading.setId(UUID.fromString(rs.getString("uuid"))); // Kunden-ID
                        reading.setComment(rs.getString("kommentar")); // Kommentar
                        reading.setCustomer(customerDao.getCustomerById(rs.getString("kundenid"))); // Kundenobjekt abrufen
                        reading.setDateOfReading(rs.getDate("datum").toLocalDate()); // Datum
                        reading.setKindOfMeter(kindOfMeter); // Zählerart
                        reading.setMeterCount(rs.getDouble(zaehlerstandColumn)); // Zählerstand
                        reading.setMeterId(rs.getString("zaehlernummer")); // Zählernummer
                        readings.add(reading); // Messwert zur Liste hinzufügen
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            return readings;

        } else {

            String zaehlerstandColumn = "";
            // Bestimmung der Spalte basierend auf der Zählerart
            switch (kindOfMeter) {
                case STROM -> zaehlerstandColumn = "zaehlerstand_in_kwh";
                case WASSER -> zaehlerstandColumn = "zaehlerstand_in_m3";
                case HEIZUNG -> zaehlerstandColumn = "zaehlerstand_in_mwh";
            }


            String metre = kindOfMeter.toString().toLowerCase();
            try {
                String query = "SELECT * FROM " + metre;
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Reading reading = new Reading();
                    reading.setId(UUID.fromString(rs.getString("uuid"))); // Kunden-ID
                    reading.setComment(rs.getString("kommentar")); // Kommentar
                    reading.setCustomer(customerDao.getCustomerById(rs.getString("kundenid"))); // Kundenobjekt abrufen
                    reading.setDateOfReading(rs.getDate("datum").toLocalDate()); // Datum
                    reading.setKindOfMeter(kindOfMeter); // Zählerart
                    reading.setMeterCount(rs.getDouble(zaehlerstandColumn)); // Zählerstand
                    reading.setMeterId(rs.getString("zaehlernummer")); // Zählernummer
                    readings.add(reading); // Messwert zur Liste hinzufügen
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return readings; // Liste mit Messwerten zurückgeben
        }
    }

    /**
     * Aktualisiert einen bestehenden Messwert in der Datenbank.
     * @param reading das Reading-Objekt mit aktualisierten Werten
     */
    @Override
    public void updateReading(IReading reading) {

        validateReading(reading);

        try {
            String zaehlerstand = "";
            switch (reading.getKindOfMeter()) {
                case STROM -> zaehlerstand = "zaehlerstand_in_kwh";
                case WASSER -> zaehlerstand = "zaehlerstand_in_m3";
                case HEIZUNG -> zaehlerstand = "zaehlerstand_in_mwh";
                case UNBEKANNT -> {
                    return; // Keine Aktualisierung für unbekannte Zählerart
                }
            }

            String metre = reading.getKindOfMeter().toString().toLowerCase();

            // SQL-Query für das Aktualisieren eines Messwerts
            String query = "UPDATE " + metre + " SET kommentar = ?, kundenid = ?, datum = ?, zaehlernummer = ?, " + zaehlerstand + " = ? WHERE uuid = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, reading.getComment()); // Kommentar
            stmt.setString(2, reading.getCustomer().getId().toString()); // Kunden-ID
            stmt.setDate(3, Date.valueOf(reading.getDateOfReading())); // Datum
            stmt.setString(4, reading.getMeterId()); // Zählernummer
            stmt.setDouble(5, reading.getMeterCount()); // Zählerstand
            stmt.setString(6, reading.getId().toString());
            stmt.executeUpdate(); // SQL ausführen
        } catch (SQLException | NullPointerException e) {
            throw new RuntimeException("Error with updating reading: " + e);
        }
    }

    /**
     * Löscht einen Messwert aus der Datenbank basierend auf Zählerart und ID.
     * @param kindOfMeter die Art des Zählers
     * @param id die ID des Messwerts
     */
    @Override
    public void deleteReading(IReading.KindOfMeter kindOfMeter, String id) {
        String metre = kindOfMeter.toString().toLowerCase();
        try {
            String query = "DELETE FROM " + metre + " WHERE uuid = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, id);
            stmt.executeUpdate(); // SQL ausführen
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Validiert ein Reading auf seine richtigkeit.
     * @param reading das Reading welches validiert wird
     */
    private void validateReading(IReading reading) {
        Set<ConstraintViolation<IReading>> violations = validator.validate(reading);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder("Validation failed for Reading:\n");

            // Add all validation failures to the error message
            for (ConstraintViolation<IReading> violation : violations) {
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
