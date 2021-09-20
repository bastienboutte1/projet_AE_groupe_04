package be.vinci.pae.utils;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.mockito.Mockito;
import be.vinci.pae.services.AddressDAO;
import be.vinci.pae.services.DalServices;
import be.vinci.pae.services.FurnitureDAO;
import be.vinci.pae.services.PhotoDAO;
import be.vinci.pae.services.SaleDAO;
import be.vinci.pae.services.TypeFurnitureDAO;
import be.vinci.pae.services.UserDAO;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ApplicationBinderTest extends AbstractBinder {

  private UserDAO userDAO;
  private AddressDAO addressDAO;
  private FurnitureDAO furnitureDAO;
  private SaleDAO saleDAO;
  private PhotoDAO photoDAO;
  private TypeFurnitureDAO typeFurnitureDAO;
  private DalServices dalServices;

  /**
   * constructor for ApplicationBinder.
   */
  public ApplicationBinderTest() {
    this.userDAO = Mockito.mock(UserDAO.class);
    this.addressDAO = Mockito.mock(AddressDAO.class);
    this.dalServices = Mockito.mock(DalServices.class);
    this.furnitureDAO = Mockito.mock(FurnitureDAO.class);
    this.saleDAO = Mockito.mock(SaleDAO.class);
    this.photoDAO = Mockito.mock(PhotoDAO.class);
    this.typeFurnitureDAO = Mockito.mock(TypeFurnitureDAO.class);
  }

  @Override
  protected void configure() {
    bind(userDAO).to(UserDAO.class).ranked(2);
    bind(addressDAO).to(AddressDAO.class).ranked(2);
    bind(furnitureDAO).to(FurnitureDAO.class).ranked(2);
    bind(photoDAO).to(PhotoDAO.class).ranked(2);
    bind(saleDAO).to(SaleDAO.class).ranked(2);
    bind(typeFurnitureDAO).to(TypeFurnitureDAO.class).ranked(2);

    bind(dalServices).to(DalServices.class).ranked(2);
  }
}
