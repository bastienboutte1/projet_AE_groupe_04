package be.vinci.pae.domain.furnitures;

import java.util.ArrayList;

public interface FurnitureUCC {

  ArrayList<FurnitureDTO> showAllFurniture();

  FurnitureDTO getFurniture(int id);

  FurnitureDTO updateFurniture(FurnitureDTO furniture);

  ArrayList<FurnitureDTO> showAllSelledFurnitureDesc();

  ArrayList<FurnitureDTO> showAllBoughtFurnitureDesc();

}
