package dev.hv.rest.ResourceEndpoints;

import dev.hv.model.IReading;
import dev.hv.projectFiles.DAO.daoImplementation.CustomerDaoImpl;
import dev.hv.projectFiles.DAO.daoImplementation.ReadingDaoImpl;
import dev.hv.projectFiles.DAO.daoInterfaces.CustomerDao;
import dev.hv.projectFiles.DAO.daoInterfaces.ReadingDao;
import dev.hv.projectFiles.DAO.entities.Customer;
import dev.hv.projectFiles.DAO.entities.Reading;
import dev.hv.projectFiles.DatabaseConnection;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Resource-Klasse, die REST-Endpunkte für die Verwaltung von Zählerständen bereitstellt.
 * Ermöglicht das Hinzufügen, Aktualisieren, Abrufen und Löschen von Zählerständen.
 */
@Path("/readings")
public class ReadingResource {

    DatabaseConnection connection = DatabaseConnection.getInstance();
    CustomerDao<Customer> customerDao = new CustomerDaoImpl(connection.getConnection());
    ReadingDao<Reading> readingDao = new ReadingDaoImpl(connection.getConnection());

    /**
     * Erstellt einen neuen Zählerstand.
     *
     * @param reading Das zu erstellende Reading-Objekt. Muss gültig sein (siehe Bean Validation).
     * @return Eine HTTP-Antwort mit dem erstellten Zählerstand im JSON-Format.
     * @throws WebApplicationException mit folgenden möglichen Status-Codes:
     *                                 201 (Created) - Wenn der Zählerstand erfolgreich erstellt wurde.
     *                                 400 (Bad Request) - Wenn die übergebenen Daten ungültig sind.
     *                                 500 (Internal Server Error) - Bei unerwarteten Fehlern während der Verarbeitung.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response readingsPost(@Valid Reading reading) {

        //add UUID if not existent
        if (reading.getId() == null) {
            reading.setId(UUID.randomUUID());
        }

        Customer customer = (Customer) reading.getCustomer();

        //add customer of reading to DB if not already present
        if (customerDao.getCustomerById(customer.getId().toString()) == null) {
            customerDao.addCustomer(customer);
        }

        readingDao.addReading(reading);

        return Response.status(Response.Status.CREATED)
                .entity(reading)
                .build();

    }

    /**
     * Aktualisiert einen bestehenden Zählerstand.
     *
     * @param reading Das zu aktualisierende Reading-Objekt. Muss gültig sein (siehe Bean Validation) und eine ID enthalten.
     * @return Eine HTTP-Antwort mit dem aktualisierten Zählerstand im JSON-Format.
     * @throws WebApplicationException mit folgenden möglichen Status-Codes:
     *                                 200 (OK) - Wenn der Zählerstand erfolgreich aktualisiert wurde.
     *                                 400 (Bad Request) - Wenn die übergebenen Daten ungültig sind.
     *                                 404 (Not Found) - Wenn kein Zählerstand mit der angegebenen ID existiert.
     *                                 500 (Internal Server Error) - Bei unerwarteten Fehlern während der Verarbeitung.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response readingPut(@Valid Reading reading) {
        if (reading.getId() == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Reading existingReading = findReadingByUuid(reading.getId().toString());
        if (existingReading == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            Customer customer = (Customer) reading.getCustomer();
            if (customer.getId() == null){ // Sollte Kunden ID nicht gegeben sein, wird Kunde aus der Ablesung genommen
                customer = (Customer) existingReading.getCustomer();
            }
            //Kunde zur DB hinzufügen, wenn nicht vorhanden, sonst wird der Kunde der Ablesung geändert
            //es werden aber keine Kundenänderungen übernommen, falls andere Namen oder Gender eingegeben werden!
            if (customerDao.getCustomerById(customer.getId().toString()) == null && (customer.getFirstName() == null
                    || customer.getLastName() == null || customer.getGender() == null)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Kunde zu gegebener ID existiert nicht!\nID korrigieren oder zusätzlich 'firstName', " +
                                "'lastName' und 'gender' des Kunden angeben, um einen neuen Kundeneintrag anzulegen.")
                        .build();
            } else if (customerDao.getCustomerById(customer.getId().toString()) == null
                    && customer.getFirstName() != null
                    && customer.getLastName() != null
                    && customer.getGender() != null) {
                customerDao.addCustomer(customer);
            } else {
                try {
                    customer = (Customer) customerDao.getCustomerById(customer.getId().toString());
                } catch (Exception e) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Kunde wurde anhand der ID nicht gefunden!")
                            .build();
                }
            }
            reading.setCustomer(customer);
            readingDao.updateReading(reading);
            String responseMessage = String.format("Ablesung erfolgreich aktualisiert - ID der Ablesung: %s, " +
                            "Datum der Ablesung: %s,\n      Art der Ablesung: %s, Zählerstand: %s, Zählernummer: %s, " +
                            "Ersatzzähler: %s, Kommentar: %s,\n      Kunde: %s %s mit ID: %s",
                    existingReading.getId(),
                    existingReading.getDateOfReading(),
                    existingReading.getKindOfMeter(),
                    existingReading.getMeterCount(),
                    existingReading.getMeterId(),
                    existingReading.getSubstitute() != null ? existingReading.getSubstitute() : "N/A",
                    existingReading.getComment() != null ? existingReading.getComment() : "N/A",
                    customer.getFirstName() != null ? customer.getFirstName() : "N/A",
                    customer.getLastName() != null ? customer.getLastName() : "N/A",
                    customer.getId() != null ? customer.getId().toString() : "N/A");
            return Response.status(Response.Status.OK)
                    .entity(responseMessage)
                    .build();
        }
    }

    /**
     * Sucht eine Ablesung anhand ihrer eindeutigen UUID.
     *
     * @param uuid Die eindeutige Identifikationsnummer der Ablesung.
     * @return Eine HTTP-Antwort mit den Ablesungsdetails im JSON-Format.
     * @throws WebApplicationException mit folgenden möglichen Status-Codes:
     *                                 200 (OK) - Wenn die Ablesung gefunden wurde.
     *                                 404 (Not Found) - Wenn keine Ablesung mit der angegebenen UUID existiert.
     *                                 500 (Internal Server Error) - Bei unerwarteten Fehlern während der Verarbeitung.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readingGetById(@PathParam("id") String uuid) {
        try {
            // Abrufen des existierenden Benutzers
            Reading existingReading = findReadingByUuid(uuid);

            if (existingReading == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"status\":\"error\",\"message\":\"Ablesung nicht gefunden\"}")
                        .build();
            }

            return Response.status(Response.Status.OK)
                    .entity(existingReading)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"status\":\"error\",\"message\":\"Fehler beim Abrufen der Ablesung\"}")
                    .build();
        }
    }

    /**
     * Ruft spezifische Zählerstände basierend auf verschiedenen Suchparametern ab.
     *
     * @param customer (Optional) Die ID des Kunden, dessen Zählerstände abgerufen werden sollen.
     * @param date1Str (Optional) Das Startdatum für den Zeitraum, in dem die Zählerstände liegen müssen (Format: YYYY-MM-DD).
     * @param date2Str (Optional) Das Enddatum für den Zeitraum, in dem die Zählerstände liegen müssen (Format: YYYY-MM-DD).
     * @param meterStr (Optional) Der Zählertyp, dessen Zählerstände abgerufen werden sollen (HEIZUNG, STROM, UNBEKANNT, WASSER).
     * @return Eine HTTP-Antwort mit einer Liste von Zählerständen im JSON-Format, die den Suchkriterien entsprechen.
     * Die Antwort enthält ein JSON-Objekt mit dem Schlüssel "readings", dessen Wert die Liste der Zählerstände ist.
     * @throws WebApplicationException mit folgenden möglichen Status-Codes:
     *                                 200 (OK) - Wenn die Zählerstände erfolgreich abgerufen wurden.
     *                                 400 (Bad Request) - Wenn das Datumsformat ungültig ist oder ein ungültiger Zählertyp angegeben wurde.
     *                                 500 (Internal Server Error) - Bei unerwarteten Fehlern während der Verarbeitung.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response specificReadingGet(
            @QueryParam("customer") String customer,
            @QueryParam("start") String date1Str,
            @QueryParam("end") String date2Str,
            @QueryParam("kindOfMeter") String meterStr
    ) {
        IReading.KindOfMeter meter;
        LocalDate date1 = parseLocalDate(date1Str);
        LocalDate date2 = parseLocalDate(date2Str);
        if (date1Str != null && date1 == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"status\":\"error\",\"message\":\"Start date format has to be YYYY-MM-DD!\"}")
                    .build();
        }
        if (date2Str != null && date2 == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"status\":\"error\",\"message\":\"End date format has to be YYYY-MM-DD!\"}")
                    .build();
        }
        if (date1 == null && date2 == null) {
            date2 = LocalDate.now();
        }

        try {
            if (meterStr == null) {
                meter = IReading.KindOfMeter.UNBEKANNT;
            } else {
                meter = IReading.KindOfMeter.valueOf(meterStr.toUpperCase());
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"status\":\"error\",\"message\":\"Invalid kindOfMeter parameter. " +
                            "Allowed values are: HEIZUNG, STROM, UNBEKANNT and WASSER\"}")
                    .build();
        }

        List<Reading> readings = readingDao.getAllReadings(meter);
        List<Reading> finalReadings = new ArrayList<>();
        for (int i = 0; i < readings.size(); i++) {
            Reading reading = readings.get(i);
            if (!Objects.equals(reading.getCustomer().getId().toString(), customer) && customer != null) {
                continue;
            }
            if (validDate(date1, date2, reading.getDateOfReading())) {
                finalReadings.add(reading);
            }
        }

        try {
            List<Customer> customers = customerDao.getAllCustomers();

            // Einbetten der Kundenliste in ein JSON-Objekt mit dem Schlüssel "readings"
            Map<String, Object> response = new HashMap<>();
            response.put("readings", finalReadings);

            return Response.ok(response).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Ein interner Serverfehler ist aufgetreten\"}")
                    .build();
        }
    }

    /**
     * Löscht eine Ablesung anhand ihrer eindeutigen UUID.
     *
     * @param uuid Die UUID der zu löschenden Ablesung.
     * @return Eine HTTP-Antwort, die den Erfolg oder Misserfolg der Löschung angibt.
     * @throws WebApplicationException mit folgenden möglichen Status-Codes:
     *                                 200 (OK) - Wenn die Ablesung erfolgreich gelöscht wurde.
     *                                 404 (Not Found) - Wenn keine Ablesung mit der angegebenen UUID existiert.
     *                                 500 (Internal Server Error) - Bei unerwarteten Fehlern während der Verarbeitung.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readingDelete(@PathParam("id") String uuid) {
        try {
            Reading existingReading = findReadingByUuid(uuid);

            if (existingReading == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"status\":\"error\",\"message\":\"Ablesung nicht gefunden\"}")
                        .build();
            }

            readingDao.deleteReading(existingReading.getKindOfMeter(), uuid);
            return Response.status(Response.Status.OK).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"status\":\"error\",\"message\":\"Fehler beim Löschen der Ablesung\"}")
                    .build();
        }
    }

    private boolean validDate(LocalDate date1, LocalDate date2, LocalDate dateToCheck) {
        if (date1 == null) {
            return !dateToCheck.isAfter(date2);
        }
        if (date2 == null) {
            return !dateToCheck.isBefore(date1);
        }
        return (dateToCheck.isEqual(date1) || dateToCheck.isAfter(date1)) &&
                (dateToCheck.isEqual(date2) || dateToCheck.isBefore(date2));
    }

    /**
     * Hilfsmethode zum Parsen eines LocalDate aus einem String.
     *
     * @param dateStr Der zu parsende String im Format YYYY-MM-DD.
     * @return Das geparste LocalDate-Objekt oder null, wenn der String null oder ungültig ist.
     */
    private LocalDate parseLocalDate(String dateStr) {
        try {
            return (dateStr != null) ? LocalDate.parse(dateStr) : null;
        } catch (DateTimeParseException e) {
            return null; // Falls das Datum ungültig ist, wird null zurückgegeben
        }
    }

    /**
     * Sucht eine Ablesung anhand ihrer UUID in allen Zählertypen.
     *
     * @param uuid Die UUID der Ablesung.
     * @return Die gefundene Ablesung oder null, wenn keine Ablesung mit der UUID gefunden wurde.
     */
    private Reading findReadingByUuid(String uuid) {
        // Durchlaufe alle vorhandenen Zählertypen
        for (IReading.KindOfMeter kind : IReading.KindOfMeter.values()) {
            // Überspringe 'UNBEKANNT' falls gewünscht
            if (kind == IReading.KindOfMeter.UNBEKANNT) continue;

            try {
                IReading reading = readingDao.getReadingById(kind, uuid);

                if (reading instanceof Reading) {
                    return (Reading) reading;
                }
            } catch (Exception e) {
                // Logge Fehler, aber versuche nächsten Zählertyp
                System.err.println("Fehler bei Suche in " + kind + ": " + e.getMessage());
            }
        }
        return null; // Kein Treffer in allen Kategorien
    }
}