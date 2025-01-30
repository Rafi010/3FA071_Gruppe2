package dev.hv.rest.ResourceEndpoints;


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

        try {
            readingDao.updateReading(reading);
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.CREATED)
                .entity(reading)
                .build();
    }
}
