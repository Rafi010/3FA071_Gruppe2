package dev.hv.rest.ResourceEndpoints;


import dev.hv.projectFiles.DAO.daoImplementation.CustomerDaoImpl;
import dev.hv.projectFiles.DAO.daoImplementation.ReadingDaoImpl;
import dev.hv.projectFiles.DAO.daoInterfaces.CustomerDao;
import dev.hv.projectFiles.DAO.daoInterfaces.ReadingDao;
import dev.hv.projectFiles.DAO.entities.Reading;
import dev.hv.projectFiles.DAO.entities.Customer;
import dev.hv.projectFiles.DatabaseConnection;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.UUID;

@Path("/readings")
public class ReadingResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response readingsPost(@Valid Reading readingData) throws SQLException {

        DatabaseConnection connection = DatabaseConnection.getInstance();

        CustomerDao<Customer> customerDao = new CustomerDaoImpl(connection.getConnection());

        ReadingDao<Reading> readingDao = new ReadingDaoImpl(connection.getConnection());



        if (readingData.getId() == null) {
            readingData.setId(UUID.randomUUID());
        }

        Customer customer = (Customer)readingData.getCustomer();
        System.out.println(customerDao.getCustomerById("e6124ae9-a0fb-4a3a-bfcb-e9e017c874f5"));
        System.out.println(customerDao.getCustomerById(customer.getId().toString()));

        if (customerDao.getCustomerById(customer.getId().toString()) == null) {
            customerDao.addCustomer(customer);
        }

        readingDao.addReading(readingData);




        return Response.status(Response.Status.CREATED)
                .entity(readingData)
                .build();

    }
}
