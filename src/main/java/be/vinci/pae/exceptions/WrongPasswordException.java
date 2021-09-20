package be.vinci.pae.exceptions;

import jakarta.ws.rs.WebApplicationException;

public class WrongPasswordException extends WebApplicationException {

  private static final long serialVersionUID = 1L;

  /**
   * WrongPasswordException.
   */
  public WrongPasswordException() {
    super();
  }

  /**
   * WrongPasswordException.
   */
  public WrongPasswordException(String message) {
    super(message);
  }

  /**
   * WrongPasswordException.
   */
  public WrongPasswordException(Throwable cause) {
    super(cause);
  }

  /**
   * WrongPasswordException.
   */
  public WrongPasswordException(String message, Throwable cause) {
    super(message, cause);
  }
}
