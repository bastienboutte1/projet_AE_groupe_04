package be.vinci.pae.domain.adresses;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = AddressImpl.class)
public interface Address {
  int getId();

  void setId(int id);

  String getStreet();

  void setStreet(String street);

  String getNumber();

  void setNumber(String number);

  String getBox();

  void setBox(String box);


  String getPostalCode();

  void setPostalCode(String postalCode);

  String getMunicipality();

  void setMunicipality(String municipality);

  String getCountry();

  void setCountry(String country);

}
