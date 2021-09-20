package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import be.vinci.pae.domain.photos.Photo;
import be.vinci.pae.domain.photos.PhotoDTO;
import be.vinci.pae.domain.photos.PhotoFactory;
import be.vinci.pae.domain.photos.PhotoUCC;
import be.vinci.pae.exceptions.DataException;
import be.vinci.pae.services.DalServices;
import be.vinci.pae.services.PhotoDAO;
import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.ApplicationBinderTest;
import jakarta.ws.rs.WebApplicationException;

class PhotoUCCImplTest {
  private static PhotoUCC photoUCC;
  private static PhotoDAO photoDAO;
  private static PhotoFactory photoFactory;
  private static DalServices dalServices;

  private Photo validPhoto;

  @BeforeAll
  static void before() {
    ServiceLocator locator =
        ServiceLocatorUtilities.bind(new ApplicationBinder(), new ApplicationBinderTest());
    photoUCC = locator.getService(PhotoUCC.class);
    photoDAO = locator.getService(PhotoDAO.class);
    photoFactory = locator.getService(PhotoFactory.class);
    dalServices = locator.getService(DalServices.class);
  }

  @BeforeEach
  void setup() throws Exception {
    this.validPhoto = (Photo) photoFactory.getPhoto();
    this.validPhoto.setId(1);
    this.validPhoto.setIdFurniture(1);
    this.validPhoto.setPrefered(true);
    this.validPhoto.setVisible(true);
    this.validPhoto.setBase64Value("Ma photo en base64");

    Mockito.reset(photoDAO);
    Mockito.reset(dalServices);
  }

  @Test
  @DisplayName("getAllPictures_success_emptyList")
  void getAllPictures_success_emptyList() {
    Mockito.when(photoDAO.get()).thenReturn(new ArrayList<PhotoDTO>());
    ArrayList<PhotoDTO> list = photoUCC.getAllPictures();
    assertEquals(0, list.size());
    Mockito.verify(photoDAO).get();
  }

  @Test
  @DisplayName("getAllPictures_success_1eltInList")
  void getAllPictures_success_1eltInList() {
    ArrayList<PhotoDTO> returnedList = new ArrayList<>();
    returnedList.add(validPhoto);

    Mockito.when(photoDAO.get()).thenReturn(returnedList);
    ArrayList<PhotoDTO> list = photoUCC.getAllPictures();
    assertEquals(1, list.size());
    assertEquals(1, list.get(0).getId());
    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(photoDAO).get();
    Mockito.verify(dalServices).commitTransaction();
  }

  @Test
  @DisplayName("getAllPictures_fail_startTransaction")
  void getAllPictures_fail_startTransaction() {
    doThrow(new IllegalArgumentException()).when(dalServices).startTransaction();
    assertThrows(WebApplicationException.class, () -> photoUCC.getAllPictures());
    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(dalServices).rollbackTransaction();
  }

  @Test
  @DisplayName("getAllPictures(id)_success")
  void getAllPicturesID_success() {
    int id = 1;
    ArrayList<PhotoDTO> returnedList = new ArrayList<>();
    returnedList.add(validPhoto);

    Mockito.when(photoDAO.get(id)).thenReturn(returnedList);
    ArrayList<PhotoDTO> list = photoUCC.getAllPictures(id);
    assertEquals(id, list.get(0).getIdFurniture());
    assertEquals(1, list.size());
    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(photoDAO).get(id);
    Mockito.verify(dalServices).commitTransaction();
  }

  @Test
  @DisplayName("getAllPictures(id)_fail_startTransaction")
  void getAllPicturesID_fail_startTransaction() {
    int id = 1;
    doThrow(new IllegalArgumentException()).when(dalServices).startTransaction();
    assertThrows(WebApplicationException.class, () -> photoUCC.getAllPictures(id));
    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(dalServices).rollbackTransaction();
  }

