package be.vinci.pae.domain.users;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UserImpl.class)
public interface User extends UserDTO {

  boolean checkPassword(String password);

  String hashPassword(String password);

  boolean checkCanBeAdmin();

  void changeToAdmin();
}
