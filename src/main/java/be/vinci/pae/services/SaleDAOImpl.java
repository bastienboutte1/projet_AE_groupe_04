package be.vinci.pae.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import be.vinci.pae.domain.sales.SaleDTO;
import be.vinci.pae.domain.sales.SaleFactory;
import be.vinci.pae.exceptions.DataException;
import jakarta.inject.Inject;

public class SaleDAOImpl implements SaleDAO {

  @Inject
  private DalBackendServices dalServices;

  @Inject
  private SaleFactory saleFactory;

  /**
   * Constructor for class.
   */
  public SaleDAOImpl() {
    System.out.println("SALE DATA LOADED");
  }

  @Override
  public SaleDTO addSale(SaleDTO sale) {
    try {
      String addSQL =
          "INSERT INTO projet.sales (id_furniture,id_user,date_of_sale,last_selling_price) "
              + "VALUES (?,?,?,?) RETURNING *;";
      PreparedStatement ps = dalServices.getPreparedStatement(addSQL);
      ps.setInt(1, sale.getIdFurniture());
      ps.setString(2, sale.getIdUser());
      ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
      ps.setDouble(4, sale.getLastSellingPrice());
      return execQuery(ps);
    } catch (SQLException e) {
      throw new DataException("addSale");
    }
  }

  @Override
  public ArrayList<SaleDTO> getAllSalesForUserById(String id) {
    try {
      String sql = "SELECT * FROM projet.sales WHERE id_user = ?";
      PreparedStatement ps = dalServices.getPreparedStatement(sql);
      ps.setString(1, id);
      return execQueryMulti(ps);
    } catch (Exception e) {
      throw new DataException("SaleDAO : getAllSalesForUserById(" + id + ")");
    }
  }

  @Override
  public SaleDTO getSaleById(int id) {
    try {
      String sql = "SELECT * FROM projet.sales WHERE id_furniture = ?";
      PreparedStatement ps = dalServices.getPreparedStatement(sql);
      ps.setInt(1, id);
      return execQuery(ps);
    } catch (SQLException e) {
      throw new DataException("getSaleByIdFurniture");
    }
  }

  @Override
  public ArrayList<SaleDTO> getAllSales() {
    return null;
  }

  private SaleDTO execQuery(PreparedStatement ps) {
    try (ResultSet rs = ps.executeQuery()) {
      SaleDTO sale = saleFactory.getSale();
      if (rs.next()) {
        completeSale(rs, sale);
        return sale;
      }
    } catch (SQLException e) {
      throw new DataException("execQuery");
    }
    return null;
  }

  private ArrayList<SaleDTO> execQueryMulti(PreparedStatement ps) {
    try (ResultSet rs = ps.executeQuery()) {
      ArrayList<SaleDTO> list = new ArrayList<SaleDTO>();
      while (rs.next()) {
        SaleDTO sale = saleFactory.getSale();
        completeSale(rs, sale);
        list.add(sale);
      }
      return list;
    } catch (Exception e) {
      throw new DataException("SaleDAO : execQueryMulti");
    }
  }

  private void completeSale(ResultSet rs, SaleDTO sale) throws SQLException {
    sale.setIdSale(rs.getInt("id_sale"));
    sale.setIdFurniture(rs.getInt("id_furniture"));
    sale.setIdUser(rs.getString("id_user"));
    sale.setDateOfSale(rs.getTimestamp("date_of_sale").toLocalDateTime());
    sale.setLastSellingPrice(rs.getDouble("last_selling_price"));
  }
}