  @Test
  @DisplayName("getAllVisiblePictures()_success")
  void getAllVisiblePictures_success() {
    Mockito.when(photoDAO.getVisible()).thenReturn(new ArrayList<PhotoDTO>());
    ArrayList<PhotoDTO> list = photoUCC.getAllVisiblePictures();
    assertEquals(0, list.size());
    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(photoDAO).getVisible();
    Mockito.verify(dalServices).commitTransaction();
  }

  @Test
  @DisplayName("getAllVisiblePictures()_success_1eltInList")
  void getAllVisiblePictures_success_1eltInList() {
    ArrayList<PhotoDTO> returnedList = new ArrayList<PhotoDTO>();
    returnedList.add(validPhoto);
    Mockito.when(photoDAO.getVisible()).thenReturn(returnedList);
    ArrayList<PhotoDTO> list = photoUCC.getAllVisiblePictures();
    assertEquals(1, list.size());
    assertEquals(1, list.get(0).getIdFurniture());
    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(photoDAO).getVisible();
    Mockito.verify(dalServices).commitTransaction();
  }

  @Test
  @DisplayName("getAllVisiblePictures()_fail_startTransaction")
  void getAllVisiblePictures_fail_startTransaction() {
    doThrow(new DataException()).when(dalServices).startTransaction();
    assertThrows(WebApplicationException.class, () -> photoUCC.getAllVisiblePictures());
    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(dalServices).rollbackTransaction();
  }

  @Test
  @DisplayName("getAllVisiblePictures(id)_success_emptyList")
  void getAllVisiblePicturesID_success_emptyList() {
    int id = 1;
    Mockito.when(photoDAO.getVisible(id)).thenReturn(new ArrayList<PhotoDTO>());
    assertEquals(0, photoUCC.getAllVisiblePictures(id).size());

    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(photoDAO).getVisible(id);
    Mockito.verify(dalServices).commitTransaction();
  }

  @Test
  @DisplayName("getAllVisiblePictures(id)_success_nonEmptyList")
  void getAllVisiblePicturesID_success_nonEmptyList() {
    int id = 1;
    ArrayList<PhotoDTO> returnedList = new ArrayList<PhotoDTO>();
    returnedList.add(validPhoto);
    Mockito.when(photoDAO.getVisible(id)).thenReturn(returnedList);

    ArrayList<PhotoDTO> list = photoUCC.getAllVisiblePictures(id);
    assertEquals(1, list.size());
    assertEquals(id, list.get(0).getIdFurniture());

    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(photoDAO).getVisible(id);
    Mockito.verify(dalServices).commitTransaction();
  }

  @Test
  @DisplayName("getAllVisiblePictures(id)_fail_startTransaction")
  void getAllVisiblePicturesID_fail_startTransaction() {
    doThrow(new IllegalArgumentException()).when(dalServices).startTransaction();
    assertThrows(WebApplicationException.class, () -> photoUCC.getAllVisiblePictures(1));
    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(dalServices).rollbackTransaction();
  }

  @Test
  @DisplayName("getPrefered(id)")
  void getPreferedID_success() {
    int id = 1;
    Mockito.when(photoDAO.getPrefered(id)).thenReturn(validPhoto);
    PhotoDTO photo = photoUCC.getPrefered(id);
    assertEquals(id, photo.getIdFurniture());

    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(photoDAO).getPrefered(id);
    Mockito.verify(dalServices).commitTransaction();
  }

  @Test
  @DisplayName("getPrefered(id)_null")
  void getPreferedID_success_null() {
    int id = 1;
    Mockito.when(photoDAO.getPrefered(id)).thenReturn(null);
    assertNull(photoUCC.getPrefered(id));

    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(photoDAO).getPrefered(id);
    Mockito.verify(dalServices).commitTransaction();
  }

  @Test
  @DisplayName("getPrefered(id)_fail_startTransaction")
  void getPreferedID_fail_startTransaction() {
    doThrow(new IllegalArgumentException()).when(dalServices).startTransaction();
    assertThrows(WebApplicationException.class, () -> photoUCC.getPrefered(1));
    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(dalServices).rollbackTransaction();
  }

