package be.vinci.pae.api;


import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.api.filters.AuthorizeForAdmin;
import be.vinci.pae.domain.photos.Photo;
import be.vinci.pae.domain.photos.PhotoDTO;
import be.vinci.pae.domain.photos.PhotoFactory;
import be.vinci.pae.domain.photos.PhotoUCC;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;

@Singleton
@Path("/photos")
public class PhotoRessource {
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  @Inject
  private PhotoUCC photoUCC;

  @Inject
  private PhotoFactory photoFactory;


  /**
   * get all photos from db.
   * 
   * @return list of photos
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public ArrayList<PhotoDTO> getPhotos() {
    logInfo("Affichage de toutes les photos");
    return photoUCC.getAllPictures();
  }

  /**
   * get all photos from specific furniture.
   * 
   * 
   * @return list of photos
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/visible")
  public ArrayList<PhotoDTO> getAllVisiblePhotos() {
    logInfo("Affichage de toutes les photos visibles");
    return photoUCC.getAllVisiblePictures();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/furniture/{id}/visible")
  public ArrayList<PhotoDTO> getAllVisiblePhotos(@PathParam("id") int id) {
    logInfo("Affichage de toutes les photos visibles pour meuble id : " + id);
    return photoUCC.getAllVisiblePictures(id);
  }

  /**
   * get all photos from specific furniture.
   * 
   * @param id : id_furniture
   * 
   * @return list of photos
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/furniture/{id}")
  public ArrayList<PhotoDTO> getAllPhotos(@PathParam("id") int id) {
    logInfo("Affichage de toutes les photos pour meuble id : " + id);
    return photoUCC.getAllPictures(id);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/furniture/{id}/prefered")
  public PhotoDTO getPreferedPhoto(@PathParam("id") int id) {
    logInfo("Affichage de la photo preferee pour le meuble id : " + id);
    return photoUCC.getPrefered(id);
  }

  /**
   * creates a new photo.
   * 
   * @return photo created
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public PhotoDTO create(JsonNode json) {
    logInfo("Create a new photo");
    if (validPhotoInputs(json)) {
      try {
        Photo photo = getPhotoFromJson(json);
        return photoUCC.insert(photo);
      } catch (Exception e) {
        throw new WebApplicationException("Error while insert new photo", e);
      }
    } else {
      logInfo("Not all inputs where given for creating a new photo");
      return null;
    }
  }


  /**
   * Update a Photo.
   * 
   * @param json : photo send by client
   * @return photo : updated photo
   */
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @AuthorizeForAdmin
  public PhotoDTO update(JsonNode json) {
    if (json.hasNonNull("id_photo") && json.hasNonNull("visible") && json.hasNonNull("prefered")) {
      try {
        logInfo("Begin update photo id : " + json.get("id_photo"));

        Photo photo = (Photo) photoFactory.getPhoto();
        photo.setPrefered(json.get("prefered").asBoolean());
        photo.setVisible(json.get("visible").asBoolean());
        photo.setId(json.get("id_photo").asInt());
        return photoUCC.update(photo);
      } catch (Exception e) {
        throw new WebApplicationException("Error while updating a photo", e);
      }
    } else {
      logInfo("Not all inputs where given for updating a photo");
    }
    return null;
  }

  /**
   * Delete a Photo from db.
   * 
   * @param id : id of the photo to delete
   * @return photo : deleted photo
   */
  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @AuthorizeForAdmin
  public PhotoDTO delete(@PathParam("id") int id) {
    if (id > 0) {
      logInfo("Start delete photo id : " + id);
      return photoUCC.delete(id);
    }
    return null;
  }

  /**
   * Verify if all info are given by client.
   * 
   * @param json JsonNode with info send by client
   * @return false if an info is missing, else true
   */
  private boolean validPhotoInputs(JsonNode json) {
    if (!json.hasNonNull("id_furniture") || !json.hasNonNull("base64_value")
        || !json.hasNonNull("visible") || !json.hasNonNull("prefered")) {
      return false;
    }
    return true;
  }

  /**
   * Logging method for info log.
   * 
   * @param msg to log
   */
  private void logInfo(String msg) {
    LOGGER.setLevel(Level.INFO);
    LOGGER.info(msg + "\n--------------------------------------"
        + "--------------------------------------------------------\n");
  }

  private Photo getPhotoFromJson(JsonNode json) {
    Photo photo = (Photo) photoFactory.getPhoto();
    photo.setIdFurniture(json.get("id_furniture").asInt());
    photo.setBase64Value(json.get("base64_value").asText());
    photo.setPrefered(json.get("prefered").asBoolean());
    photo.setVisible(json.get("visible").asBoolean());
    return photo;
  }
}
