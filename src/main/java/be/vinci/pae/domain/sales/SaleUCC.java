package be.vinci.pae.domain.sales;

import java.util.ArrayList;

public interface SaleUCC {

  /**
   * Add a sale.
   */
  SaleDTO addSale(SaleDTO sale);

  ArrayList<SaleDTO> getAllSalesForUserById(String id);

  SaleDTO checkSale(int id);

  SaleDTO getSaleByFurnitureId(int id);

}
