package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import java.util.ArrayList;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import be.vinci.pae.domain.adresses.Address;
import be.vinci.pae.domain.adresses.AddressFactory;
import be.vinci.pae.domain.users.User;
import be.vinci.pae.domain.users.UserDTO;
import be.vinci.pae.domain.users.UserFactory;
import be.vinci.pae.domain.users.UserUCC;
import be.vinci.pae.exceptions.DataException;
import be.vinci.pae.exceptions.LoginException;
import be.vinci.pae.exceptions.RegisterException;
import be.vinci.pae.exceptions.UserNotFoundException;
import be.vinci.pae.services.AddressDAO;
import be.vinci.pae.services.DalServices;
import be.vinci.pae.services.UserDAO;
import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.ApplicationBinderTest;

class UserUCCImplTest {

  private static UserUCC userUCC;
  private static UserFactory userFactory;
  private static UserDAO userDAO;
  private static AddressDAO addressDAO;
  private static DalServices dalServices;
  private static AddressFactory addressFactory;
  private UserDTO user;
  private User validUser;
  private Address validAddress;

  private static final String pseudo = "devPseudo";
  private static final String password = "dev";
  private static final String email = "dev@email.com";
  private static final String idUser = "userId";

  @BeforeAll
  static void before() {
    ServiceLocator locator =
        ServiceLocatorUtilities.bind(new ApplicationBinder(), new ApplicationBinderTest());

    userDAO = locator.getService(UserDAO.class);
    addressDAO = locator.getService(AddressDAO.class);
    userUCC = locator.getService(UserUCC.class);
    userFactory = locator.getService(UserFactory.class);
    addressFactory = locator.getService(AddressFactory.class);
    dalServices = locator.getService(DalServices.class);
  }

  @BeforeEach
  void setUp() throws Exception {
    this.user = null;

    this.validUser = (User) userFactory.getUser();
    this.validUser.setID(idUser);
    this.validUser.setPseudo(pseudo);
    this.validUser.setPassword(validUser.hashPassword(password));
    this.validUser.setEmail(email);

    this.validAddress = addressFactory.getAddress();
    this.validAddress.setId(1);
    this.validAddress.setCountry("Belgium");
    this.validAddress.setMunicipality("Bruxelles");
    this.validAddress.setStreet("Street");
    this.validAddress.setNumber("1");
    this.validAddress.setPostalCode("1490");
    this.validAddress.setBox("2b");

    Mockito.reset(userDAO);
    Mockito.reset(addressDAO);
    Mockito.reset(dalServices);
  }

  /*
   * LOGIN TESTS
   */

  @Test
  @DisplayName("Login Success")
  void loginSuccess_Test() {
    Mockito.when(userDAO.getUserByPseudo(pseudo)).thenReturn(validUser);
    user = userUCC.login(pseudo, password);
    assertEquals(pseudo, user.getPseudo());
    Mockito.verify(userDAO).getUserByPseudo(pseudo);
  }

  @DisplayName("LoginFail - mauvais pseudo")
  @Test
  void loginFail_WrongPseudo_UserNotFoundException_Test() {
    Mockito.when(userDAO.getUserByPseudo("charlieChaplin")).thenReturn(null);
    assertNull(userUCC.login("charlieChaplin", password));
    Mockito.verify(userDAO).getUserByPseudo("charlieChaplin");
  }

  @Test
  @DisplayName("LoginFail - mauvais mot de passe")
  void loginFail_WrongPassword_UserNotFoundException_Test() {
    Mockito.when(userDAO.getUserByPseudo(pseudo)).thenReturn(validUser);
    assertNull(userUCC.login(pseudo, "wrongPassword"));
    Mockito.verify(userDAO).getUserByPseudo(pseudo);
  }


  @Test
  @DisplayName("LoginFail - pseudo null")
  void loginFail_NullPseudo_Test() {
    assertNull(userUCC.login(null, password));
  }


