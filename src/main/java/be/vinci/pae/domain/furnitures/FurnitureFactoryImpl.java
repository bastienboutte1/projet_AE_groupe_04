package be.vinci.pae.domain.furnitures;

public class FurnitureFactoryImpl implements FurnitureFactory {

  @Override
  public FurnitureDTO getFurniture() {
    return new FurnitureImpl();
  }
}
