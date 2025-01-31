package dev.hv.rest.ResourceEndpoints;


import dev.hv.model.IReading;
import dev.hv.projectFiles.DAO.daoImplementation.CustomerDaoImpl;
import dev.hv.projectFiles.DAO.daoImplementation.ReadingDaoImpl;
import dev.hv.projectFiles.DAO.daoInterfaces.CustomerDao;
import dev.hv.projectFiles.DAO.daoInterfaces.ReadingDao;
import dev.hv.projectFiles.DAO.entities.Reading;
import dev.hv.projectFiles.DAO.entities.Customer;
import dev.hv.projectFiles.DatabaseConnection;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Path("/readings")
public class ReadingResource {

    DatabaseConnection connection = DatabaseConnection.getInstance();
    CustomerDao<Customer> customerDao = new CustomerDaoImpl(connection.getConnection());
    ReadingDao<Reading> readingDao = new ReadingDaoImpl(connection.getConnection());

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response readingsPost(@Valid Reading reading){

        //add UUID if not existent
        if (reading.getId() == null) {
            reading.setId(UUID.randomUUID());
        }

        Customer customer = (Customer)reading.getCustomer();

        //add customer of reading to DB if not already present
        if (customerDao.getCustomerById(customer.getId().toString()) == null) {
            customerDao.addCustomer(customer);
        }

        readingDao.addReading(reading);

        return Response.status(Response.Status.CREATED)
                .entity(reading)
                .build();

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response readingPut(@Valid Reading reading){
        if (reading.getId() == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (readingDao.getReadingById(reading.getKindOfMeter(), reading.getId().toString()) == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            readingDao.updateReading(reading);
            return Response.status(Response.Status.CREATED)
                    .entity(reading)
                    .build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response specificReadingGet(
            @QueryParam("uuid") String uuid,
            @QueryParam("start") String date1Str,
            @QueryParam("end") String date2Str,
            @QueryParam("kindOfMeter") IReading.KindOfMeter metre
    ) {

        LocalDate date1 = parseLocalDate(date1Str);
        LocalDate date2 = parseLocalDate(date2Str);

        if (date1 == null && date2 == null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new Reading())
                    .build();
        }
        if (metre == null) {
            metre = IReading.KindOfMeter.UNBEKANNT;
        }
        List<Reading> readings = readingDao.getAllReadings(metre);
        List<Reading> finalReadings = new ArrayList<>();
        for (int i = 0; i < readings.size(); i++) {
            Reading reading = readings.get(i);
            if (!Objects.equals(reading.getCustomer().getId().toString(), uuid)) {
                continue;
            }
            if (validDate(date1, date2, reading.getDateOfReading())) {
                finalReadings.add(reading);
            }
        }


        return Response.status(Response.Status.OK)
                .entity(finalReadings)
                .build();
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

    private LocalDate parseLocalDate(String dateStr) {
        try {
            return (dateStr != null) ? LocalDate.parse(dateStr) : null;
        } catch (DateTimeParseException e) {
            return null; // Falls das Datum ungültig ist, wird null zurückgegeben
        }
    }
}
