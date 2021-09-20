package be.vinci.pae.utils;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import be.vinci.pae.domain.adresses.AddressFactory;
import be.vinci.pae.domain.adresses.AddressFactoryImpl;
import be.vinci.pae.domain.adresses.AddressUCC;
import be.vinci.pae.domain.adresses.AddressUCCImpl;
import be.vinci.pae.domain.furnitures.FurnitureFactory;
import be.vinci.pae.domain.furnitures.FurnitureFactoryImpl;
import be.vinci.pae.domain.furnitures.FurnitureUCC;
import be.vinci.pae.domain.furnitures.FurnitureUCCImpl;
import be.vinci.pae.domain.photos.PhotoFactory;
import be.vinci.pae.domain.photos.PhotoFactoryImpl;
import be.vinci.pae.domain.photos.PhotoUCC;
import be.vinci.pae.domain.photos.PhotoUCCImpl;
import be.vinci.pae.domain.sales.SaleFactory;
import be.vinci.pae.domain.sales.SaleFactoryImpl;
import be.vinci.pae.domain.sales.SaleUCC;
import be.vinci.pae.domain.sales.SaleUCCImpl;
import be.vinci.pae.domain.type.TypeFurniture;
import be.vinci.pae.domain.type.TypeFurnitureFactory;
import be.vinci.pae.domain.type.TypeFurnitureFactoryImpl;
import be.vinci.pae.domain.type.TypeFurnitureImpl;
import be.vinci.pae.domain.type.TypeFurnitureUCC;
import be.vinci.pae.domain.type.TypeFurnitureUCCImpl;
import be.vinci.pae.domain.users.UserFactory;
import be.vinci.pae.domain.users.UserFactoryImpl;
import be.vinci.pae.domain.users.UserUCC;
import be.vinci.pae.domain.users.UserUCCImpl;
import be.vinci.pae.services.AddressDAO;
import be.vinci.pae.services.AddressDAOImpl;
import be.vinci.pae.services.DalBackendServices;
import be.vinci.pae.services.DalServices;
import be.vinci.pae.services.DalServicesImpl;
import be.vinci.pae.services.FurnitureDAO;
import be.vinci.pae.services.FurnitureDAOImpl;
import be.vinci.pae.services.PhotoDAO;
import be.vinci.pae.services.PhotoDAOImpl;
import be.vinci.pae.services.SaleDAO;
import be.vinci.pae.services.SaleDAOImpl;
import be.vinci.pae.services.TypeFurnitureDAO;
import be.vinci.pae.services.TypeFurnitureDAOImpl;
import be.vinci.pae.services.UserDAO;
import be.vinci.pae.services.UserDAOImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    // DalServices
    bind(DalServicesImpl.class).to(DalBackendServices.class).to(DalServices.class)
        .in(Singleton.class);

    // Users
    bind(UserFactoryImpl.class).to(UserFactory.class).in(Singleton.class);
    bind(UserDAOImpl.class).to(UserDAO.class).in(Singleton.class).ranked(1);
    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);

    // Furnitures
    bind(FurnitureFactoryImpl.class).to(FurnitureFactory.class).in(Singleton.class);
    bind(FurnitureDAOImpl.class).to(FurnitureDAO.class).in(Singleton.class);
    bind(FurnitureUCCImpl.class).to(FurnitureUCC.class).in(Singleton.class);

    // Addresses
    bind(AddressDAOImpl.class).to(AddressDAO.class).in(Singleton.class);
    bind(AddressFactoryImpl.class).to(AddressFactory.class).in(Singleton.class);
    bind(AddressUCCImpl.class).to(AddressUCC.class).in(Singleton.class);

    // TypeFunrniture
    bind(TypeFurnitureImpl.class).to(TypeFurniture.class).in(Singleton.class);
    bind(TypeFurnitureFactoryImpl.class).to(TypeFurnitureFactory.class).in(Singleton.class);
    bind(TypeFurnitureUCCImpl.class).to(TypeFurnitureUCC.class).in(Singleton.class);
    bind(TypeFurnitureDAOImpl.class).to(TypeFurnitureDAO.class).in(Singleton.class);

    // Photos
    bind(PhotoDAOImpl.class).to(PhotoDAO.class).in(Singleton.class);
    bind(PhotoFactoryImpl.class).to(PhotoFactory.class).in(Singleton.class);
    bind(PhotoUCCImpl.class).to(PhotoUCC.class).in(Singleton.class);

    // Sales
    bind(SaleDAOImpl.class).to(SaleDAO.class).in(Singleton.class);
    bind(SaleFactoryImpl.class).to(SaleFactory.class).in(Singleton.class);
    bind(SaleUCCImpl.class).to(SaleUCC.class).in(Singleton.class);

  }
}
