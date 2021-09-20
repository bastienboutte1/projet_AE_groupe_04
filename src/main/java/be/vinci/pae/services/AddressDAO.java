package be.vinci.pae.services;

import be.vinci.pae.domain.adresses.Address;

public interface AddressDAO {

  Address addAdress(Address adress);

  Address getAddressById(int id);
}
