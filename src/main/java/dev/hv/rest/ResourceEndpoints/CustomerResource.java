package dev.hv.rest.ResourceEndpoints;

import dev.hv.projectFiles.DAO.entities.User;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/customers")
public class CustomerResource {

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
}