  @Test
  @DisplayName("LoginFail - mot de passe null")
  void loginFail_NullPassword_Test() {
    assertNull(userUCC.login(pseudo, null));
  }


  @Test
  @DisplayName("LoginFail - pseudo vide")
  void loginFail_EmptyPseudo_Test() {
    Mockito.when(userDAO.getUserByPseudo("")).thenReturn(null);
    assertNull(userUCC.login("", password));
    Mockito.verify(userDAO).getUserByPseudo("");
  }


  @Test
  @DisplayName("LoginFail - mot de passe vide")
  void loginFail_EmptyPassword_Test() {
    Mockito.when(userDAO.getUserByPseudo(pseudo)).thenReturn(validUser);
    assertNull(userUCC.login(pseudo, ""));
    Mockito.verify(userDAO).getUserByPseudo(pseudo);
  }

  @Test
  @DisplayName("LoginFail - user pas trouve en BD")
  void loginFail_UserNotFound_Test() {
    Mockito.when(userDAO.getUserByPseudo(pseudo)).thenThrow(new DataException("user not found"));
    assertThrows(LoginException.class, () -> userUCC.login(pseudo, password));
    Mockito.verify(userDAO).getUserByPseudo(pseudo);
  }


  /*
   * REGISTER TESTS
   */

  @Test
  @DisplayName("Register Success")
  void registerSuccess() {
    Mockito.when(addressDAO.addAdress(validAddress)).thenReturn(validAddress);
    Mockito.when(userDAO.addUser(validUser)).thenReturn(validUser);
    user = userUCC.register(validUser, validAddress);
    assertEquals(pseudo, user.getPseudo());
    assertEquals(1, user.getAddress());
    Mockito.verify(addressDAO).addAdress(validAddress);
    Mockito.verify(userDAO).addUser(validUser);
  }

  @Test
  @DisplayName("RegisterFail - Erreur ajout adresse")
  void registerFail_addingAddress() {
    Mockito.when(addressDAO.addAdress(validAddress)).thenReturn(null);
    assertThrows(RegisterException.class, () -> userUCC.register(validUser, validAddress));
    Mockito.verify(addressDAO).addAdress(validAddress);
  }

  @Test
  @DisplayName("RegisterFail - Erreur ajout adresse")
  void registerFail_addingUser() {
    Mockito.when(addressDAO.addAdress(validAddress)).thenReturn(validAddress);
    Mockito.when(userDAO.addUser(validUser)).thenReturn(null);
    assertThrows(RegisterException.class, () -> userUCC.register(validUser, validAddress));
    Mockito.verify(addressDAO).addAdress(validAddress);
    Mockito.verify(userDAO).addUser(validUser);
  }


  /*
   * checkEmailFormat
   */

  @Test
  @DisplayName("checkEmailFormat Success")
  void checkEmailFormat_success() {
    assertTrue(userUCC.checkEmailFormat("valid@email.com"));
  }

  @Test
  @DisplayName("checkEmailFormat Fail - null email")
  void checkEmailFormat_fail_null() {
    assertFalse(userUCC.checkEmailFormat(null));
  }

  @Test
  @DisplayName("checkEmailFormat Fail - wrong format 1")
  void checkEmailFormat_fail_wrongFormat1() {
    assertFalse(userUCC.checkEmailFormat("wrongEmailFormat"));
  }

  @Test
  @DisplayName("checkEmailFormat Fail - wrong format 2")
  void checkEmailFormat_fail_wrongFormat2() {
    assertFalse(userUCC.checkEmailFormat(""));
  }

  @Test
  @DisplayName("checkEmailFormat Fail - wrong format 3")
  void checkEmailFormat_fail_wrongFormat3() {
    assertFalse(userUCC.checkEmailFormat("123"));
  }

  @Test
  @DisplayName("checkEmailFormat Fail - wrong format 4")
  void checkEmailFormat_fail_wrongFormat4() {
    assertFalse(userUCC.checkEmailFormat("wrongEmailFormat"));
  }

