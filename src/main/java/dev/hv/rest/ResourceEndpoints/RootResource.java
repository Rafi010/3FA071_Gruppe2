package dev.hv.rest.ResourceEndpoints;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Das ist ein Test
 * Bei Aufrufen von "Localhost:8080 wird im optimalfall "Server is running!" ausgegeben
 */
@Path("")
public class RootResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Server is running!";
    }
}
