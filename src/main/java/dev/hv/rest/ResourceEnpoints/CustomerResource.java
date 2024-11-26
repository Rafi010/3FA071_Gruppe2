package dev.hv.rest.ResourceEnpoints;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/customer")
public class CustomerResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Customer Page";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/sayHello")
    public String sayHello(){
        System.out.println("hello");
        return "hello";
    }

}
