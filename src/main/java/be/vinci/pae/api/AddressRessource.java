package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.adresses.Address;
import be.vinci.pae.domain.adresses.AddressUCC;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Singleton
@Path("/addresses")
public class AddressRessource {

  @Inject
  private AddressUCC addressUCC;

  /**
   * Get an address by his id.
   * 
   * @param id :adress_id
   * @return adress
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public Address getAddressById(@PathParam("id") int id) {
    if (id <= 0) {
      return null;
    }
    return this.addressUCC.getAddressById(id);
  }
}
