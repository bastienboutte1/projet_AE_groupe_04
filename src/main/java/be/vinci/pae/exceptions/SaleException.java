package be.vinci.pae.exceptions;

import jakarta.ws.rs.WebApplicationException;

public class SaleException extends WebApplicationException {

  private static final long serialVersionUID = 1L;

  /*
   * SaleException
   */
  public SaleException() {
    super();
  }

  /*
   * SaleException
   */
  public SaleException(String message) {
    super(message);
  }

  /*
   * SaleException
   */
  public SaleException(Throwable cause) {
    super(cause);
  }

  /*
   * SaleException
   */
  public SaleException(String message, Throwable cause) {
    super(message, cause);
  }
}
