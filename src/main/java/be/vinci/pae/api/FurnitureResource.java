package be.vinci.pae.api;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.api.filters.AuthorizeForAdmin;
import be.vinci.pae.domain.furnitures.FurnitureDTO;
import be.vinci.pae.domain.furnitures.FurnitureFactory;
import be.vinci.pae.domain.furnitures.FurnitureUCC;
import be.vinci.pae.exceptions.UpdateFurnitureException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("/furnitures")
public class FurnitureResource {

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  @Inject
  private FurnitureUCC furnitureUCC;

  @Inject
  private FurnitureFactory furnitureFactory;

  /**
   * get all the furnitures.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public ArrayList<FurnitureDTO> getAllFurniture() {
    LOGGER.setLevel(Level.INFO);
    LOGGER.info("Affichage de tous les meubles \n-------------------------------------------------"
        + "--------------------------------------------- \n");

    return this.furnitureUCC.showAllFurniture();
  }

  /**
   * get all the selled furnitures.
   */
  @GET
  @Path("/selled")
  @Produces(MediaType.APPLICATION_JSON)
  public ArrayList<FurnitureDTO> getAllSelledFurnitureDesc() {
    LOGGER.setLevel(Level.INFO);
    LOGGER.info(
        "Affichage de tous les meubles vendus \n-------------------------------------------------"
            + "--------------------------------------------- \n");

    return this.furnitureUCC.showAllSelledFurnitureDesc();
  }

  /**
   * get all the bought furnitures.
   */
  @GET
  @Path("/bought")
  @Produces(MediaType.APPLICATION_JSON)
  public ArrayList<FurnitureDTO> getAllBoughtFurnitureDesc() {
    LOGGER.setLevel(Level.INFO);
    LOGGER.info(
        "Affichage de tous les meubles achetés \n-------------------------------------------------"
            + "--------------------------------------------- \n");

    return this.furnitureUCC.showAllBoughtFurnitureDesc();
  }


  /**
   * Get the furniture with id passed.
   * 
   * @param id of the furnitures wanted
   * @return the furniture with thr corresponding id
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public FurnitureDTO getFurniture(@PathParam("id") int id) {
    FurnitureDTO furniture = this.furnitureUCC.getFurniture(id);
    if (furniture == null) {
      throw new WebApplicationException("Meuble avec id = " + id + " n'a pas ete trouve", null,
          Status.NOT_FOUND);
    }
    return furniture;
  }

  /**
   * Update Furniture.
   */
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @AuthorizeForAdmin
  public FurnitureDTO updateFurniture(JsonNode json) {
    if (!verifyFurnitureInfo(json)) {
      throw new WebApplicationException("Un ou plusieurs champs d'un meuble ne sont pas fournis",
          null, Status.BAD_REQUEST);
    }
    FurnitureDTO furniture = furnitureFactory.getFurniture();
    furniture.setCondition(json.get("condition").asText());
    furniture.setIdFurniture(json.get("id_furniture").asInt());
    furniture.setDescription(json.get("description").asText());
    furniture.setIdType(json.get("id_type").asInt());
    furniture.setSellingPrice(json.get("selling_price").asDouble());
    furniture.setPurchasePrice(json.get("purchase_price").asDouble());

    LOGGER.setLevel(Level.INFO);
    LOGGER.info("Mise à jour du meuble : " + furniture.getDescription()
        + "\n--------------------------------------------"
        + "-------------------------------------------------- \n");

    try {
      furniture = this.furnitureUCC.updateFurniture(furniture);
      if (furniture == null) {
        throw new UpdateFurnitureException();
      }
    } catch (UpdateFurnitureException e) {
      throw new WebApplicationException("Error while updating a furniture", e);
    }
    return furniture;
  }

  private boolean verifyFurnitureInfo(JsonNode json) {
    if (!json.hasNonNull("condition") || !json.hasNonNull("id_furniture")
        || !json.hasNonNull("description") || !json.hasNonNull("id_type")
        || !json.hasNonNull("selling_price") || !json.hasNonNull("purchase_price")) {
      return false;
    }
    return true;
  }
}
