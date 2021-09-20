package be.vinci.pae.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import be.vinci.pae.domain.users.UserDTO;
import be.vinci.pae.domain.users.UserFactory;
import be.vinci.pae.exceptions.DataException;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

public class UserDAOImpl implements UserDAO {

  @Inject
  private DalBackendServices dalServices;

  @Inject
  private UserFactory userFactory;

  /**
   * Constructor for class.
   */
  public UserDAOImpl() {
    System.out.println("USER DATA LOADED");
  }

  @Override
  public UserDTO getUserByPseudo(String pseudo) {
    UserDTO user = userFactory.getUser();
    PreparedStatement ps =
        dalServices.getPreparedStatement("SELECT * FROM projet.users WHERE username = ?;");
    try {
      ps.setString(1, pseudo);
      user = execQuery(ps);
      return user;
    } catch (SQLException e) {
      throw new DataException("getUserByPseudo");
    }
  }

  @Override
  public UserDTO getUserById(String id) {
    UserDTO user = userFactory.getUser();
    PreparedStatement ps =
        dalServices.getPreparedStatement("SELECT * FROM projet.users WHERE id_user = ?;");
    try {
      ps.setString(1, id);
      user = execQuery(ps);
      return user;
    } catch (SQLException e) {
      throw new DataException("getUserById");
    }
  }

  /**
   * get the user by his email.
   * 
   * @param email is the email of the user
   */
  @Override
  public UserDTO getUserByEmail(String email) {
    UserDTO user = userFactory.getUser();
    PreparedStatement ps =
        dalServices.getPreparedStatement("SELECT * FROM projet.users WHERE email = ?;");
    try {
      ps.setString(1, email);
      user = execQuery(ps);
      return user;
    } catch (SQLException e) {
      throw new DataException("getUserByEmail");
    }
  }

  private UserDTO execQuery(PreparedStatement ps) {
    UserDTO user = userFactory.getUser();
    try (ResultSet rs = ps.executeQuery()) {
      if (rs.next()) {
        user.setID(rs.getString("id_user"));
        user.setPseudo(rs.getString("username"));
        user.setName(rs.getString("last_name"));
        user.setFirstName(rs.getString("first_name"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getString("role"));
        user.setPassword(rs.getString("password"));
        user.setRegistrationConfirmation(rs.getBoolean("confirmation"));
        user.setAddress(rs.getInt("id_address"));
        user.setRegistrationDate(rs.getTimestamp("register_date").toLocalDateTime());
        return user;
      }
    } catch (SQLException e) {
      throw new DataException("execQuery");
    }
    return null;
  }

  @Override
  public UserDTO addUser(UserDTO user) {
    String addSQL = "INSERT INTO projet.users VALUES (?,?,?,?,?,?,?,?,?,?) RETURNING *;";
    PreparedStatement ps = dalServices.getPreparedStatement(addSQL);
    try {
      ps.setString(1, user.getID());
      ps.setString(2, user.getPseudo());
      ps.setString(3, user.getName());
      ps.setString(4, user.getFirstName());
      ps.setString(5, user.getEmail());
      ps.setString(6, user.getRole());
      ps.setString(7, user.getPassword());
      ps.setInt(8, user.getAddress());
      ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
      ps.setBoolean(10, false);
      return execQuery(ps);
    } catch (SQLException e) {
      throw new DataException("addUser");
    }
  }

  @Override
  public UserDTO updateUser(UserDTO user) {
    String updateSQL = "UPDATE projet.users "
        + "SET username = ?, last_name = ?, first_name = ?, email = ?, role = ?, password = ?, "
        + "id_adresse = ?, confirmation = ? WHERE id_user = ?;";
    PreparedStatement ps = dalServices.getPreparedStatement(updateSQL);
    try {
      ps.setString(1, user.getPseudo());
      ps.setString(2, user.getName());
      ps.setString(3, user.getFirstName());
      ps.setString(4, user.getEmail());
      ps.setObject(5, user.getRole());
      ps.setString(6, user.getPassword());
      ps.setInt(7, user.getAddress());
      ps.setBoolean(8, user.isConfirmedRegistration());
      ps.setString(9, user.getID());
      ps.executeQuery();
    } catch (SQLException e) {
      throw new DataException("updateUser");
    }
    return user;
  }

  @Override
  public UserDTO deleteUser(UserDTO user) {
    String deleteSQL = "DELETE FROM projet.users WHERE id_user = ?;";
    PreparedStatement ps = dalServices.getPreparedStatement(deleteSQL);
    try {
      ps.setString(1, user.getID());
      ps.executeQuery();
    } catch (SQLException e) {
      throw new WebApplicationException("Error while deleting a user", e);
    }

    return user;
  }

  @Override
  public ArrayList<UserDTO> getAllUsers() {
    ArrayList<UserDTO> userList = new ArrayList<UserDTO>();
    String sql = "SELECT * FROM projet.users;";
    PreparedStatement ps = dalServices.getPreparedStatement(sql);
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        UserDTO user = completeUser(rs);
        userList.add(user);
      }
    } catch (SQLException e) {
      throw new DataException("getAllUsers");
    }
    return userList;
  }

  @Override
  public ArrayList<UserDTO> getAllUsersDesc() {
    ArrayList<UserDTO> userList = new ArrayList<UserDTO>();
    String sql = "SELECT * FROM projet.users ORDER BY register_date DESC;";
    PreparedStatement ps = dalServices.getPreparedStatement(sql);
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        UserDTO user = completeUser(rs);
        userList.add(user);
      }
    } catch (SQLException e) {
      throw new DataException("getAllUsersDesc");
    }
    return userList;
  }

  private UserDTO completeUser(ResultSet rs) throws SQLException {
    UserDTO user = userFactory.getUser();
    user.setID(rs.getString("id_user"));
    user.setPseudo(rs.getString("username"));
    user.setName(rs.getString("last_name"));
    user.setFirstName(rs.getString("first_name"));
    user.setEmail(rs.getString("email"));
    user.setRole(rs.getString("role"));
    user.setPassword(rs.getString("password"));
    user.setRegistrationConfirmation(rs.getBoolean("confirmation"));
    user.setRegistrationDate(rs.getTimestamp("register_date").toLocalDateTime());
    return user;
  }

}
