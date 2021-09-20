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
import be.vinci.pae.domain.furnitures.FurnitureDTO;
import be.vinci.pae.domain.furnitures.FurnitureFactory;
import be.vinci.pae.domain.furnitures.FurnitureUCC;
import be.vinci.pae.exceptions.FurnitureException;
import be.vinci.pae.services.DalServices;
import be.vinci.pae.services.FurnitureDAO;
import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.ApplicationBinderTest;

class FurnitureUCCImplTest {

  private static FurnitureUCC furnitureUCC;
  private static FurnitureFactory furnitureFactory;
  private static FurnitureDAO furnitureDAO;
  private static DalServices dalServices;
  private FurnitureDTO furniture;
  private Furniture validFurniture;
  private Furniture selledFurniture;
  private static final int idFurniture = 1;
  private static final int idFurnitureSelled = 2;
  private static final String description = "bought";
  private static final String condition = "achete";
  private static final String conditionSelled = "vendu";

  @BeforeAll
  static void before() {
    ServiceLocator locator =
        ServiceLocatorUtilities.bind(new ApplicationBinder(), new ApplicationBinderTest());
    furnitureUCC = locator.getService(FurnitureUCC.class);
    furnitureFactory = locator.getService(FurnitureFactory.class);
    dalServices = locator.getService(DalServices.class);
    furnitureDAO = locator.getService(FurnitureDAO.class);
  }

  @BeforeEach
  void setUp() throws Exception {
    this.furniture = null;

    this.validFurniture = (Furniture) furnitureFactory.getFurniture();
    this.validFurniture.setIdFurniture(idFurniture);
    this.validFurniture.setDescription(description);
    this.validFurniture.setCondition(condition);

    this.selledFurniture = (Furniture) furnitureFactory.getFurniture();
    this.selledFurniture.setIdFurniture(idFurnitureSelled);
    this.selledFurniture.setDescription(description);
    this.selledFurniture.setCondition(conditionSelled);

    Mockito.reset(furnitureDAO);
    Mockito.reset(dalServices);

  }

  /*
   * getAllFurniture.
   */
  @Test
  @DisplayName("getAllFurniture empltyList")
  void getAllFurniture_emptyList() {
    Mockito.when(furnitureDAO.getAllFurnitures()).thenReturn(new ArrayList<FurnitureDTO>());
    ArrayList<FurnitureDTO> furnitureList = furnitureUCC.showAllFurniture();
    assertEquals(0, furnitureList.size());
    Mockito.verify(furnitureDAO).getAllFurnitures();
  }

  @Test
  @DisplayName("getAllFurniture listWith1Furniture")
  void getAllFurniture_listWith1Furniture() {
    ArrayList<FurnitureDTO> mockList = new ArrayList<FurnitureDTO>();
    mockList.add(validFurniture);
    Mockito.when(furnitureDAO.getAllFurnitures()).thenReturn(mockList);
    ArrayList<FurnitureDTO> furnitureList = furnitureUCC.showAllFurniture();
    assertEquals(1, furnitureList.size());
    assertEquals(idFurniture, furnitureList.get(0).getIdFurniture());
    Mockito.verify(furnitureDAO).getAllFurnitures();
  }

  @Test
  @DisplayName("getAllFurniture Fail - DAL Service error")
  void getAllFurniture_fail_dalServiceError() {
    Mockito.when(furnitureDAO.getAllFurnitures()).thenThrow(new IllegalArgumentException());
    assertThrows(FurnitureException.class, () -> furnitureUCC.showAllFurniture());
    Mockito.verify(furnitureDAO).getAllFurnitures();
  }

  /*
   * updateFurniture.
   */
  @Test
  @DisplayName("updateFurniture - succes")
  void updateFurniture_succes() {
    Mockito.when(furnitureDAO.updateFurniture(validFurniture)).thenReturn(validFurniture);
    assertEquals(validFurniture, furnitureUCC.updateFurniture(validFurniture));
    Mockito.verify(furnitureDAO).updateFurniture(validFurniture);
  }

  @Test
  @DisplayName("updateFurniture Fail - DAL Service error")
  void updateFurniture_fail_dalServiceError() {
    Mockito.when(furnitureDAO.updateFurniture(validFurniture))
        .thenThrow(new IllegalArgumentException());
    assertThrows(FurnitureException.class, () -> furnitureUCC.updateFurniture(validFurniture));
    Mockito.verify(furnitureDAO).updateFurniture(validFurniture);
  }

