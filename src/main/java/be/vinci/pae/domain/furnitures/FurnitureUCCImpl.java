package be.vinci.pae.domain.furnitures;

import java.util.ArrayList;

import be.vinci.pae.exceptions.FurnitureException;
import be.vinci.pae.services.DalServices;
import be.vinci.pae.services.FurnitureDAO;
import jakarta.inject.Inject;

public class FurnitureUCCImpl implements FurnitureUCC {

  @Inject
  private FurnitureDAO dataService;

  @Inject
  private DalServices dalServices;

  /**
   * Get all furnitures.
   * 
   * @return ArrayList of all the furnitures
   */
  @Override
  public ArrayList<FurnitureDTO> showAllFurniture() {
    ArrayList<FurnitureDTO> furnitures = null;
    try {
      dalServices.startTransaction();
      furnitures = dataService.getAllFurnitures();
      dalServices.commitTransaction();
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw new FurnitureException("showAllFurnitures");
    }
    return furnitures;
  }

  /**
   * update furniture with id from the param(furniture) furniture.
   * 
   * @param furniture : the updated furniture
   * @return FurnitureDTO return the updated furniture returned by DB after update
   */
  @Override
  public FurnitureDTO updateFurniture(FurnitureDTO furniture) {
    try {
      dalServices.startTransaction();
      FurnitureDTO updatedFurniture = dataService.updateFurniture(furniture);
      dalServices.commitTransaction();
      return updatedFurniture;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw new FurnitureException("updateFurniture");
    }
  }

  @Override
  public FurnitureDTO getFurniture(int id) {
    try {
      dalServices.startTransaction();
      FurnitureDTO furniture = dataService.getFurniture(id);
      dalServices.commitTransaction();
      return furniture;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw new FurnitureException("getFurniture");
    }
  }

  /**
   * Get all selled furnitures order by sales desc.
   * 
   * @return ArrayList of all the selled furnitures
   */
  @Override
  public ArrayList<FurnitureDTO> showAllSelledFurnitureDesc() {
    ArrayList<FurnitureDTO> furnitures = null;
    try {
      dalServices.startTransaction();
      furnitures = dataService.getAllSelledFurnituresDesc();
      dalServices.commitTransaction();
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw new FurnitureException("showAllSelledFurnituresDesc");
    }
    return furnitures;
  }

  /**
   * Get all bought furnitures order by id.
   * 
   * @return ArrayList of all the bought furnitures
   */
  @Override
  public ArrayList<FurnitureDTO> showAllBoughtFurnitureDesc() {
    ArrayList<FurnitureDTO> furnitures = null;
    try {
      dalServices.startTransaction();
      furnitures = dataService.getAllBoughtFurnituresDesc();
      dalServices.commitTransaction();
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw new FurnitureException("showAllBoughtFurnituresDesc");
    }
    return furnitures;
  }
}
