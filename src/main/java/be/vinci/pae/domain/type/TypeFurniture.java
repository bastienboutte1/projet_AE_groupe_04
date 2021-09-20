package be.vinci.pae.domain.type;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = TypeFurniture.class)
public interface TypeFurniture {
  int getIdTypeFurniture();

  void setIdTypeFurniture(int idTypeFurniture);

  String getType();

  void setType(String type);
}
