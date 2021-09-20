package be.vinci.pae.domain.furnitures;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = FurnitureImpl.class)
public interface FurnitureDTO {
  enum Condition {
    refuse, restaure, depose, propose, achete, retire, vendu, reserve, livre, emporte, visite
  }

  int getIdFurniture();

  void setIdFurniture(int i);

  int getIdType();

  void setIdType(int idType);

  String getDescription();

  void setDescription(String description);

  String getCondition();

  void setCondition(String string);

  double getPurchasePrice();

  void setPurchasePrice(double purchasePrice);

  double getSellingPrice();

  void setSellingPrice(double sellingPrice);
}
