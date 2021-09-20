package be.vinci.pae.domain.users;

import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import be.vinci.pae.domain.adresses.Address;
import be.vinci.pae.exceptions.LoginException;
import be.vinci.pae.exceptions.RegisterException;
import be.vinci.pae.exceptions.UserNotFoundException;
import be.vinci.pae.services.AddressDAO;
import be.vinci.pae.services.DalServices;
import be.vinci.pae.services.UserDAO;
import jakarta.inject.Inject;

public class UserUCCImpl implements UserUCC {

  @Inject
  private UserDAO dataService;

  @Inject
  private AddressDAO addressDataService;

  @Inject
  private DalServices dalServices;

  @Override
  public UserDTO register(UserDTO user, Address address) {
    Address newAddress = null;
    try {
      dalServices.startTransaction();
      newAddress = this.addressDataService.addAdress(address);
      if (newAddress == null) {
        throw new RegisterException("Adress was not created");
      }
      user.setAddress(newAddress.getId());
      user.setID(UUID.randomUUID().toString());
      user.setPassword(((User) user).hashPassword(user.getPassword()));
      user.setRole("client");
      user = this.dataService.addUser(user);
      if (user == null) {
        throw new RegisterException("User was not created");
      }
      dalServices.commitTransaction();
      return user;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw new RegisterException("User was not created");
    }
  }

  @Override
  public UserDTO login(String pseudo, String password) {
    if (pseudo == null || password == null) {
      return null;
    }
    User user = null;
    try {
      dalServices.startTransaction();
      user = (User) dataService.getUserByPseudo(pseudo);
      dalServices.commitTransaction();
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw new LoginException("Error getUserByPseudo");
    }
    if (user == null || !user.checkPassword(password)) {
      return null;
    }
    return user;
  }

  /**
   * check if the user exist.
   * 
   * @return true if user already exist, return false if user not exist.
   */
  @Override
  public boolean checkIfUserExist(String pseudo) {
    boolean bool = false;
    try {
      dalServices.startTransaction();
      bool = dataService.getUserByPseudo(pseudo) != null;
      dalServices.commitTransaction();
    } catch (Exception e) {
      dalServices.rollbackTransaction();
    }
    return bool;
  }

  @Override
  public boolean checkIfEmailExist(String email) throws IllegalArgumentException {
    if (!checkEmailFormat(email)) {
      return false;
    }
    boolean bool = false;
    try {
      dalServices.startTransaction();
      bool = dataService.getUserByEmail(email) != null;
      dalServices.commitTransaction();
    } catch (Exception e) {
      dalServices.rollbackTransaction();
    }
    return bool;
  }

  /**
   * Source : https://mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/.
   */
  @Override
  public boolean checkEmailFormat(String email) {
    if (email == null) {
      return false;
    }
    final String EMAIL_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }

  @Override
  public UserDTO getUserByUsername(String username) {
    try {
      dalServices.startTransaction();
      UserDTO user = dataService.getUserByPseudo(username);
      return user;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw new UserNotFoundException("getUserByUsername");
    }
  }

  @Override
  public UserDTO getUserById(String id) {
    try {
      dalServices.startTransaction();
      UserDTO user = dataService.getUserById(id);
      return user;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw new UserNotFoundException("getUserById");
    }
  }

  @Override
  public ArrayList<UserDTO> getAllUser() {
    try {
      dalServices.startTransaction();
      ArrayList<UserDTO> userList = dataService.getAllUsers();
      dalServices.commitTransaction();
      return userList;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw new UserNotFoundException("getAllUser");
    }
  }

  @Override
  public ArrayList<UserDTO> getAllUserDesc() {
    try {
      dalServices.startTransaction();
      ArrayList<UserDTO> userList = dataService.getAllUsersDesc();
      dalServices.commitTransaction();
      return userList;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw new UserNotFoundException("getAllUserDesc");
    }
  }
}
