package be.vinci.pae.domain.photos;

import java.util.ArrayList;
import be.vinci.pae.services.DalServices;
import be.vinci.pae.services.PhotoDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

public class PhotoUCCImpl implements PhotoUCC {

  @Inject
  private PhotoDAO dataService;

  @Inject
  private DalServices dalServices;

  /**
   * Get all pictures from the DB.
   * 
   * @return list of all the photos
   */
  @Override
  public ArrayList<PhotoDTO> getAllPictures() {
    try {
      dalServices.startTransaction();
      ArrayList<PhotoDTO> list = dataService.get();
      dalServices.commitTransaction();
      return list;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw new WebApplicationException("Photo : getAllPictures() error", e);
    }
  }


  /**
   * Get all pictures from with the id_furniture passed.
   * 
   * @param id : id_furniture
   * 
   * @return list of all the photos that belong to id_furniture
   */
  @Override
  public ArrayList<PhotoDTO> getAllPictures(int id) {
    try {
      dalServices.startTransaction();
      ArrayList<PhotoDTO> list = dataService.get(id);
      dalServices.commitTransaction();
      return list;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw new WebApplicationException("Photo : getAllPictures(int id) error");
    }
  }

  @Override
  public ArrayList<PhotoDTO> getAllVisiblePictures() {
    try {
      dalServices.startTransaction();
      ArrayList<PhotoDTO> list = dataService.getVisible();
      dalServices.commitTransaction();
      return list;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw new WebApplicationException("Photo : getAllVisiblePictures()");
    }
  }

  @Override
  public ArrayList<PhotoDTO> getAllVisiblePictures(int id) {
    try {
      dalServices.startTransaction();
      ArrayList<PhotoDTO> list = dataService.getVisible(id);
      dalServices.commitTransaction();
      return list;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw new WebApplicationException("Photo : getAllVisiblePicture(" + id + ")");
    }
  }

  @Override
  public PhotoDTO getPrefered(int id) {
    try {
      dalServices.startTransaction();
      PhotoDTO photo = dataService.getPrefered(id);
      dalServices.commitTransaction();
      return photo;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw new WebApplicationException("Photo : getPrefered");
    }
  }

  @Override
  public PhotoDTO update(PhotoDTO photo) {
    if (photo.getId() > 0) {
      try {
        dalServices.startTransaction();
        PhotoDTO updatedPhoto = dataService.update(photo);
        dalServices.commitTransaction();
        return updatedPhoto;
      } catch (Exception e) {
        dalServices.rollbackTransaction();
        throw new WebApplicationException("PhotoUCCImpl : update");
      }
    } else {
      throw new WebApplicationException("PhotoUCCImpl : update");
    }
  }

  /**
   * Insert the photo in the DB.
   * 
   * @param photo : photo we want to insert
   * 
   * @return newPhoto : photo returned by db (including photo_id)
   */
  @Override
  public PhotoDTO insert(PhotoDTO photo) {
    if (validPhotoInput(photo)) {
      try {
        dalServices.startTransaction();
        PhotoDTO newPhoto = dataService.create(photo);
        dalServices.commitTransaction();
        return newPhoto;
      } catch (Exception e) {
        dalServices.rollbackTransaction();
        throw new WebApplicationException("PhotoUCCImpl : create");
      }
    } else {
      throw new WebApplicationException("PhotoUCCImpl : create");
    }
  }

  /**
   * Delete photo with id in param.
   * 
   * @param id : id of the photo
   * @return photo : the deleted photo from db
   */
  @Override
  public PhotoDTO delete(int id) {
    if (id <= 0) {
      throw new IllegalArgumentException("id must be positive");
    }
    try {
      dalServices.startTransaction();
      PhotoDTO deletedPhoto = dataService.delete(id);
      dalServices.commitTransaction();
      return deletedPhoto;

    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw new WebApplicationException("PhotoUCCImpl : delete");
    }
  }

  private boolean validPhotoInput(PhotoDTO photo) {
    if (photo.getBase64Value().isBlank() || photo.getId() < 0 || photo.getIdFurniture() <= 0) {
      return false;
    }
    return true;
  }
}
