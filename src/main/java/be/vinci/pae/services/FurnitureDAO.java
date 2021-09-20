package be.vinci.pae.services;

import java.util.ArrayList;

import be.vinci.pae.domain.furnitures.FurnitureDTO;

public interface FurnitureDAO {

  ArrayList<FurnitureDTO> getAllFurnitures();

  FurnitureDTO getFurniture(int id);

  FurnitureDTO updateFurniture(FurnitureDTO furniture);

  ArrayList<FurnitureDTO> getAllSelledFurnituresDesc();

  ArrayList<FurnitureDTO> getAllBoughtFurnituresDesc();
}
