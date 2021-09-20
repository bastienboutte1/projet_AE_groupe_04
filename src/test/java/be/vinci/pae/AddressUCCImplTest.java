package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import be.vinci.pae.domain.adresses.Address;
import be.vinci.pae.domain.adresses.AddressFactory;
import be.vinci.pae.domain.adresses.AddressUCC;
import be.vinci.pae.services.AddressDAO;
import be.vinci.pae.services.DalServices;
import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.ApplicationBinderTest;
import jakarta.ws.rs.WebApplicationException;

class AddressUCCImplTest {

  private static AddressDAO addressDAO;
  private static DalServices dalServices;
  private static AddressFactory addressFactory;
  private static AddressUCC addressUCC;

  private Address validAddress;

  @BeforeAll
  static void before() {
    ServiceLocator locator =
        ServiceLocatorUtilities.bind(new ApplicationBinder(), new ApplicationBinderTest());

    addressDAO = locator.getService(AddressDAO.class);
    addressUCC = locator.getService(AddressUCC.class);
    addressFactory = locator.getService(AddressFactory.class);
    dalServices = locator.getService(DalServices.class);
  }

  @BeforeEach
  void setup() throws Exception {
    this.validAddress = addressFactory.getAddress();
    this.validAddress.setId(1);
    this.validAddress.setStreet("My street name");
    this.validAddress.setNumber("My number");
    this.validAddress.setBox("My box");
    this.validAddress.setCountry("My country");
    this.validAddress.setMunicipality("My municipality");

    Mockito.reset(addressDAO);
    Mockito.reset(dalServices);
  }

  @Test
  @DisplayName("Success getAddressById")
  void getAddressById_success() {
    Mockito.when(addressDAO.getAddressById(1)).thenReturn(validAddress);
    Address response = addressUCC.getAddressById(1);
    assertEquals("My street name", response.getStreet());
    Mockito.verify(addressDAO).getAddressById(1);
  }

  @Test
  @DisplayName("fail - id negatif")
  void getAddressByID_fail_negativeId() {
    assertThrows(WebApplicationException.class, () -> addressUCC.getAddressById(-1));
  }

  @Test
  @DisplayName("Fail - dalService error")
  void getAddressById_fail_dalServiceError() {
    doThrow(new IllegalArgumentException()).when(dalServices).startTransaction();
    assertThrows(WebApplicationException.class, () -> addressUCC.getAddressById(1));
    Mockito.verify(dalServices).rollbackTransaction();
  }
}