  @Test
  @DisplayName("checkEmailFormat Fail - wrong format 5")
  void checkEmailFormat_fail_wrongFormat5() {
    assertFalse(userUCC.checkEmailFormat("wrong@email"));
  }


  /*
   * checkIfEmailExist
   */

  @Test
  @DisplayName("checkIfEmailExist Success - email exists")
  void checkIfEmailExist_success_exists() {
    Mockito.when(userDAO.getUserByEmail(email)).thenReturn(validUser);
    assertTrue(userUCC.checkIfEmailExist(email));
    Mockito.verify(userDAO).getUserByEmail(email);
  }

  @Test
  @DisplayName("checkIfEmailExist Success - email not exists")
  void checkIfEmailExist_success_notExists() {
    Mockito.when(userDAO.getUserByEmail(email)).thenReturn(null);
    assertFalse(userUCC.checkIfEmailExist(email));
    Mockito.verify(userDAO).getUserByEmail(email);
  }

  @Test
  @DisplayName("checkIfEmailExist Fail - wrong email format")
  void checkIfEmailExist_fail_wrongEmailFormat() {
    assertFalse(userUCC.checkIfEmailExist("wrongEmailFormat"));
  }

  @Test
  @DisplayName("checkIfEmailExist Fail - DAL Service error")
  void checkIfEmailExist_fail_dalServiceError() {
    Mockito.when(userDAO.getUserByEmail("random@email.com"))
        .thenThrow(new IllegalArgumentException());
    assertFalse(userUCC.checkIfEmailExist("random@email.com"));
    Mockito.verify(userDAO).getUserByEmail("random@email.com");
  }

  /*
   * checkIfUserExists
   */

  @Test
  @DisplayName("checkIfUserExists Success - pseudo already used")
  void checkIfUserExist_success_pseudoUsed() {
    Mockito.when(userDAO.getUserByPseudo(pseudo)).thenReturn(validUser);
    assertTrue(userUCC.checkIfUserExist(pseudo));
    Mockito.verify(userDAO).getUserByPseudo(pseudo);
  }

  @Test
  @DisplayName("checkIfUserExists Success - pseudo not used")
  void checkIfUserExist_success_pseudoNotUsed() {
    Mockito.when(userDAO.getUserByPseudo(pseudo)).thenReturn(null);
    assertFalse(userUCC.checkIfUserExist(pseudo));
    Mockito.verify(userDAO).getUserByPseudo(pseudo);
  }

  @Test
  @DisplayName("checkIfUserExist Fail - Exception was thrown")
  void checkIfUserExist_fail_exceptionThrown() {
    doThrow(new IllegalArgumentException()).when(dalServices).startTransaction();
    assertFalse(userUCC.checkIfUserExist(pseudo));
    Mockito.verify(dalServices).rollbackTransaction();
  }

  /*
   * getAllUser
   */

  @Test
  @DisplayName("getAllUser emptyList")
  void getAllUser_emptyList() {
    Mockito.when(userDAO.getAllUsers()).thenReturn(new ArrayList<UserDTO>());
    ArrayList<UserDTO> userList = userUCC.getAllUser();
    assertEquals(0, userList.size());
    Mockito.verify(userDAO).getAllUsers();
  }

  @Test
  @DisplayName("getAllUser listWith1User")
  void getAllUser_listWith1User() {
    ArrayList<UserDTO> mockList = new ArrayList<UserDTO>();
    mockList.add(validUser);
    Mockito.when(userDAO.getAllUsers()).thenReturn(mockList);
    ArrayList<UserDTO> userList = userUCC.getAllUser();
    assertEquals(1, userList.size());
    assertEquals(pseudo, userList.get(0).getPseudo());
    Mockito.verify(userDAO).getAllUsers();
  }

