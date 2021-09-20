package be.vinci.pae.exceptions;

import jakarta.ws.rs.WebApplicationException;

public class RegisterException extends WebApplicationException {

  private static final long serialVersionUID = 1L;

  /*
   * RegisterException
   */
  public RegisterException() {
    super();
  }

  /*
   * RegisterException
   */
  public RegisterException(String message) {
    super(message);
  }

  /*
   * RegisterException
   */
  public RegisterException(Throwable cause) {
    super(cause);
  }

  /*
   * RegisterException
   */
  public RegisterException(String message, Throwable cause) {
    super(message, cause);
  }

}
