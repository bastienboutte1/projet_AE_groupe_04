package be.vinci.pae.domain.photos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(as = PhotoImpl.class)
public interface PhotoDTO {
  int getId();

  int getIdFurniture();

  String getBase64Value();

  boolean isVisible();

  boolean isPrefered();
}
