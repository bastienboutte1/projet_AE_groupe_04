package be.vinci.pae.exceptions;

import jakarta.ws.rs.WebApplicationException;

public class DataException extends WebApplicationException {

  private static final long serialVersionUID = 1L;

  /*
   * DataException.
   */
  public DataException() {
    super();
  }

  /*
   * DataException.
   */
  public DataException(String message) {
    super(message);
  }

  /*
   * DataException.
   */
  public DataException(Throwable cause) {
    super(cause);
  }

  /*
   * DataException.
   */
  public DataException(String message, Throwable cause) {
    super(message, cause);
  }
}
