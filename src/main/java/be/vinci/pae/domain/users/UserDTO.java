package be.vinci.pae.domain.users;

import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UserImpl.class)
public interface UserDTO {
  enum Role {
    patron, antiquaire, client
  }

  String getID();

  void setID(String id);

  String getPseudo();

  void setPseudo(String pseudo);

  String getName();

  void setName(String name);

  String getFirstName();

  void setFirstName(String firstName);

  String getEmail();

  void setEmail(String email);

  LocalDateTime getRegistrationDate();

  void setRegistrationDate(LocalDateTime date);

  boolean isConfirmedRegistration();

  void setRegistrationConfirmation(boolean bool);

  String getRole();

  void setRole(String role);

  int getAddress();

  void setAddress(int address);

  String getPassword();

  void setPassword(String password);

}
