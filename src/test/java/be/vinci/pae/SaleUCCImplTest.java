package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.ArrayList;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import be.vinci.pae.domain.furnitures.Furniture;
import be.vinci.pae.domain.furnitures.FurnitureFactory;
import be.vinci.pae.domain.sales.Sale;
import be.vinci.pae.domain.sales.SaleDTO;
import be.vinci.pae.domain.sales.SaleFactory;
import be.vinci.pae.domain.sales.SaleUCC;
import be.vinci.pae.domain.users.User;
import be.vinci.pae.domain.users.UserFactory;
import be.vinci.pae.exceptions.SaleException;
import be.vinci.pae.services.DalServices;
import be.vinci.pae.services.FurnitureDAO;
import be.vinci.pae.services.SaleDAO;
import be.vinci.pae.services.UserDAO;
import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.ApplicationBinderTest;

class SaleUCCImplTest {

  private static SaleUCC saleUCC;
  private static SaleFactory saleFactory;
  private static SaleDAO saleDAO;
  private static UserDAO userDAO;
  private static UserFactory userFactory;
  private static FurnitureDAO furnitureDAO;
  private static FurnitureFactory furnitureFactory;
  private static DalServices dalServices;
  private Sale validSale;
  private Sale saleAntRefu;
  private Sale saleCliRefu;
  private User userAntiquaire;
  private User userClient;
  private Furniture furniturePropose;
  private Furniture furnitureRefuse;
  private Furniture furnitureAchete;

  private static final int idSale = 1;
  private static final String idUserAntiquare = "antiquaire";
  private static final String idUserClient = "client";
  private static final int idFurniturePropose = 1;
  private static final String conditionPropose = "propose";
  private static final int idFurnitureRefuse = 2;
  private static final String conditionRefuse = "refuse";
  private static final int idFurnitureAchete = 3;
  private static final String conditionAchete = "achete";


  @BeforeAll
  static void before() {
    ServiceLocator locator =
        ServiceLocatorUtilities.bind(new ApplicationBinder(), new ApplicationBinderTest());
    saleDAO = locator.getService(SaleDAO.class);
    saleUCC = locator.getService(SaleUCC.class);
    saleFactory = locator.getService(SaleFactory.class);
    userDAO = locator.getService(UserDAO.class);
    userFactory = locator.getService(UserFactory.class);
    furnitureDAO = locator.getService(FurnitureDAO.class);
    furnitureFactory = locator.getService(FurnitureFactory.class);
    dalServices = locator.getService(DalServices.class);
  }

  @BeforeEach
  void setUp() throws Exception {

    this.validSale = (Sale) saleFactory.getSale();
    this.validSale.setIdSale(idSale);
    this.validSale.setIdUser(idUserAntiquare);
    this.validSale.setIdFurniture(idFurniturePropose);

    this.saleAntRefu = (Sale) saleFactory.getSale();
    this.saleAntRefu.setIdSale(idSale);
    this.saleAntRefu.setIdUser(idUserAntiquare);
    this.saleAntRefu.setIdFurniture(idFurnitureRefuse);

    this.saleCliRefu = (Sale) saleFactory.getSale();
    this.saleCliRefu.setIdSale(idSale);
    this.saleCliRefu.setIdUser(idUserClient);
    this.saleCliRefu.setIdFurniture(idFurnitureRefuse);

    this.userAntiquaire = (User) userFactory.getUser();
    this.userAntiquaire.setID(idUserAntiquare);
    this.userAntiquaire.setRole(idUserAntiquare);

    this.userClient = (User) userFactory.getUser();
    this.userClient.setID(idUserClient);
    this.userClient.setRole(idUserClient);

    this.furnitureAchete = (Furniture) furnitureFactory.getFurniture();
    this.furnitureAchete.setIdFurniture(idFurnitureAchete);
    this.furnitureAchete.setCondition(conditionAchete);

    this.furniturePropose = (Furniture) furnitureFactory.getFurniture();
    this.furniturePropose.setIdFurniture(idFurniturePropose);
    this.furniturePropose.setCondition(conditionPropose);

    this.furnitureRefuse = (Furniture) furnitureFactory.getFurniture();
    this.furnitureRefuse.setIdFurniture(idFurnitureRefuse);
    this.furnitureRefuse.setCondition(conditionRefuse);

    Mockito.reset(saleDAO);
    Mockito.reset(furnitureDAO);
    Mockito.reset(userDAO);
    Mockito.reset(dalServices);
  }

  /*
   * addSale.
   */
  @Test
  @DisplayName("addSale - succes")
  void addSale_succes() {
    Mockito.when(furnitureDAO.getFurniture(idFurniturePropose)).thenReturn(furniturePropose);
    Mockito.when(userDAO.getUserById(idUserAntiquare)).thenReturn(userAntiquaire);
    Mockito.when(saleDAO.addSale(validSale)).thenReturn(validSale);
    assertEquals(validSale, saleUCC.addSale(validSale));
    Mockito.verify(saleDAO).addSale(validSale);
  }

