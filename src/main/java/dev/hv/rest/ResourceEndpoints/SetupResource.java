package dev.hv.rest.ResourceEndpoints;

import dev.hv.projectFiles.DatabaseConnection;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;

@Path("/setupDB")
public class SetupResource {

    DatabaseConnection connection = DatabaseConnection.getInstance();

    @DELETE
    public Response setupDB(){
        // Tabellen erstellen und mit Daten füllen
        connection.removeAllTables();
        connection.createAllTables();
        // daten in DB laden
        connection.fillDatabase();
        try {
            connection.getConnection().setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Response.status(Response.Status.OK)
                .entity("Database setup complete.")
                .build();
    }
}
