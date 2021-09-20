package be.vinci.pae.exceptions;

import jakarta.ws.rs.WebApplicationException;

public class WrongEmailFormatException extends WebApplicationException {

  private static final long serialVersionUID = 1L;

  /**
   * WrongEmailFormatException.
   */
  public WrongEmailFormatException() {
    super();
  }

  /**
   * WrongEmailFormatException.
   */
  public WrongEmailFormatException(String message) {
    super(message);
  }

  /**
   * WrongEmailFormatException.
   */
  public WrongEmailFormatException(Throwable cause) {
    super(cause);
  }

  /**
   * WrongEmailFormatException.
   */
  public WrongEmailFormatException(String message, Throwable cause) {
    super(message, cause);
  }
}
