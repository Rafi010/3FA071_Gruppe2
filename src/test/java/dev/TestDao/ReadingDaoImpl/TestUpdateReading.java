package dev.TestDao.ReadingDaoImpl;

import dev.BaseTest;
import dev.hv.projectFiles.DAO.daoImplementation.ReadingDaoImpl;
import dev.hv.projectFiles.DAO.entities.Reading;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/*
public class testUpdateReading extends BaseTest {
    ReadingDaoImpl dao = new ReadingDaoImpl(connection.getConnection());

    @BeforeEach
    public void initiate() {
        connection.createAllTables();
        connection.fillDatabase();

    }


    Tabellenspalten:

    Strom: Kunde,Zaehlernummer,Datum,Zählerstand in kWh,Kommentar
    Wasser: Kunde,Zaehlernummer,Datum,Zählerstand in m³,Kommentar
    Heizung: Kunde,Zaehlernummer,Datum,Zählerstand in MWh,Kommentar

    UPDATE zaehlerdaten
    SET Zaehlernummer = '1234567890',
    Datum = '2025-01-27',
    `Zählerstand in kWh` = 15000,
    Kommentar = 'Zählerstand manuell aktualisiert'
    WHERE Kunde = 'Musterkunde' AND Zaehlernummer = '0987654321';



    public void updateDataWithUnits(String tableName, String kunde, String zaehlernummer, String datum, double zaehlerstand, String kommentar) throws SQLException {
        if (!List.of("strom", "wasser", "heizung").contains(tableName.toLowerCase())) {
            throw new IllegalArgumentException("Ungültiger Tabellenname: " + tableName);
        }

        String zaehlerstandColumn = switch (tableName.toLowerCase()) {
            case "wasser" -> "zaehlerstand_in_m3";
            case "heizung" -> "zaehlerstand_in_mwh";
            case "strom" -> "zaehlerstand_in_kwh";
            default -> throw new IllegalArgumentException("Unbekannte Tabelle: " + tableName);

        };
        Reading reading1 = new Reading();
        dao.addReading(reading1);

        dao.getReadingById();

        String updateQuery = "UPDATE " + tableName +
                " SET kunde = ?, zaehlernummer = ?, datum = ?, " + zaehlerstandColumn + " = ?, kommentar = ? " +
                "WHERE zaehlernummer = ?";


        try {
            Connection conn = connection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(updateQuery);

            // Parameter setzen
            stmt.setString(1, kunde);
            stmt.setString(2, zaehlernummer);
            stmt.setString(3, datum);
            stmt.setDouble(4, zaehlerstand);
            stmt.setString(5, kommentar);
            stmt.setString(6, zaehlernummer); // WHERE-Bedingung

            // Abfrage ausführen
            int rowsUpdated = stmt.executeUpdate();
            System.out.println("Anzahl aktualisierter Zeilen: " + rowsUpdated);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @Test
    public void ts() throws SQLException {
        Connection conn = connection.getConnection();
        String id = UUID.randomUUID().toString();
        String query = "UPDATE table SET last_name = 'Schmidt' WHERE customer_id = 1";
        String updateQuery = "UPDATE ? SET kunde = ?, zaehlernummer = ?, datum = ?, ''  ";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString();
        stmt.executeQuery();


    }

}

 */

