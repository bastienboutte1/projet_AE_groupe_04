package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import java.util.ArrayList;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import be.vinci.pae.domain.type.TypeFurniture;
import be.vinci.pae.domain.type.TypeFurnitureFactory;
import be.vinci.pae.domain.type.TypeFurnitureUCC;
import be.vinci.pae.services.DalServices;
import be.vinci.pae.services.TypeFurnitureDAO;
import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.ApplicationBinderTest;
import jakarta.ws.rs.WebApplicationException;

class TypeFunritureUCCImplTest {
  private static TypeFurnitureUCC typeFurnitureUCC;
  private static TypeFurnitureFactory typeFurnitureFactory;
  private static TypeFurnitureDAO typeFurnitureDAO;
  private static DalServices dalServices;

  private TypeFurniture validTypeFurniture;

  private static final String monType = "Mon type";
  private static final int monIdFurniture = 1;

  @BeforeAll
  static void before() {
    ServiceLocator locator =
        ServiceLocatorUtilities.bind(new ApplicationBinder(), new ApplicationBinderTest());
    typeFurnitureUCC = locator.getService(TypeFurnitureUCC.class);
    typeFurnitureFactory = locator.getService(TypeFurnitureFactory.class);
    typeFurnitureDAO = locator.getService(TypeFurnitureDAO.class);
    dalServices = locator.getService(DalServices.class);
  }

  @BeforeEach
  void setup() throws Exception {

    this.validTypeFurniture = typeFurnitureFactory.getTypeFurniture();
    this.validTypeFurniture.setType(monType);
    this.validTypeFurniture.setIdTypeFurniture(monIdFurniture);

    Mockito.reset(typeFurnitureDAO);
    Mockito.reset(dalServices);
  }

  @Test
  @DisplayName("getTypeFunritureById(id)_success")
  void getTypeFunritureById_success() {
    Mockito.when(typeFurnitureDAO.getTypeFurnitureById(monIdFurniture))
        .thenReturn(validTypeFurniture);
    TypeFurniture type = typeFurnitureUCC.getTypeFunritureById(monIdFurniture);
    assertEquals(monType, type.getType());
    assertEquals(monIdFurniture, type.getIdTypeFurniture());

    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(typeFurnitureDAO).getTypeFurnitureById(monIdFurniture);
    Mockito.verify(dalServices).commitTransaction();
  }

  @Test
  @DisplayName("getTypeFunritureById_fail_startTransaction")
  void getTypeFunritureById_fail_startTransaction() {
    doThrow(new IllegalArgumentException()).when(dalServices).startTransaction();
    assertThrows(WebApplicationException.class,
        () -> typeFurnitureUCC.getTypeFunritureById(monIdFurniture));

    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(dalServices).rollbackTransaction();
  }

  @Test
  @DisplayName("getAllTypeFurnites_success")
  void getAllTypeFurnites_success() {
    ArrayList<TypeFurniture> returnedList = new ArrayList<>();
    returnedList.add(validTypeFurniture);
    Mockito.when(typeFurnitureDAO.getAllTypeFurnitures()).thenReturn(returnedList);
    ArrayList<TypeFurniture> list = typeFurnitureUCC.getAllTypeFurnites();
    assertEquals(1, list.size());
    assertEquals(monIdFurniture, list.get(0).getIdTypeFurniture());
    assertEquals(monType, list.get(0).getType());

    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(typeFurnitureDAO).getAllTypeFurnitures();
    Mockito.verify(dalServices).commitTransaction();
  }

  @Test
  @DisplayName("getAllTypeFurnites_fail_startTransaction")
  void getAllTypeFurnites_fail_startTransaction() {
    doThrow(new IllegalArgumentException()).when(dalServices).startTransaction();
    assertThrows(WebApplicationException.class, () -> typeFurnitureUCC.getAllTypeFurnites());

    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(dalServices).rollbackTransaction();
  }

}
