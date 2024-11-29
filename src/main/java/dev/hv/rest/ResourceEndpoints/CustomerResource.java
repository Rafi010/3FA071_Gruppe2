package dev.hv.rest.ResourceEndpoints;

import dev.hv.projectFiles.DAO.entities.User;
import jakarta.ws.rs.*;
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
    //@Path("/test")
    public Response sayHello(User userData) {
        //Validierung der Daten
        if (userData.getGender() == null || userData.getBirthDate() == null || userData.getLastName() == null || userData.getFirstName() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("incomplete data")
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity(userData.getLastName() + ", " + userData.getFirstName() + " " + userData.getGender() + " " + userData.getBirthDate())
                .build();

    }
}
