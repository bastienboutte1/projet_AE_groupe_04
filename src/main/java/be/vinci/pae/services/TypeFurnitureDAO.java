package be.vinci.pae.services;

import java.util.ArrayList;
import be.vinci.pae.domain.type.TypeFurniture;

public interface TypeFurnitureDAO {

  ArrayList<TypeFurniture> getAllTypeFurnitures();

  TypeFurniture getTypeFurnitureById(int id);
}
