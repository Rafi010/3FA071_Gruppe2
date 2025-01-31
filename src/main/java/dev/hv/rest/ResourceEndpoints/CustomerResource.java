package dev.hv.rest.ResourceEndpoints;

import dev.hv.model.ICustomer;
import dev.hv.projectFiles.DAO.daoImplementation.CustomerDaoImpl;
import dev.hv.projectFiles.DAO.entities.Customer;
import dev.hv.projectFiles.DatabaseConnection;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;


@Path("/customers")
public class CustomerResource {

    DatabaseConnection connection = DatabaseConnection.getInstance();
    CustomerDaoImpl customerDao = new CustomerDaoImpl(connection.getConnection());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response customerGetAll() {
        List<Customer> customers = customerDao.getAllCustomers();
        return Response.status(Response.Status.OK)
                .entity(customers)
                .build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response customerGetById(@PathParam("id") String uuid) {
        try {
            // Abrufen des existierenden Benutzers
            Customer existingCustomer = findCustomerByUuid(uuid);

            if (existingCustomer == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"status\":\"error\",\"message\":\"Fehler: Kunde nicht gefunden\"}")
                        .build();
            }

            return Response.status(Response.Status.OK)
                    .entity(existingCustomer)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"status\":\"error\",\"message\":\"Fehler beim Abrufen des Kunden\"}")
                    .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response customerPost(@Valid Customer customer) {

        if (customer.getId() == null) {
            customer.setId(UUID.randomUUID());
        }
        customerDao.addCustomer(customer);

        String responseMessage = String.format(
                "{\"message\":\"Customer created: %s %s (%s) - Birthdate: %s, ID: %s\"}",
                customer.getFirstName(),
                customer.getLastName(),
                customer.getGender(),
                customer.getBirthDate() != null ? customer.getBirthDate().toString() : "N/A",
                customer.getId() != null ? customer.getId().toString() : "N/A"
        );

        return Response.status(Response.Status.CREATED)
                .entity(responseMessage)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response customerPut(@PathParam("id") String uuid, @Valid Customer customer) {
        try {
            // Abrufen des existierenden Benutzers
            Customer existingCustomer = findCustomerByUuid(uuid);

            if (existingCustomer == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Fehler: Kunde nicht gefunden")
                        .build();
            }

            // Überschreiben der Werte des existierenden Benutzers
            existingCustomer.setFirstName(customer.getFirstName());
            existingCustomer.setLastName(customer.getLastName());
            existingCustomer.setGender(customer.getGender());
            existingCustomer.setBirthDate(customer.getBirthDate());

            // Aktualisieren des Benutzers in der Datenbank
            updateCustomer(existingCustomer);

            // Erfolgreiche Antwort als Plain Text
            String responseMessage = String.format("Kunde erfolgreich aktualisiert - ID: %s, Name: %s %s",
                    existingCustomer.getId(),
                    existingCustomer.getFirstName(),
                    existingCustomer.getLastName());
            return Response.status(Response.Status.OK)
                    .entity(responseMessage)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Fehler beim Aktualisieren des Kunden")
                    .build();
        }
    }

    private Customer findCustomerByUuid(String uuid) {
        try {
            ICustomer customer = customerDao.getCustomerById(uuid);
            if (customer instanceof Customer) {
                return (Customer) customer;
            } else if (customer == null) {
                return null;
            } else {
                throw new RuntimeException("Unexpected customer type returned");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding customer", e);
        }
    }

    private void updateCustomer(Customer customer) {
        try {
            customerDao.updateCustomer(customer);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating customer", e);
        }
    }


    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response customerDelete(@PathParam("id") String uuid) {
        try {
            // Abrufen des existierenden Benutzers
            Customer existingCustomer = findCustomerByUuid(uuid);

            if (existingCustomer == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"status\":\"error\",\"message\":\"Fehler: Kunde nicht gefunden\"}")
                        .build();
            }
            customerDao.deleteCustomer(uuid);

            return Response.status(Response.Status.OK)
                    .entity("{\"message\":\"Kunde entfernt.\"}")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"status\":\"error\",\"message\":\"Fehler beim Entfernen des Kunden\"}")
                    .build();
        }
    } //TODO SET TO NULL??


}
