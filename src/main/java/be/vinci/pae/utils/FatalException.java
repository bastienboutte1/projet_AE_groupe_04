package be.vinci.pae.utils;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public class FatalException extends WebApplicationException {

  private static final long serialVersionUID = -5926196101906096391L;

  /**
   * FatalException.
   */
  public FatalException(Throwable cause) {
    super(cause, Response.status(Status.INTERNAL_SERVER_ERROR).build());
  }

  /**
   * FatalException.
   */
  public FatalException(String message, Throwable cause) {
    super(cause,
        Response.status(Status.INTERNAL_SERVER_ERROR).entity(message).type("text/plain").build());
  }

  /**
   * FatalException.
   */
  public FatalException(String message) {
    super(Response.status(Status.INTERNAL_SERVER_ERROR).entity(message).type("text/plain").build());
  }

}
