package dev.hv.rest.ResourceEnpoints;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("")
public class RootResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Server is running!";
    }
}
