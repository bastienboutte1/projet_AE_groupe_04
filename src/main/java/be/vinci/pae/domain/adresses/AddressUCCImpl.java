package be.vinci.pae.domain.adresses;

import be.vinci.pae.services.AddressDAO;
import be.vinci.pae.services.DalServices;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

public class AddressUCCImpl implements AddressUCC {

  @Inject
  private DalServices dalServices;

  @Inject
  private AddressDAO addressDataService;

  @Override
  public Address getAddressById(int id) {
    if (id <= 0) {
      throw new WebApplicationException("getAddressById : invalid ID");
    }
    Address address = null;
    try {
      dalServices.startTransaction();
      address = this.addressDataService.getAddressById(id);
      dalServices.commitTransaction();
      return address;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw new WebApplicationException("getAddressById : error with try catch");
    }
  }

}
