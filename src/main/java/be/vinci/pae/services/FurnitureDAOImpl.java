package be.vinci.pae.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import be.vinci.pae.domain.furnitures.FurnitureDTO;
import be.vinci.pae.domain.furnitures.FurnitureFactory;
import be.vinci.pae.exceptions.DataException;
import jakarta.inject.Inject;

public class FurnitureDAOImpl implements FurnitureDAO {

  @Inject
  private DalBackendServices dalServices;

  @Inject
  private FurnitureFactory furnitureFactory;


  /**
   * Constructor for class.
   */
  public FurnitureDAOImpl() {
    System.out.println("FURNITURE DATA LOADED");
  }


  // rajouter expcetion
  @Override
  public ArrayList<FurnitureDTO> getAllFurnitures() {
    PreparedStatement ps = dalServices.getPreparedStatement("SELECT * FROM projet.furnitures;");
    ArrayList<FurnitureDTO> furnitures = new ArrayList<FurnitureDTO>();
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        FurnitureDTO furniture = furnitureFactory.getFurniture();
        completeFurniture(furniture, rs);
        furnitures.add(furniture);
      }
      return furnitures;
    } catch (SQLException e) {
      throw new DataException("getAllFurnitures");
    }
  }

  @Override
  public FurnitureDTO updateFurniture(FurnitureDTO furniture) {
    try {
      String sql = "UPDATE projet.furnitures "
          + "SET id_type = ?, description = ?, condition = ?, purchase_price = ?, "
          + "selling_price = ?" + "WHERE id_furniture = ? RETURNING *";
      PreparedStatement ps = dalServices.getPreparedStatement(sql);
      ps.setInt(1, furniture.getIdType());
      ps.setString(2, furniture.getDescription());
      ps.setString(3, furniture.getCondition());
      ps.setDouble(4, furniture.getPurchasePrice());
      ps.setDouble(5, furniture.getSellingPrice());
      ps.setInt(6, furniture.getIdFurniture());
      return execQuery(ps);
    } catch (SQLException e) {
      throw new DataException("updateFurniture");
    }
  }

  private FurnitureDTO execQuery(PreparedStatement ps) {
    FurnitureDTO furniture = furnitureFactory.getFurniture();
    try (ResultSet rs = ps.executeQuery()) {
      if (rs.next()) {
        completeFurniture(furniture, rs);
        return furniture;
      }
    } catch (SQLException e) {
      throw new DataException("execQuery");
    }
    return null;
  }

  private void completeFurniture(FurnitureDTO furniture, ResultSet rs) throws SQLException {
    furniture.setIdFurniture(rs.getInt("id_furniture"));
    furniture.setIdType(rs.getInt("id_type"));
    furniture.setDescription(rs.getString("description"));
    furniture.setCondition(rs.getString("condition"));
    furniture.setPurchasePrice(rs.getDouble("purchase_price"));
    furniture.setSellingPrice(rs.getDouble("selling_price"));
  }

  @Override
  public FurnitureDTO getFurniture(int id) {
    try {
      String sql = "SELECT * FROM projet.furnitures WHERE id_furniture=?;";
      PreparedStatement ps = dalServices.getPreparedStatement(sql);
      ps.setInt(1, id);
      return execQuery(ps);
    } catch (SQLException e) {
      throw new DataException("getFurniture");
    }
  }

  @Override
  public ArrayList<FurnitureDTO> getAllSelledFurnituresDesc() {
    PreparedStatement ps =
        dalServices.getPreparedStatement("SELECT * FROM projet.furnitures fu, projet.sales sa "
            + "WHERE fu.id_furniture = sa.id_furniture "
            + "AND fu.condition = 'vendu' ORDER BY sa.date_of_sale DESC;");
    ArrayList<FurnitureDTO> furnitures = new ArrayList<FurnitureDTO>();
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        FurnitureDTO furniture = furnitureFactory.getFurniture();
        completeFurniture(furniture, rs);
        furnitures.add(furniture);
      }
      return furnitures;
    } catch (SQLException e) {
      throw new DataException("getAllFurnituresSaledDesc");
    }
  }

  @Override
  public ArrayList<FurnitureDTO> getAllBoughtFurnituresDesc() {
    PreparedStatement ps = dalServices
        .getPreparedStatement("SELECT * FROM projet.furnitures WHERE condition != 'refuse' "
            + "AND condition != 'vendu' ORDER BY id_furniture DESC;");
    ArrayList<FurnitureDTO> furnitures = new ArrayList<FurnitureDTO>();
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        FurnitureDTO furniture = furnitureFactory.getFurniture();
        completeFurniture(furniture, rs);
        furnitures.add(furniture);
      }
      return furnitures;
    } catch (SQLException e) {
      throw new DataException("getAllFurnituresBoughtDesc");
    }
  }
}
