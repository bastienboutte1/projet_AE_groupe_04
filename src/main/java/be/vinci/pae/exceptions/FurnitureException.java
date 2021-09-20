package be.vinci.pae.exceptions;

import jakarta.ws.rs.WebApplicationException;

public class FurnitureException extends WebApplicationException {

  private static final long serialVersionUID = 1L;

  /*
   * FurnitureException
   */
  public FurnitureException() {
    super();
  }

  /*
   * FurnitureException
   */
  public FurnitureException(String message) {
    super(message);
  }

  /*
   * FurnitureException
   */
  public FurnitureException(Throwable cause) {
    super(cause);
  }

  /*
   * FurnitureException
   */
  public FurnitureException(String message, Throwable cause) {
    super(message, cause);
  }
}
