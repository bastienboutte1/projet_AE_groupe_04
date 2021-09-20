package be.vinci.pae.domain.users;

public class UserFactoryImpl implements UserFactory {

  @Override
  public UserDTO getUser() {
    return new UserImpl();
  }

}
