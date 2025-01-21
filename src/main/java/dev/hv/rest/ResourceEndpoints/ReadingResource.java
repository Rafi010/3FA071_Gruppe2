package dev.hv.rest.ResourceEndpoints;


import dev.hv.projectFiles.DAO.entities.Reading;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/readings")
public class ReadingResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response readingsPost(@Valid Reading readingData){

        //TODO remove "!= null" statements after testing
        String responseMessage = String.format(
                "{\"message\":\"Reading created - Date: %s, Comment: %s, Customer %s, Substitute: %s, " +
                        "KindOfMeter %s, MeterCount: %s, MeterId: %s, ID: %s\"}",
                readingData.getDateOfReading() != null ? readingData.getDateOfReading().toString() : "N/A",
                readingData.getComment() != null ? readingData.getComment() : "N/A",
                readingData.getCustomer() != null ? readingData.getCustomer().toString() : "N/A", //TODO process customer!!!
                readingData.getSubstitute() != null ? readingData.getSubstitute().toString() : "N/A",
                readingData.getKindOfMeter() != null ? readingData.getKindOfMeter().toString() : "N/A",
                readingData.getMeterCount() != null ? readingData.getMeterCount().toString() : "N/A",
                readingData.getMeterId() != null ? readingData.getMeterId() : "N/A",
                readingData.getId() != null ? readingData.getId().toString() : "N/A"
        );


        return Response.status(Response.Status.CREATED)
                .entity(responseMessage)
                .build();

    }
}
