package be.vinci.pae.main;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.MyLogger;

/**
 * Main class.
 *
 */
public class Main {

  /**
   * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
   * 
   * @return Grizzly HTTP server.
   */
  public static HttpServer startServer() {
    // Create a resource config that scans for JAX-RS resources and providers
    final ResourceConfig rc = new ResourceConfig().packages("be.vinci.pae.api")
        // .packages("org.glassfish.jersey.examples.jackson")
        .register(JacksonFeature.class).register(ApplicationBinder.class)
        .property("jersey.config.server.wadl.disableWadl", true);

    // Create and start a new instance of grizzly http server
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(Config.getProperty("BaseUri")), rc);
  }

  /**
   * Main method.
   * 
   * @param args arguments
   * @throws IOException if file dev.properties not found
   */
  public static void main(String[] args) throws IOException {
    // Load properties file
    Config.load("dev.properties");
    // Start the server
    final HttpServer server = startServer();
    try {
      MyLogger.setup();
    } catch (IOException e) {
      throw new RuntimeException("Problems with creating the log files");
    }
    System.out.println("Jersey app started at " + Config.getProperty("BaseUri"));
    // Listen to key press and shutdown server
    System.out.println("Hit enter to stop it...");
    System.in.read();
    server.shutdownNow();
  }

}
