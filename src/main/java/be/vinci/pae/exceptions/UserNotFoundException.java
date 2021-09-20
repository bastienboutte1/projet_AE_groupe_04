package be.vinci.pae.exceptions;

import jakarta.ws.rs.WebApplicationException;

public class UserNotFoundException extends WebApplicationException {

  private static final long serialVersionUID = 1L;

  /**
   * UserNotFoundException.
   */
  public UserNotFoundException() {
    super();
  }

  /**
   * UserNotFoundException.
   */
  public UserNotFoundException(String message) {
    super(message);
  }

  /**
   * UserNotFoundException.
   */
  public UserNotFoundException(Throwable cause) {
    super(cause);
  }

  /**
   * UserNotFoundException.
   */
  public UserNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
