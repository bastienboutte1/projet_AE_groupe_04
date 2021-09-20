package be.vinci.pae.domain.photos;

import java.util.ArrayList;

public interface PhotoUCC {
  ArrayList<PhotoDTO> getAllPictures();

  ArrayList<PhotoDTO> getAllPictures(int id);

  PhotoDTO update(PhotoDTO photo);

  PhotoDTO insert(PhotoDTO photo);

  PhotoDTO delete(int id);

  ArrayList<PhotoDTO> getAllVisiblePictures();

  ArrayList<PhotoDTO> getAllVisiblePictures(int id);

  PhotoDTO getPrefered(int id);
}
