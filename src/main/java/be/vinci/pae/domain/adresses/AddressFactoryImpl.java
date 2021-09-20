package be.vinci.pae.domain.adresses;

public class AddressFactoryImpl implements AddressFactory {

  @Override
  public Address getAddress() {
    return new AddressImpl();
  }
}
