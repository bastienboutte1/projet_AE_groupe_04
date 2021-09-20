package be.vinci.pae.services;

public interface DalServices {

  void startTransaction() throws IllegalArgumentException;

  void commitTransaction();

  void rollbackTransaction();
}

