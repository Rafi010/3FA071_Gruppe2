package dev.hv.rest.ResourceEndpoints;

import dev.hv.projectFiles.DatabaseConnection;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/setupDB")
public class SetupResource {

    @DELETE
    public Response setupDB(){

        DatabaseConnection connection = DatabaseConnection.getInstance();

        // Tabellen erstellen und mit Daten füllen
        connection.removeAllTables();
        connection.createAllTables();
        // daten in DB laden
        connection.fillDatabase();



        return Response.status(Response.Status.CREATED)
                .entity("Database setup complete.")
                .build();
    }
}