  /*
   * getFurnitureById.
   */
  @Test
  @DisplayName("getFurnitureById - succes")
  void getFurnitureById_succes() {
    Mockito.when(furnitureDAO.getFurniture(idFurniture)).thenReturn(validFurniture);
    assertEquals(validFurniture, furnitureUCC.getFurniture(idFurniture));
    Mockito.verify(furnitureDAO).getFurniture(idFurniture);
  }

  @Test
  @DisplayName("getFurnitureById Fail - DAL Service error")
  void getFurnitureById_fail_dalServiceError() {
    Mockito.when(furnitureDAO.getFurniture(idFurniture)).thenThrow(new IllegalArgumentException());
    assertThrows(FurnitureException.class, () -> furnitureUCC.getFurniture(idFurniture));
    Mockito.verify(furnitureDAO).getFurniture(idFurniture);
  }

  /*
   * showAllSelledFurnitureDesc.
   */
  @Test
  @DisplayName("showAllSelledFurnitureDesc empltyList")
  void showAllSelledFurnitureDesc_emptyList() {
    Mockito.when(furnitureDAO.getAllSelledFurnituresDesc())
        .thenReturn(new ArrayList<FurnitureDTO>());
    ArrayList<FurnitureDTO> furnitureList = furnitureUCC.showAllSelledFurnitureDesc();
    assertEquals(0, furnitureList.size());
    Mockito.verify(furnitureDAO).getAllSelledFurnituresDesc();
  }

  @Test
  @DisplayName("showAllSelledFurnitureDesc listWith1Furniture")
  void showAllSelledFurnitureDesc_listWith1Furniture() {
    ArrayList<FurnitureDTO> mockList = new ArrayList<FurnitureDTO>();
    mockList.add(selledFurniture);
    Mockito.when(furnitureDAO.getAllSelledFurnituresDesc()).thenReturn(mockList);
    ArrayList<FurnitureDTO> furnitureList = furnitureUCC.showAllSelledFurnitureDesc();
    assertEquals(1, furnitureList.size());
    assertEquals(idFurnitureSelled, furnitureList.get(0).getIdFurniture());
    Mockito.verify(furnitureDAO).getAllSelledFurnituresDesc();
  }

  @Test
  @DisplayName("showAllSelledFurnitureDesc Fail - DAL Service error")
  void showAllSelledFurnitureDesc_fail_dalServiceError() {
    Mockito.when(furnitureDAO.getAllSelledFurnituresDesc())
        .thenThrow(new IllegalArgumentException());
    assertThrows(FurnitureException.class, () -> furnitureUCC.showAllSelledFurnitureDesc());
    Mockito.verify(furnitureDAO).getAllSelledFurnituresDesc();
  }

  /*
   * showAllBoughtFurnitureDesc.
   */
  @Test
  @DisplayName("showAllBoughtFurnitureDesc empltyList")
  void showAllBoughtFurnitureDesc_emptyList() {
    Mockito.when(furnitureDAO.getAllBoughtFurnituresDesc())
        .thenReturn(new ArrayList<FurnitureDTO>());
    ArrayList<FurnitureDTO> furnitureList = furnitureUCC.showAllBoughtFurnitureDesc();
    assertEquals(0, furnitureList.size());
    Mockito.verify(furnitureDAO).getAllBoughtFurnituresDesc();
  }

  @Test
  @DisplayName("showAllBoughtFurnitureDesc listWith1Furniture")
  void showAllBoughtFurnitureDesc_listWith1Furniture() {
    ArrayList<FurnitureDTO> mockList = new ArrayList<FurnitureDTO>();
    mockList.add(validFurniture);
    Mockito.when(furnitureDAO.getAllBoughtFurnituresDesc()).thenReturn(mockList);
    ArrayList<FurnitureDTO> furnitureList = furnitureUCC.showAllBoughtFurnitureDesc();
    assertEquals(1, furnitureList.size());
    assertEquals(idFurniture, furnitureList.get(0).getIdFurniture());
    Mockito.verify(furnitureDAO).getAllBoughtFurnituresDesc();
  }

  @Test
  @DisplayName("showAllBoughtFurnitureDesc Fail - DAL Service error")
  void showAllBoughtFurnitureDesc_fail_dalServiceError() {
    Mockito.when(furnitureDAO.getAllBoughtFurnituresDesc())
        .thenThrow(new IllegalArgumentException());
    assertThrows(FurnitureException.class, () -> furnitureUCC.showAllBoughtFurnitureDesc());
    Mockito.verify(furnitureDAO).getAllBoughtFurnituresDesc();
  }



}
