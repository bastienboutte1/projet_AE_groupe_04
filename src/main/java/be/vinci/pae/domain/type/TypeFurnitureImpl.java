package be.vinci.pae.domain.type;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import views.Views;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TypeFurnitureImpl implements TypeFurniture {

  @JsonView(Views.Public.class)
  private int idTypeFurniture;

  @JsonView(Views.Public.class)
  private String type;

  /*
   * METHODS GETTERS & SETTERS
   */

  @Override
  public int getIdTypeFurniture() {
    return idTypeFurniture;
  }

  @Override
  public void setIdTypeFurniture(int idTypeFurniture) {
    this.idTypeFurniture = idTypeFurniture;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public void setType(String type) {
    this.type = type;
  }



}