  @Test
  @DisplayName("addSale - errorAntiquaire ")
  void addSale_error_antiquaire_refuse() {
    Mockito.when(furnitureDAO.getFurniture(idFurnitureRefuse)).thenReturn(furnitureRefuse);
    Mockito.when(userDAO.getUserById(idUserAntiquare)).thenReturn(userAntiquaire);
    assertThrows(SaleException.class, () -> saleUCC.addSale(saleAntRefu));
  }

  @Test
  @DisplayName("addSale - errorClient")
  void addSale_error_client_refuse() {
    Mockito.when(furnitureDAO.getFurniture(idFurnitureRefuse)).thenReturn(furnitureRefuse);
    Mockito.when(userDAO.getUserById(idUserClient)).thenReturn(userClient);
    assertThrows(SaleException.class, () -> saleUCC.addSale(saleCliRefu));
  }

  @Test
  @DisplayName("addSale - dalservice Error")
  void addSale_fail_dalService() {
    Mockito.when(furnitureDAO.getFurniture(idFurniturePropose)).thenReturn(furniturePropose);
    Mockito.when(userDAO.getUserById(idUserAntiquare)).thenReturn(userAntiquaire);
    Mockito.when(saleDAO.addSale(validSale)).thenThrow(new IllegalArgumentException());
    assertThrows(SaleException.class, () -> saleUCC.addSale(validSale));
    Mockito.verify(saleDAO).addSale(validSale);
  }

  @Test
  @DisplayName("checkSale - dalservice Error")
  void checkSale_dalService_fail() {
    Mockito.when(furnitureDAO.getFurniture(idFurniturePropose)).thenReturn(furniturePropose);
    Mockito.when(userDAO.getUserById(idUserAntiquare)).thenReturn(userAntiquaire);
    Mockito.when(saleDAO.getSaleById(idSale)).thenThrow(new IllegalArgumentException());
    assertThrows(SaleException.class, () -> saleUCC.checkSale(idSale));
    Mockito.verify(saleDAO).getSaleById(idSale);
  }

  @Test
  @DisplayName("addSale - sale Already exist")
  void addSale_exist() {
    Mockito.when(saleDAO.getSaleById(idFurniturePropose)).thenReturn(validSale);
    Mockito.when(furnitureDAO.getFurniture(idFurniturePropose)).thenReturn(furniturePropose);
    Mockito.when(userDAO.getUserById(idUserAntiquare)).thenReturn(userAntiquaire);
    Mockito.when(saleDAO.addSale(validSale)).thenThrow(new IllegalArgumentException());
    assertThrows(SaleException.class, () -> saleUCC.addSale(validSale));
  }

  /*
   * getAllSalesForUserById.
   */
  @Test
  @DisplayName("getAllSalesForUserById emptyList")
  void getAllSalesForUserById_emptyList() {
    Mockito.when(saleDAO.getAllSalesForUserById(idUserAntiquare))
        .thenReturn(new ArrayList<SaleDTO>());
    ArrayList<SaleDTO> saleList = saleUCC.getAllSalesForUserById(idUserAntiquare);
    assertEquals(0, saleList.size());
    Mockito.verify(saleDAO).getAllSalesForUserById(idUserAntiquare);
  }

  @Test
  @DisplayName("getAllSalesForUserById list1Sale")
  void getAllSalesForUserById_list_1_sale() {
    ArrayList<SaleDTO> mockList = new ArrayList<SaleDTO>();
    mockList.add(validSale);
    Mockito.when(saleDAO.getAllSalesForUserById(idUserAntiquare)).thenReturn(mockList);
    ArrayList<SaleDTO> saleList = saleUCC.getAllSalesForUserById(idUserAntiquare);
    assertEquals(1, saleList.size());
    Mockito.verify(saleDAO).getAllSalesForUserById(idUserAntiquare);
  }

  @Test
  @DisplayName("getAllSalesForUserById Fail - DalService")
  void getAllSalesForUserById_fail_dalService() {
    Mockito.when(saleDAO.getAllSalesForUserById(idUserAntiquare))
        .thenThrow(new IllegalArgumentException());
    assertThrows(SaleException.class, () -> saleUCC.getAllSalesForUserById(idUserAntiquare));
    Mockito.verify(saleDAO).getAllSalesForUserById(idUserAntiquare);
  }

  /*
   * getSaleByFurnitureId
   */
  @Test
  @DisplayName("getSaleByFurnitureId - succes")
  void getFurnitureById_succes() {
    Mockito.when(saleDAO.getSaleById(idFurniturePropose)).thenReturn(validSale);
    assertEquals(validSale, saleUCC.getSaleByFurnitureId(idFurniturePropose));
    Mockito.verify(saleDAO).getSaleById(idFurniturePropose);
  }

  @Test
  @DisplayName("getSaleByFurnitureId Fail - DAL Service error")
  void getFurnitureById_fail_dalServiceError() {
    Mockito.when(saleDAO.getSaleById(idFurniturePropose)).thenThrow(new IllegalArgumentException());
    assertThrows(SaleException.class, () -> saleUCC.getSaleByFurnitureId(idFurniturePropose));
    Mockito.verify(saleDAO).getSaleById(idFurniturePropose);
  }



}
