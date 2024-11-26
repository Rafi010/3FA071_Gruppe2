package dev.hv.rest;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class Server {

    private static HttpServer server;

    public static void startServer(String url) {
        if (server == null) {
            ResourceConfig resourceConfig = new ResourceConfig().packages("dev.hv.rest.ResourceEnpoints"); // Adjust the package path
            server = GrizzlyHttpServerFactory.createHttpServer(URI.create(url), resourceConfig);
            System.out.println("Server started at: " + url);
        }
    }

    public static void stopServer() {
    if (server != null) {
        server.shutdown();
        System.out.println("Server stopped.");
        }
    }
}
