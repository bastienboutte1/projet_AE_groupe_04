package be.vinci.pae.api;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.databind.JsonNode;
import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.api.filters.AuthorizeForAdmin;
import be.vinci.pae.domain.sales.SaleDTO;
import be.vinci.pae.domain.sales.SaleFactory;
import be.vinci.pae.domain.sales.SaleUCC;
import be.vinci.pae.exceptions.SaleException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Singleton
@Path("/sales")
public class SaleRessource {

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  @Inject
  private SaleUCC saleUCC;

  @Inject
  private SaleFactory saleFactory;

  /**
   * add sale.
   * 
   * @Param json json with user information
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @AuthorizeForAdmin
  public SaleDTO addSale(JsonNode json) {

    if (!verifySaleInputs(json)) {
      throw new SaleException("addSale saleRessource missing inputs");
    }

    SaleDTO sale = this.saleFactory.getSale();
    sale.setIdFurniture(json.get("idFurniture").asInt());
    sale.setIdUser(json.get("idUser").asText());
    sale.setLastSellingPrice(json.get("lastSellingPrice").asDouble());
    sale = saleUCC.addSale(sale);

    LOGGER.setLevel(Level.INFO);
    LOGGER.info("vente du meuble  " + sale.getIdFurniture() + " a " + sale.getIdUser()
        + " numero de vente = " + sale.getIdSale() + "\n-----------------------------------------"
        + "----------------------------------------------------- \n");
    return sale;
  }

  /**
   * Return all the sales for a specific user.
   * 
   * @param id : id_user
   * @return list : list of all the sales made by this user
   */
  @GET
  @Path("/user/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ArrayList<SaleDTO> getAllSalesForUserById(@PathParam("id") String id) {
    LOGGER.setLevel(Level.INFO);
    LOGGER.info("Recuperation des achats de l'utilisateur : " + id
        + "\n-----------------------------------------"
        + "----------------------------------------------------- \n");
    return saleUCC.getAllSalesForUserById(id);
  }

  /**
   * Return the sale of a furnitures.
   * 
   * @param id : id_furniture
   * @return sale
   */
  @GET
  @Path("/furniture/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @AuthorizeForAdmin
  public SaleDTO getSaleByFurnitureId(@PathParam("id") int id) {
    LOGGER.setLevel(Level.INFO);
    LOGGER.info(
        "Recuperation de l'achat du meuble : " + id + "\n-----------------------------------------"
            + "----------------------------------------------------- \n");
    return saleUCC.getSaleByFurnitureId(id);
  }

  /**
   * Verify if all inputs needed are given.
   * 
   * @param json JsonNode containing all data
   * @return true if all inputs exists, return false otherwise
   */
  private boolean verifySaleInputs(JsonNode json) {
    return !json.hasNonNull("finalPrice") || !json.hasNonNull("userName");
  }
}
