package dev.hv.rest.ResourceEndpoints;


import dev.hv.projectFiles.DAO.entities.Reading;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/readings")
public class ReadingResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response readingsPost(Reading readingData){
        if (readingData.getDateOfReading() == null || readingData.getComment() == null /*|| readingData.getCustomer() == null*/ || readingData.getId() == null || readingData.getSubstitute() == null || readingData.getKindOfMeter() == null || readingData.getMeterCount() == null || readingData.getMeterId() == null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("incomplete data")
                    .build();
        }
        return Response.status(Response.Status.CREATED)
                .entity(readingData.getDateOfReading() + " " + readingData.getComment() + " " +  readingData.getCustomer() + " " + readingData.getId() + " " + readingData.getSubstitute() + " " + readingData.getKindOfMeter() + " " + readingData.getMeterCount() + " " + readingData.getMeterId())
                .build();
    }
}
