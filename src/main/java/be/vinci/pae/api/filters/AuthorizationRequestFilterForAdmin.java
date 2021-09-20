package be.vinci.pae.api.filters;

import java.io.IOException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import be.vinci.pae.domain.users.User;
import be.vinci.pae.domain.users.UserDTO;
import be.vinci.pae.domain.users.UserDTO.Role;
import be.vinci.pae.services.DalServices;
import be.vinci.pae.services.UserDAO;
import be.vinci.pae.utils.Config;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Singleton
@Provider
@AuthorizeForAdmin
public class AuthorizationRequestFilterForAdmin implements ContainerRequestFilter {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final JWTVerifier jwtVerifier =
      JWT.require(this.jwtAlgorithm).withIssuer("auth0").build();

  @Inject
  private UserDAO dataService;

  @Inject
  private DalServices dalServices;

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    String token = requestContext.getHeaderString("Authorization");
    DecodedJWT decodedToken = null;
    try {
      decodedToken = this.jwtVerifier.verify(token);
    } catch (Exception e) {
      requestContext.abortWith(
          Response.status(Response.Status.UNAUTHORIZED).entity("Token has expired").build());
      // silent token
    }

    try {
      dalServices.startTransaction();
      User user = (User) dataService.getUserById(decodedToken.getClaim("user").asString());
      if (user.getRole().equals(Role.patron.name())) {
        requestContext.setProperty("user", (UserDTO) user);
      } else {
        throw new IllegalAccessException("user is not an admin");
      }
      dalServices.commitTransaction();
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
          .entity("You need to be admin to access this ressource").build());
    }
  }
}
