package dev.hv.rest.ResourceEndpoints;

import dev.hv.model.ICustomer;
import dev.hv.projectFiles.DAO.daoImplementation.CustomerDaoImpl;
import dev.hv.projectFiles.DAO.entities.User;
import dev.hv.projectFiles.DatabaseConnection;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.Properties;


@Path("/customers")
public class CustomerResource {

    public CustomerResource() throws SQLException {
        DatabaseConnection connection = DatabaseConnection.getInstance();
    }


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Customer Page";
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response customerPost(@Valid User user) {

        // Simulierte Verarbeitung
        String responseMessage = String.format(
                "{\"message\":\"Customer created: %s %s (%s) - Birthdate: %s, ID: %s\"}",
                user.getFirstName(),
                user.getLastName(),
                user.getGender(),
                user.getBirthDate() != null ? user.getBirthDate().toString() : "N/A",
                user.getId() != null ? user.getId().toString() : "N/A"
        );

        return Response.status(Response.Status.CREATED)
                .entity(responseMessage)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response customerPut(@PathParam("id") String uuid, @Valid User user) {
        try {
            // Abrufen des existierenden Benutzers
            User existingUser = findUserByUuid(uuid);

            if (existingUser == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Kunde nicht gefunden\"}")
                        .build();
            }

            // Überschreiben der Werte des existierenden Benutzers
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setGender(user.getGender());
            existingUser.setBirthDate(user.getBirthDate());

            // Aktualisieren des Benutzers in der Datenbank
            updateUser(existingUser);

            // Erfolgreiche Antwort
            String responseMessage = String.format("{\"message\": \"Kunde erfolgreich aktualisiert\", \"customer\": {\"id\": \"%s\", \"firstName\": \"%s\", \"lastName\": \"%s\"}}",
                    existingUser.getId(),
                    existingUser.getFirstName(),
                    existingUser.getLastName());
            return Response.status(Response.Status.OK)
                    .entity(responseMessage)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Fehler beim Aktualisieren des Kunden\"}")
                    .build();
        }
    }

    private User findUserByUuid(String uuid) {
        try {
            DatabaseConnection connection = DatabaseConnection.getInstance();
            CustomerDaoImpl customerDao = new CustomerDaoImpl(connection.getConnection());
            ICustomer customer = customerDao.getUserById(uuid);
            if (customer instanceof User) {
                return (User) customer;
            } else if (customer == null) {
                return null;
            } else {
                throw new RuntimeException("Unexpected customer type returned");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding user", e);
        }
    }

    private void updateUser(User user) {
        try {
            DatabaseConnection connection = DatabaseConnection.getInstance();
            CustomerDaoImpl customerDao = new CustomerDaoImpl(connection.getConnection());
            customerDao.updateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating user", e);
        }
    }

}
