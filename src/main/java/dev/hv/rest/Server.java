package dev.hv.rest;

import com.fasterxml.jackson.core.util.JacksonFeature;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class Server {

    // Instanz des HTTP-Servers
    private static HttpServer server;

    /**
     * Startet den HTTP-Server.
     *
     * @param url Die URL, auf welcher der Server erreichbar sein soll (z. B. "<a href="http://localhost:8080">localhost</a>").
     */
    public static void startServer(String url) {
        if (server == null) {
            // Konfiguration der verfügbaren Ressourcen-Endpunkte
            ResourceConfig resourceConfig = new ResourceConfig()
                    .packages("dev.hv.rest.ResourceEndpoints")
                    .register(JacksonFeature.class);

            // Initialisiert und startet den Grizzly HTTP-Server
            server = GrizzlyHttpServerFactory.createHttpServer(URI.create(url), resourceConfig);

            // Ausgabe einer Bestätigungsmeldung
            System.out.println("Server started at: " + url);
        } else {
            System.out.println("Server is already running.");
        }
    }

    /**
     * Stoppt den laufenden Server.
     */
    public static void stopServer() {
        if (server != null) {
            // Beendet den Server und gibt Ressourcen frei
            server.shutdown();

            // Ausgabe einer Bestätigungsmeldung
            System.out.println("Server stopped.");
        } else {
            System.out.println("Server is not running.");
        }
    }
}