  @Test
  @DisplayName("update_success")
  void update_success() {
    Mockito.when(photoDAO.update(validPhoto)).thenReturn(validPhoto);
    assertEquals(validPhoto, photoUCC.update(validPhoto));

    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(photoDAO).update(validPhoto);
    Mockito.verify(dalServices).commitTransaction();
  }

  @Test
  @DisplayName("update_fail_id<0")
  void update_fail_invalidId() {
    Photo invalidPhoto = (Photo) photoFactory.getPhoto();
    invalidPhoto.setId(0);
    assertThrows(WebApplicationException.class, () -> photoUCC.update(invalidPhoto));
  }

  @Test
  @DisplayName("update_fail_startTransaction")
  void update_fail_startTransaction() {
    doThrow(new IllegalArgumentException()).when(dalServices).startTransaction();
    assertThrows(WebApplicationException.class, () -> photoUCC.update(validPhoto));

    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(dalServices).rollbackTransaction();
  }

  @Test
  @DisplayName("insert_success")
  void insert_success() {
    Mockito.when(photoDAO.create(validPhoto)).thenReturn(validPhoto);
    PhotoDTO photo = photoUCC.insert(validPhoto);
    assertEquals(validPhoto.getBase64Value(), photo.getBase64Value());
    assertEquals(validPhoto.getId(), photo.getId());

    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(photoDAO).create(validPhoto);
    Mockito.verify(dalServices).commitTransaction();
  }

  @Test
  @DisplayName("insert_fail_invalidInputs")
  void insert_fail_invalidInputs() {
    Photo invalidPhoto = (Photo) photoFactory.getPhoto();
    assertThrows(NullPointerException.class, () -> photoUCC.insert(invalidPhoto));
  }

  @Test
  @DisplayName("insert_fail_blankBase64Value")
  void insert_fail_blankBase64Value() {
    Photo invalidPhoto = (Photo) photoFactory.getPhoto();
    invalidPhoto.setBase64Value("");
    assertThrows(WebApplicationException.class, () -> photoUCC.insert(invalidPhoto));
  }

  @Test
  @DisplayName("insert_fail_invalidIdFurniture")
  void insert_fail_invalidIdFurniture() {
    Photo invalidPhoto = (Photo) photoFactory.getPhoto();
    invalidPhoto.setBase64Value("valid base64Value");
    invalidPhoto.setIdFurniture(0);
    assertThrows(WebApplicationException.class, () -> photoUCC.insert(invalidPhoto));
  }

  @Test
  @DisplayName("insert_fail_startTransaction")
  void insert_fail_startTransaction() {
    doThrow(new IllegalArgumentException()).when(dalServices).startTransaction();
    assertThrows(WebApplicationException.class, () -> photoUCC.insert(validPhoto));

    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(dalServices).rollbackTransaction();
  }

  @Test
  @DisplayName("delete_success")
  void delete_success() {
    int id = 1;
    Mockito.when(photoDAO.delete(id)).thenReturn(validPhoto);
    PhotoDTO photo = photoUCC.delete(id);
    assertEquals(id, photo.getId());

    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(photoDAO).delete(id);
    Mockito.verify(dalServices).commitTransaction();
  }

  @Test
  @DisplayName("delete_fail_invalidId")
  void delete_fail_invalidId() {
    assertThrows(IllegalArgumentException.class, () -> photoUCC.delete(0));
    assertThrows(IllegalArgumentException.class, () -> photoUCC.delete(-1));
  }

  @Test
  @DisplayName("delete_fail_startTransaction")
  void delete_fail_startTransaction() {
    doThrow(new IllegalArgumentException()).when(dalServices).startTransaction();
    assertThrows(WebApplicationException.class, () -> photoUCC.delete(1));

    Mockito.verify(dalServices).startTransaction();
    Mockito.verify(dalServices).rollbackTransaction();
  }


}
