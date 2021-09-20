package be.vinci.pae.services;

import java.util.ArrayList;

import be.vinci.pae.domain.users.UserDTO;

public interface UserDAO {

  UserDTO getUserByPseudo(String pseudo);

  UserDTO getUserById(String id);

  UserDTO getUserByEmail(String email);

  UserDTO addUser(UserDTO user);

  UserDTO updateUser(UserDTO user);

  UserDTO deleteUser(UserDTO user);

  ArrayList<UserDTO> getAllUsers();

  ArrayList<UserDTO> getAllUsersDesc();
}
