package be.vinci.pae.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import be.vinci.pae.domain.photos.Photo;
import be.vinci.pae.domain.photos.PhotoDTO;
import be.vinci.pae.domain.photos.PhotoFactory;
import be.vinci.pae.exceptions.DataException;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

public class PhotoDAOImpl implements PhotoDAO {

  @Inject
  private DalBackendServices dalServices;

  @Inject
  private PhotoFactory photoFactory;

  public PhotoDAOImpl() {
    System.out.println("PHOTO DATA LOADED");
  }


  @Override
  public ArrayList<PhotoDTO> getVisible() {
    try {
      String sql = "SELECT * FROM projet.photos WHERE visible=true";
      PreparedStatement ps = dalServices.getPreparedStatement(sql);
      return execQueryMulti(ps);
    } catch (Exception e) {
      throw new DataException("Photo : getVisible()");
    }
  }

  @Override
  public ArrayList<PhotoDTO> getVisible(int id) {
    try {
      String sql = "SELECT * FROM projet.photos WHERE visible=true AND id_furniture = ?;";
      PreparedStatement ps = dalServices.getPreparedStatement(sql);
      ps.setInt(1, id);
      return execQueryMulti(ps);
    } catch (Exception e) {
      throw new DataException("Photo : getVisible(" + id + ")");
    }
  }

  /**
   * Insert a new photo in DB.
   */
  @Override
  public PhotoDTO create(PhotoDTO photo) {
    try {
      String sql = "INSERT INTO projet.photos VALUES (DEFAULT,?,?,?,?) RETURNING *;";
      PreparedStatement ps = dalServices.getPreparedStatement(sql);
      ps.setInt(1, photo.getIdFurniture());
      ps.setString(2, photo.getBase64Value());
      ps.setBoolean(3, photo.isVisible());
      ps.setBoolean(4, photo.isPrefered());
      return execQuery(ps);
    } catch (Exception e) {
      throw new DataException("Photo : create");
    }
  }

  /**
   * Get all photos from DB.
   */
  @Override
  public ArrayList<PhotoDTO> get() {
    try {
      String sql = "SELECT * FROM projet.photos";
      PreparedStatement ps = dalServices.getPreparedStatement(sql);
      return execQueryMulti(ps);
    } catch (Exception e) {
      throw new DataException("Photo : get()");
    }
  }

  /**
   * Return a list of all the pictures for a specific furniture.
   * 
   * @param id : id_furniture
   * @return list : list of all photos for a furniture
   */
  @Override
  public ArrayList<PhotoDTO> get(int id) {
    try {
      String sql = "SELECT * FROM projet.photos WHERE id_furniture = ?";
      PreparedStatement ps = dalServices.getPreparedStatement(sql);
      ps.setInt(1, id);
      return execQueryMulti(ps);
    } catch (Exception e) {
      throw new DataException("PhotoDAO : get(int id)", e);
    }
  }

  /**
   * execute query but return an array of PhotoDTO.
   * 
   * @param ps : sql command to execute
   * @return photoList : list of the returned query
   */
  private ArrayList<PhotoDTO> execQueryMulti(PreparedStatement ps) {
    ArrayList<PhotoDTO> photoList = new ArrayList<PhotoDTO>();
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        Photo photo = (Photo) photoFactory.getPhoto();
        photo.setId(rs.getInt("id_photo"));
        photo.setIdFurniture(rs.getInt("id_furniture"));
        photo.setBase64Value(rs.getString("base64_value"));
        photo.setVisible(rs.getBoolean("visible"));
        photo.setPrefered(rs.getBoolean("prefered"));
        photoList.add(photo);
      }
    } catch (Exception e) {
      throw new DataException();
    }
    return photoList;
  }

  @Override
  public PhotoDTO update(PhotoDTO photo) {
    try {
      String sql =
          "UPDATE projet.photos SET visible = ?, prefered = ? WHERE id_photo = ? RETURNING *;";
      PreparedStatement ps = dalServices.getPreparedStatement(sql);
      ps.setBoolean(1, photo.isVisible());
      ps.setBoolean(2, photo.isPrefered());
      ps.setInt(3, photo.getId());
      return execQuery(ps);
    } catch (Exception e) {
      throw new DataException("PhotoDAO : update", e);
    }
  }

  @Override
  public PhotoDTO delete(int id) {
    try {
      String sql = "DELETE FROM projet.photos WHERE id_photo = ? RETURNING *";
      PreparedStatement ps = dalServices.getPreparedStatement(sql);
      ps.setInt(1, id);
      return execQuery(ps);
    } catch (Exception e) {
      throw new WebApplicationException("PhotoDAO : delete", e);
    }
  }

  /**
   * Execute a query and return 1 photo.
   * 
   * @param ps : preparedStatement to get executed
   * @return photo : photo returned by db after executing the ps
   */
  private PhotoDTO execQuery(PreparedStatement ps) {
    Photo photo = (Photo) photoFactory.getPhoto();
    try (ResultSet rs = ps.executeQuery()) {
      if (rs.next()) {
        photo.setId(rs.getInt("id_photo"));
        photo.setIdFurniture(rs.getInt("id_furniture"));
        photo.setBase64Value(rs.getString("base64_value"));
        photo.setVisible(rs.getBoolean("visible"));
        photo.setPrefered(rs.getBoolean("prefered"));
      }
      return (PhotoDTO) photo;
    } catch (Exception e) {
      throw new DataException("PhotoDAO : execQuery", e);
    }
  }

  @Override
  public PhotoDTO getPrefered(int id) {
    try {
      String sql = "SELECT * FROM projet.photos WHERE id_furniture = ? AND prefered = true;";
      PreparedStatement ps = dalServices.getPreparedStatement(sql);
      ps.setInt(1, id);
      return execQuery(ps);
    } catch (Exception e) {
      throw new DataException("PhotoDAO : getPrefered(" + id + ")");
    }
  }
}
