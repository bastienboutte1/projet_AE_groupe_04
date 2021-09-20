package be.vinci.pae.services;

import java.util.ArrayList;
import be.vinci.pae.domain.sales.SaleDTO;

public interface SaleDAO {

  SaleDTO addSale(SaleDTO sale);

  ArrayList<SaleDTO> getAllSalesForUserById(String id);

  ArrayList<SaleDTO> getAllSales();

  SaleDTO getSaleById(int id);

}
