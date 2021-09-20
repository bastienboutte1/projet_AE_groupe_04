package be.vinci.pae.exceptions;

import jakarta.ws.rs.WebApplicationException;

public class UpdateFurnitureException extends WebApplicationException {

  private static final long serialVersionUID = 1L;

  /*
   * UpdateFurnitureException
   */
  public UpdateFurnitureException() {
    super();
  }

  /*
   * UpdateFurnitureException
   */
  public UpdateFurnitureException(String message) {
    super(message);
  }

  /*
   * UpdateFurnitureException
   */
  public UpdateFurnitureException(Throwable cause) {
    super(cause);
  }

  /*
   * UpdateFurnitureException
   */
  public UpdateFurnitureException(String message, Throwable cause) {
    super(message, cause);
  }
}
