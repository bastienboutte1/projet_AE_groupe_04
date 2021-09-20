package be.vinci.pae.domain.sales;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import views.Views;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SaleImpl implements Sale {

  @JsonView(Views.Public.class)
  private int idSale;

  @JsonView(Views.Public.class)
  private int idFurniture;

  @JsonView(Views.Public.class)
  private String idUser;

  @JsonView(Views.Public.class)
  private LocalDateTime dateOfSale;

  @JsonView(Views.Public.class)
  private double lastSellingPrice;

  @Override
  public int getIdSale() {
    return idSale;
  }

  @Override
  public void setIdSale(int idSale) {
    this.idSale = idSale;
  }

  @Override
  public int getIdFurniture() {
    return idFurniture;
  }

  @Override
  public void setIdFurniture(int idFurniture) {
    this.idFurniture = idFurniture;
  }

  @Override
  public String getIdUser() {
    return idUser;
  }

  @Override
  public void setIdUser(String idUser) {
    this.idUser = idUser;
  }

  @Override
  public LocalDateTime getDateOfSale() {
    return dateOfSale;
  }

  @Override
  public void setDateOfSale(LocalDateTime dateOfSale) {
    this.dateOfSale = dateOfSale;
  }

  @Override
  public double getLastSellingPrice() {
    return lastSellingPrice;
  }

  @Override
  public void setLastSellingPrice(double lastSellingPrice) {
    this.lastSellingPrice = lastSellingPrice;
  }

}
