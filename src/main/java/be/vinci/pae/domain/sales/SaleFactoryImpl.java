package be.vinci.pae.domain.sales;

public class SaleFactoryImpl implements SaleFactory {

  @Override
  public SaleDTO getSale() {
    return new SaleImpl();
  }
}
