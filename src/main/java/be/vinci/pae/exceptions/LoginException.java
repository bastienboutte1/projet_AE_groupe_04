package be.vinci.pae.exceptions;

import jakarta.ws.rs.WebApplicationException;

public class LoginException extends WebApplicationException {

  private static final long serialVersionUID = 1L;

  /*
   * LoginException
   */
  public LoginException() {
    super();
  }

  /*
   * LoginException
   */
  public LoginException(String message) {
    super(message);
  }

  /*
   * LoginException
   */
  public LoginException(Throwable cause) {
    super(cause);
  }

  /*
   * LoginException
   */
  public LoginException(String message, Throwable cause) {
    super(message, cause);
  }

}
