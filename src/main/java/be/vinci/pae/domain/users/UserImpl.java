package be.vinci.pae.domain.users;

import java.time.LocalDateTime;
import org.mindrot.jbcrypt.BCrypt;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import views.Views;

@JsonInclude(JsonInclude.Include.NON_NULL)
class UserImpl implements User {

  @JsonView(Views.Public.class)
  private String id;

  @JsonView(Views.Public.class)
  private int address;

  @JsonView(Views.Public.class)
  private String pseudo;

  @JsonView(Views.Public.class)
  private String name;

  @JsonView(Views.Public.class)
  private String firstName;

  @JsonView(Views.Public.class)
  private String email;

  @JsonView(Views.Internal.class)
  private LocalDateTime registrationDate;

  @JsonView(Views.Public.class)
  private boolean confirmedRegistration;

  @JsonView(Views.Internal.class)
  private String password;

  @JsonView(Views.Public.class)
  private Role role;

  /*
   * METHODS GETTERS & SETTERS
   */

  @Override
  public String getID() {
    return id;
  }

  @Override
  public void setID(String id) {
    this.id = id;
  }

  @Override
  public String getPseudo() {
    return pseudo;
  }

  @Override
  public void setPseudo(String pseudo) {
    this.pseudo = pseudo;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public LocalDateTime getRegistrationDate() {
    return registrationDate;
  }

  @Override
  public void setRegistrationDate(LocalDateTime date) {
    this.registrationDate = date;
  }

  @Override
  public boolean isConfirmedRegistration() {
    return confirmedRegistration;
  }

  @Override
  public void setRegistrationConfirmation(boolean bool) {
    this.confirmedRegistration = bool;
  }

  @Override
  public String getRole() {
    return role.name();
  }

  @Override
  public void setRole(String role) {
    this.role = Role.valueOf(role);
  }

  @Override
  public int getAddress() {
    return address;
  }

  @Override
  public void setAddress(int address) {
    this.address = address;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  /*
   * METHODS : OTHERS
   */
  @Override
  public boolean checkPassword(String password) {
    return BCrypt.checkpw(password, this.password);
  }

  @Override
  public String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }

  @Override
  public boolean checkCanBeAdmin() {
    return true;
  }

  @Override
  public void changeToAdmin() {

  }

  public String toString() {
    return "{id:" + id + ", pseudo:" + pseudo + ", role:" + role + "}";
  }
}
