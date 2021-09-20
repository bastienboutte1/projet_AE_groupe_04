package be.vinci.pae.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import be.vinci.pae.domain.type.TypeFurniture;
import be.vinci.pae.domain.type.TypeFurnitureFactory;
import be.vinci.pae.exceptions.DataException;
import jakarta.inject.Inject;

public class TypeFurnitureDAOImpl implements TypeFurnitureDAO {

  @Inject
  private DalBackendServices dalServices;

  @Inject
  private TypeFurnitureFactory typeFurnitureFactory;

  /**
   * Constructor for class.
   */
  public TypeFurnitureDAOImpl() {
    System.out.println("TYPE FURNITURE DATA LOADED");
  }

  @Override
  public TypeFurniture getTypeFurnitureById(int id) {
    try {
      String selectSQL = "SELECT * FROM projet.typeFurnitures " + "WHERE id_typeFurniture = ?";
      PreparedStatement ps = dalServices.getPreparedStatement(selectSQL);
      ps.setInt(1, id);
      return execQuery(ps);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataException("getTypeFurnitureById");
    }
  }

  @Override
  public ArrayList<TypeFurniture> getAllTypeFurnitures() {
    try {
      String sql = "SELECT * FROM projet.typeFurnitures;";
      PreparedStatement ps = dalServices.getPreparedStatement(sql);
      return execQueryMulti(ps);
    } catch (Exception e) {
      throw new DataException("getAllTypeFunritures");
    }
  }

  private TypeFurniture execQuery(PreparedStatement ps) {
    try (ResultSet rs = ps.executeQuery()) {
      TypeFurniture newType = typeFurnitureFactory.getTypeFurniture();
      if (rs.next()) {
        newType.setIdTypeFurniture(rs.getInt("id_typeFurniture"));
        newType.setType(rs.getString("type"));
        return newType;
      }
    } catch (SQLException e) {
      throw new DataException("execQuery");
    }
    return null;
  }

  private ArrayList<TypeFurniture> execQueryMulti(PreparedStatement ps) {
    ArrayList<TypeFurniture> list = new ArrayList<TypeFurniture>();
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        TypeFurniture newType = typeFurnitureFactory.getTypeFurniture();
        newType.setIdTypeFurniture(rs.getInt("id_typeFurniture"));
        newType.setType(rs.getString("type"));
        list.add(newType);
      }
      return list;
    } catch (Exception e) {
      throw new DataException("TypeFurnitureDAO : execQueryMulti");
    }
  }
}
