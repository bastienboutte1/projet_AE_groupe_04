package be.vinci.pae.domain.users;

import java.util.ArrayList;

import be.vinci.pae.domain.adresses.Address;

public interface UserUCC {
  UserDTO register(UserDTO user, Address address);

  UserDTO login(String pseudo, String password);

  boolean checkIfUserExist(String pseudo);

  boolean checkIfEmailExist(String email);

  boolean checkEmailFormat(String email);

  ArrayList<UserDTO> getAllUser();

  ArrayList<UserDTO> getAllUserDesc();

  UserDTO getUserByUsername(String username);

  UserDTO getUserById(String id);
}
