package be.vinci.pae.api.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import be.vinci.pae.exceptions.DataException;
import be.vinci.pae.exceptions.ExpiredTokenException;
import be.vinci.pae.exceptions.FurnitureException;
import be.vinci.pae.exceptions.LoginException;
import be.vinci.pae.exceptions.RegisterException;
import be.vinci.pae.exceptions.UpdateFurnitureException;
import be.vinci.pae.exceptions.UserNotFoundException;
import be.vinci.pae.exceptions.WrongEmailFormatException;
import be.vinci.pae.exceptions.WrongPasswordException;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.FatalException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class LoggingExceptionMapper implements ExceptionMapper<Throwable> {

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  @Override
  public Response toResponse(Throwable exception) {
    exception.printStackTrace();

    if (exception instanceof UserNotFoundException) {
      final Response response = ((UserNotFoundException) exception).getResponse();
      Response.Status.Family family = response.getStatusInfo().getFamily();
      if (family.equals(Response.Status.Family.REDIRECTION)) {
        return response;
      }
      if (family.equals(Response.Status.Family.SERVER_ERROR)) {
        severeLogger(exception, "User not found Exception");
      }
      return Response.status(getStatusCode(exception)).entity(exception.getMessage()).build();
    }

    if (exception instanceof WrongEmailFormatException) {
      final Response response = ((WrongEmailFormatException) exception).getResponse();
      Response.Status.Family family = response.getStatusInfo().getFamily();
      if (family.equals(Response.Status.Family.REDIRECTION)) {
        return response;
      }
      if (family.equals(Response.Status.Family.SERVER_ERROR)) {
        severeLogger(exception, "Wrong email format Exception");
      }
      return Response.status(getStatusCode(exception)).entity(exception.getMessage()).build();
    }

    if (exception instanceof WrongPasswordException) {
      final Response response = ((WrongPasswordException) exception).getResponse();
      Response.Status.Family family = response.getStatusInfo().getFamily();
      if (family.equals(Response.Status.Family.REDIRECTION)) {
        return response;
      }
      if (family.equals(Response.Status.Family.SERVER_ERROR)) {
        severeLogger(exception, "Wrong password Exception");
      }
      return Response.status(getStatusCode(exception)).entity(exception.getMessage()).build();
    }

    if (exception instanceof ExpiredTokenException) {
      final Response response = ((ExpiredTokenException) exception).getResponse();
      Response.Status.Family family = response.getStatusInfo().getFamily();
      if (family.equals(Response.Status.Family.REDIRECTION)) {
        return response;
      }
      if (family.equals(Response.Status.Family.SERVER_ERROR)) {
        severeLogger(exception, "Expired Token Exception");
      }
      return Response.status(getStatusCode(exception)).entity(exception.getMessage()).build();
    }

    if (exception instanceof LoginException) {
      final Response response = ((LoginException) exception).getResponse();
      Response.Status.Family family = response.getStatusInfo().getFamily();
      if (family.equals(Response.Status.Family.REDIRECTION)) {
        return response;
      }
      if (family.equals(Response.Status.Family.SERVER_ERROR)) {
        severeLogger(exception, "Login Exception");
      }
      return Response.status(getStatusCode(exception)).entity(exception.getMessage()).build();
    }

    if (exception instanceof RegisterException) {
      final Response response = ((RegisterException) exception).getResponse();
      Response.Status.Family family = response.getStatusInfo().getFamily();
      if (family.equals(Response.Status.Family.REDIRECTION)) {
        return response;
      }
      if (family.equals(Response.Status.Family.SERVER_ERROR)) {
        severeLogger(exception, "Register Exception");
      }
      return Response.status(getStatusCode(exception)).entity(exception.getMessage()).build();
    }

    if (exception instanceof UpdateFurnitureException) {
      final Response response = ((UpdateFurnitureException) exception).getResponse();
      Response.Status.Family family = response.getStatusInfo().getFamily();
      if (family.equals(Response.Status.Family.REDIRECTION)) {
        return response;
      }
      if (family.equals(Response.Status.Family.SERVER_ERROR)) {
        severeLogger(exception, "Update Furniture Exception");
      }
      return Response.status(getStatusCode(exception)).entity(exception.getMessage()).build();
    }

    if (exception instanceof FatalException) {
      final Response response = ((FatalException) exception).getResponse();
      Response.Status.Family family = response.getStatusInfo().getFamily();
      if (family.equals(Response.Status.Family.REDIRECTION)) {
        return response;
      }
      if (family.equals(Response.Status.Family.SERVER_ERROR)) {
        severeLogger(exception, "Fatal Exception");
      }
      return Response.status(getStatusCode(exception)).entity(exception.getMessage()).build();
    }

    if (exception instanceof DataException) {
      final Response response = ((DataException) exception).getResponse();
      Response.Status.Family family = response.getStatusInfo().getFamily();
      if (family.equals(Response.Status.Family.REDIRECTION)) {
        return response;
      }
      if (family.equals(Response.Status.Family.SERVER_ERROR)) {
        severeLogger(exception, "Exception with the data services");
      }
      return Response.status(getStatusCode(exception)).entity(exception.getMessage()).build();
    }

    if (exception instanceof FurnitureException) {
      final Response response = ((FurnitureException) exception).getResponse();
      Response.Status.Family family = response.getStatusInfo().getFamily();
      if (family.equals(Response.Status.Family.REDIRECTION)) {
        return response;
      }
      if (family.equals(Response.Status.Family.SERVER_ERROR)) {
        severeLogger(exception, "Exception with the furnitures");
      }
      return Response.status(getStatusCode(exception)).entity(exception.getMessage()).build();
    }

    if (Config.getBoolProperty("SendStackTraceToClient")) {
      return Response.status(getStatusCode(exception)).entity(getEntity(exception)).build();
    }
    return Response.status(getStatusCode(exception)).entity(exception.getMessage()).build();
  }

  /*
   * Get appropriate HTTP status code for an exception.
   */
  private int getStatusCode(Throwable exception) {
    if (exception instanceof WebApplicationException) {
      return ((WebApplicationException) exception).getResponse().getStatus();
    }
    return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
  }

  /*
   * Get response body for an exception.
   */
  private Object getEntity(Throwable exception) {
    StringWriter errorMsg = new StringWriter();
    exception.printStackTrace(new PrintWriter(errorMsg));
    return errorMsg.toString();
  }

  /*
   * Logger for severe exceptions
   */
  private void severeLogger(Throwable e, String name) {
    LOGGER.setLevel(Level.SEVERE);
    LOGGER.severe(e.getStackTrace().toString() + "\n---------------------------------------------"
        + "------------------------------------------------- \n");
    LOGGER.severe(
        name + " : \nMessage : " + e.getMessage() + "\n--------------------------------------"
            + "-------------------------------------------------------- \n");
  }

}
