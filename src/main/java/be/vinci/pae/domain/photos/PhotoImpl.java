package be.vinci.pae.domain.photos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import views.Views;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhotoImpl implements Photo {

  @JsonView(Views.Public.class)
  private int id;

  @JsonView(Views.Public.class)
  private int idFurniture;

  @JsonView(Views.Public.class)
  private String base64Value;

  @JsonView(Views.Public.class)
  private boolean visible;

  @JsonView(Views.Public.class)
  private boolean prefered;


  /*
   * GETTERS
   */

  @Override
  public int getId() {
    return id;
  }

  @Override
  public int getIdFurniture() {
    return idFurniture;
  }

  @Override
  public String getBase64Value() {
    return base64Value;
  }

  @Override
  public boolean isVisible() {
    return visible;
  }

  @Override
  public boolean isPrefered() {
    return prefered;
  }

  /*
   * SETTERS
   */

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public void setIdFurniture(int id) {
    this.idFurniture = id;
  }

  @Override
  public void setBase64Value(String base64Value) {
    this.base64Value = base64Value;
  }

  @Override
  public void setPrefered(boolean bool) {
    // uniquement une seule photo pr�f�r�e
    // verifier que toutes les autres photos sont !preferes
    this.prefered = bool;
  }

  @Override
  public void setVisible(boolean bool) {
    this.visible = bool;
  }
}
