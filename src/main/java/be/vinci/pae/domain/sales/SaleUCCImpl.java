package be.vinci.pae.domain.sales;

import java.util.ArrayList;
import be.vinci.pae.domain.furnitures.FurnitureDTO;
import be.vinci.pae.domain.furnitures.FurnitureUCC;
import be.vinci.pae.domain.users.UserDTO;
import be.vinci.pae.domain.users.UserUCC;
import be.vinci.pae.exceptions.SaleException;
import be.vinci.pae.services.DalServices;
import be.vinci.pae.services.SaleDAO;
import jakarta.inject.Inject;

public class SaleUCCImpl implements SaleUCC {

  @Inject
  private SaleDAO dataService;

  @Inject
  private DalServices dalServices;

  @Inject
  private FurnitureUCC furnitureUCC;

  @Inject
  private UserUCC userUCC;


  /**
   * Add a sale.
   */
  @Override
  public SaleDTO addSale(SaleDTO sale) {
    FurnitureDTO furniture = furnitureUCC.getFurniture(sale.getIdFurniture());
    UserDTO user = userUCC.getUserById(sale.getIdUser());
    if (checkSale(furniture.getIdFurniture()) != null) {
      throw new SaleException("Ce meuble a deja ete vendu");
    } else if (user.getRole() == "client" && furniture.getCondition() != "propose") {
      throw new SaleException("Un client ne peut acheter qu'un meuble propser a la vente");
    } else if (user.getRole() == "antiquaire" && furniture.getCondition() != "propose"
        && furniture.getCondition() != "restaure" && furniture.getCondition() != "depose"
        && furniture.getCondition() != "achete") {
      throw new SaleException("Un antiquaire ne peut acheter un meuble dans cet états");
    } else {
      furniture.setCondition("vendu");
      furnitureUCC.updateFurniture(furniture);
      try {
        dalServices.startTransaction();
        SaleDTO newSale = dataService.addSale(sale);
        dalServices.commitTransaction();
        return newSale;
      } catch (Exception e) {
        dalServices.rollbackTransaction();
        throw new SaleException("updateFurniture");
      }
    }
  }

  /*
   * Check if sale alreadyExist.
   */
  @Override
  public SaleDTO checkSale(int id) {
    try {
      dalServices.startTransaction();
      SaleDTO newSale = dataService.getSaleById(id);
      dalServices.commitTransaction();
      return newSale;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw new SaleException("updateFurniture");
    }
  }



  @Override
  public ArrayList<SaleDTO> getAllSalesForUserById(String id) {
    try {
      dalServices.startTransaction();
      ArrayList<SaleDTO> list = dataService.getAllSalesForUserById(id);
      dalServices.commitTransaction();
      return list;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw new SaleException("SaleUCC : getAllSalesForUserById(" + id + ")");
    }
  }

  @Override
  public SaleDTO getSaleByFurnitureId(int id) {
    try {
      dalServices.startTransaction();
      SaleDTO sale = dataService.getSaleById(id);
      dalServices.commitTransaction();
      return sale;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw new SaleException("SaleUCC : getAllSalesForUserById(" + id + ")");
    }
  }

}
