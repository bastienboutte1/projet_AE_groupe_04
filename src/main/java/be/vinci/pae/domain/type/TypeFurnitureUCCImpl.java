package be.vinci.pae.domain.type;

import java.util.ArrayList;

import be.vinci.pae.services.DalServices;
import be.vinci.pae.services.TypeFurnitureDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

public class TypeFurnitureUCCImpl implements TypeFurnitureUCC {

  @Inject
  private TypeFurnitureDAO dataService;

  @Inject
  private DalServices dalServices;

  @Override
  public TypeFurniture getTypeFunritureById(int id) {
    try {
      dalServices.startTransaction();
      TypeFurniture type = dataService.getTypeFurnitureById(id);
      dalServices.commitTransaction();
      return type;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw new WebApplicationException("getTypeFunritureById(" + id + ")");
    }
  }

  @Override
  public ArrayList<TypeFurniture> getAllTypeFurnites() {
    try {
      dalServices.startTransaction();
      ArrayList<TypeFurniture> list = dataService.getAllTypeFurnitures();
      dalServices.commitTransaction();
      return list;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw new WebApplicationException("getAllTypeFurnites");
    }
  }
}
