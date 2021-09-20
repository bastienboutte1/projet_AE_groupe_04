package be.vinci.pae.domain.photos;

public class PhotoFactoryImpl implements PhotoFactory {

  @Override
  public PhotoDTO getPhoto() {
    return new PhotoImpl();
  }

}
