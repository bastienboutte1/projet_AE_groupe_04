package be.vinci.pae.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import be.vinci.pae.domain.adresses.Address;
import be.vinci.pae.domain.adresses.AddressFactory;
import be.vinci.pae.exceptions.DataException;
import jakarta.inject.Inject;

public class AddressDAOImpl implements AddressDAO {

  @Inject
  private DalBackendServices dalServices;

  @Inject
  private AddressFactory addressFactory;

  /**
   * Constructor for class.
   */
  public AddressDAOImpl() {
    System.out.println("ADRESS DATA LOADED");
  }

  @Override
  public Address addAdress(Address adress) {
    String addSQL = "INSERT INTO projet.addresses "
        + "(street,building_number,unit_number,postcode,commune,country) "
        + "VALUES (?, ?, ?, ?, ?, ?) RETURNING *;";
    PreparedStatement ps = dalServices.getPreparedStatement(addSQL);
    try {
      ps.setString(1, adress.getStreet());
      ps.setString(2, adress.getNumber());
      ps.setString(3, adress.getBox());
      ps.setString(4, adress.getPostalCode());
      ps.setString(5, adress.getMunicipality());
      ps.setString(6, adress.getCountry());
      return execQuery(ps);
    } catch (SQLException e) {
      throw new DataException("addAdress");
    }
  }

  private Address execQuery(PreparedStatement ps) {
    Address newAddress = addressFactory.getAddress();
    try (ResultSet rs = ps.executeQuery()) {
      if (rs.next()) {
        newAddress.setId(rs.getInt("id_address"));
        newAddress.setStreet(rs.getString("street"));
        newAddress.setNumber(rs.getString("building_number"));
        newAddress.setBox(rs.getString("unit_number"));
        newAddress.setPostalCode(rs.getString("postcode"));
        newAddress.setMunicipality(rs.getString("commune"));
        newAddress.setCountry(rs.getString("country"));
        return newAddress;
      }
    } catch (SQLException e) {
      throw new DataException("execQuery");
    }
    return null;
  }

  @Override
  public Address getAddressById(int id) {
    String selectSQL = "SELECT * FROM projet.addresses WHERE id_address = ?";
    PreparedStatement ps = dalServices.getPreparedStatement(selectSQL);
    try {
      ps.setInt(1, id);
      return execQuery(ps);
    } catch (SQLException e) {
      throw new DataException("getAddressById");
    }
  }
}
