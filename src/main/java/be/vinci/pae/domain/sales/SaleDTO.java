package be.vinci.pae.domain.sales;

import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = SaleImpl.class)
public interface SaleDTO {

  int getIdSale();

  void setIdSale(int idSale);

  int getIdFurniture();

  void setIdFurniture(int idFurniture);

  String getIdUser();

  void setIdUser(String idUser);

  LocalDateTime getDateOfSale();

  void setDateOfSale(LocalDateTime dateOfSale);

  double getLastSellingPrice();

  void setLastSellingPrice(double lastSellingPrice);
}
