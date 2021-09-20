package be.vinci.pae.api;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import be.vinci.pae.domain.type.TypeFurniture;
import be.vinci.pae.domain.type.TypeFurnitureUCC;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Singleton
@Path("/type-furnitures")
public class TypeFurnitureRessource {

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  @Inject
  private TypeFurnitureUCC typeFurnitureUCC;

  /**
   * Get all the different type.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public ArrayList<TypeFurniture> getAllType() {
    LOGGER.setLevel(Level.INFO);
    LOGGER.info(
        "Recuperation de tous les types de Meubles" + "\n-----------------------------------------"
            + "----------------------------------------------------- \n");
    return typeFurnitureUCC.getAllTypeFurnites();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  public TypeFurniture getTypeFurnitureById(@PathParam("id") int id) {
    return typeFurnitureUCC.getTypeFunritureById(id);
  }
}
