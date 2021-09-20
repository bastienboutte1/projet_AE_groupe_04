package be.vinci.pae.domain.type;

public class TypeFurnitureFactoryImpl implements TypeFurnitureFactory {

  @Override
  public TypeFurniture getTypeFurniture() {
    return new TypeFurnitureImpl();
  }
}
