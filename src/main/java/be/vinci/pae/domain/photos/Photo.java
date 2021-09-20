package be.vinci.pae.domain.photos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(as = PhotoImpl.class)
public interface Photo extends PhotoDTO {
  void setId(int id);

  void setIdFurniture(int id);

  void setBase64Value(String base64Value);

  void setPrefered(boolean bool);

  void setVisible(boolean bool);
}
