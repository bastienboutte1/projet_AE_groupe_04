package be.vinci.pae.domain.furnitures;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import views.Views;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FurnitureImpl implements Furniture {

  @JsonView(Views.Public.class)
  private int idFurniture;

  @JsonView(Views.Public.class)
  private int idType;

  @JsonView(Views.Public.class)
  private String description;

  @JsonView(Views.Public.class)
  private Condition condition;

  @JsonView(Views.Public.class)
  private double purchasePrice;

  @JsonView(Views.Public.class)
  private double sellingPrice;



  @Override
  public int getIdFurniture() {
    return idFurniture;
  }

  @Override
  public void setIdFurniture(int idFurniture) {
    this.idFurniture = idFurniture;
  }

  @Override
  public int getIdType() {
    return idType;
  }

  @Override
  public void setIdType(int idType) {
    this.idType = idType;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String getCondition() {
    return condition.name();
  }

  @Override
  public void setCondition(String condition) {
    this.condition = Condition.valueOf(condition);
  }

  @Override
  public double getPurchasePrice() {
    return purchasePrice;
  }

  @Override
  public void setPurchasePrice(double purchasePrice) {
    this.purchasePrice = purchasePrice;
  }

  @Override
  public double getSellingPrice() {
    return sellingPrice;
  }

  @Override
  public void setSellingPrice(double sellingPrice) {
    this.sellingPrice = sellingPrice;
  }
}
