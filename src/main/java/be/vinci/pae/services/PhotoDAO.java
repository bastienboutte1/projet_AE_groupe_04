package be.vinci.pae.services;

import java.util.ArrayList;
import be.vinci.pae.domain.photos.PhotoDTO;

public interface PhotoDAO {
  PhotoDTO create(PhotoDTO photo);

  ArrayList<PhotoDTO> get();

  ArrayList<PhotoDTO> get(int id);

  PhotoDTO update(PhotoDTO photo);

  PhotoDTO delete(int id);

  ArrayList<PhotoDTO> getVisible();

  ArrayList<PhotoDTO> getVisible(int id);

  PhotoDTO getPrefered(int id);
}