  @Test
  @DisplayName("getAllUser Fail - DAL Service error")
  void getAllUser_fail_dalServiceError() {
    Mockito.when(userDAO.getAllUsers()).thenThrow(new DataException());
    assertThrows(UserNotFoundException.class, () -> userUCC.getAllUser());;
    Mockito.verify(userDAO).getAllUsers();
  }

  /*
   * getUserById
   */
  @Test
  @DisplayName("getUserById Success - get User")
  void getUserById_success() {
    Mockito.when(userDAO.getUserById(idUser)).thenReturn(validUser);
    assertEquals(validUser, userUCC.getUserById(idUser));
    Mockito.verify(userDAO).getUserById(idUser);
  }

  @Test
  @DisplayName("getUserById Success - user not exists")
  void getUserById_success_notExists() {
    Mockito.when(userDAO.getUserById(idUser)).thenReturn(null);
    assertNotEquals(validUser, userUCC.getUserById(idUser));
    Mockito.verify(userDAO).getUserById(idUser);
  }

  @Test
  @DisplayName("getUserById Fail - DAL Service error")
  void getUserById_fail_dalServiceError() {
    Mockito.when(userDAO.getUserById(idUser)).thenThrow(new DataException("user not found"));
    assertThrows(UserNotFoundException.class, () -> userUCC.getUserById(idUser));
    Mockito.verify(userDAO).getUserById(idUser);
  }

  /*
   * getUserByUsername
   */
  @Test
  @DisplayName("getUserByUsername Success - get User")
  void getUserByUsername_success() {
    Mockito.when(userDAO.getUserByPseudo(pseudo)).thenReturn(validUser);
    assertEquals(validUser, userUCC.getUserByUsername(pseudo));
    Mockito.verify(userDAO).getUserByPseudo(pseudo);
  }

  @Test
  @DisplayName("getUserByUsername Success - user not exists")
  void getUserByUsername_success_notExists() {
    Mockito.when(userDAO.getUserByPseudo(pseudo)).thenReturn(null);
    assertNotEquals(validUser, userUCC.getUserByUsername(pseudo));
    Mockito.verify(userDAO).getUserByPseudo(pseudo);
  }

  @Test
  @DisplayName("getUserByUsername Fail - DAL Service error")
  void getUserByUsername_fail_dalServiceError() {
    Mockito.when(userDAO.getUserByPseudo(pseudo)).thenThrow(new DataException("user not found"));
    assertThrows(UserNotFoundException.class, () -> userUCC.getUserByUsername(pseudo));
    Mockito.verify(userDAO).getUserByPseudo(pseudo);
  }

  @Test
  @DisplayName("getAllUserDesc")
  void getAllUserDesc_success() {
    ArrayList<UserDTO> returnedList = new ArrayList<UserDTO>();
    returnedList.add(validUser);
    Mockito.when(userDAO.getAllUsersDesc()).thenReturn(returnedList);
    ArrayList<UserDTO> list = userUCC.getAllUserDesc();
    assertEquals(1, list.size());
    assertEquals(validUser, list.get(0));

    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(userDAO).getAllUsersDesc();
    Mockito.verify(dalServices).commitTransaction();
  }

  @Test
  @DisplayName("getAllUserDesc_success_emptyList")
  void getAllUserDesc_success_emptyList() {
    Mockito.when(userDAO.getAllUsersDesc()).thenReturn(new ArrayList<UserDTO>());
    assertEquals(0, userUCC.getAllUserDesc().size());

    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(userDAO).getAllUsersDesc();
    Mockito.verify(dalServices).commitTransaction();
  }

  @Test
  @DisplayName("getAllUserDesc_fail_startTransaction")
  void getAllUserDesc_fail_startTransaction() {
    doThrow(new IllegalArgumentException()).when(dalServices).startTransaction();
    assertThrows(UserNotFoundException.class, () -> userUCC.getAllUserDesc());

    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(dalServices).rollbackTransaction();
  }
}
