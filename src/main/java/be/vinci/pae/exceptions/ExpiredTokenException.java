package be.vinci.pae.exceptions;

import jakarta.ws.rs.WebApplicationException;

public class ExpiredTokenException extends WebApplicationException {

  private static final long serialVersionUID = 1L;

  /**
   * ExpiredTokenException.
   */
  public ExpiredTokenException() {
    super();
  }

  /**
   * ExpiredTokenException.
   */
  public ExpiredTokenException(String message) {
    super(message);
  }

  /**
   * ExpiredTokenException.
   */
  public ExpiredTokenException(Throwable cause) {
    super(cause);
  }

  /**
   * ExpiredTokenException.
   */
  public ExpiredTokenException(String message, Throwable cause) {
    super(message, cause);
  }
}
