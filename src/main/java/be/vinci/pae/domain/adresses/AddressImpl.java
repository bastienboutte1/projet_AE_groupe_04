package be.vinci.pae.domain.adresses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import views.Views;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressImpl implements Address {

  @JsonView(Views.Public.class)
  private int id;

  @JsonView(Views.Public.class)
  private String street;

  @JsonView(Views.Public.class)
  private String number;

  @JsonView(Views.Public.class)
  private String box;

  @JsonView(Views.Public.class)
  private String postalCode;

  @JsonView(Views.Public.class)
  private String municipality;

  @JsonView(Views.Public.class)
  private String country;

  /*
   * METHODS GETTERS & SETTERS
   */

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getStreet() {
    return street;
  }

  @Override
  public void setStreet(String street) {
    this.street = street;
  }

  @Override
  public String getNumber() {
    return number;
  }

  @Override
  public void setNumber(String number) {
    this.number = number;
  }

  @Override
  public String getBox() {
    return box;
  }

  @Override
  public void setBox(String box) {
    this.box = box;
  }

  @Override
  public String getPostalCode() {
    return postalCode;
  }

  @Override
  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  @Override
  public String getMunicipality() {
    return municipality;
  }

  @Override
  public void setMunicipality(String municipality) {
    this.municipality = municipality;
  }

  @Override
  public String getCountry() {
    return country;
  }

  @Override
  public void setCountry(String country) {
    this.country = country;
  }

}
