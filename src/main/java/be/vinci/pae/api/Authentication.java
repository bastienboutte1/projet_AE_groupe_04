package be.vinci.pae.api;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.jersey.server.ContainerRequest;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.api.utils.Json;
import be.vinci.pae.domain.adresses.Address;
import be.vinci.pae.domain.adresses.AddressFactory;
import be.vinci.pae.domain.users.User;
import be.vinci.pae.domain.users.UserDTO;
import be.vinci.pae.domain.users.UserFactory;
import be.vinci.pae.domain.users.UserUCC;
import be.vinci.pae.exceptions.RegisterException;
import be.vinci.pae.utils.Config;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("/auths")
public class Authentication {

  private static final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private static final ObjectMapper jsonMapper = new ObjectMapper();
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  @Inject
  private UserUCC userUcc;

  @Inject
  private UserFactory userFactory;

  @Inject
  private AddressFactory addressFactory;

  /**
   * login user.
   * 
   * @Param json json with credentials
   */
  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response login(JsonNode json) {
    // Get and check credentials
    if (!json.hasNonNull("login") || !json.hasNonNull("password")) {
      return Response.status(Status.UNAUTHORIZED).entity("Login and password needed")
          .type(MediaType.TEXT_PLAIN).build();
    }

    String login = json.get("login").asText();
    String password = json.get("password").asText();
    UserDTO user = userUcc.login(login, password);
    if (user == null) {
      return Response.status(Status.UNAUTHORIZED).entity("Login or password incorrect")
          .type(MediaType.TEXT_PLAIN).build();
    }

    LOGGER.setLevel(Level.INFO);
    LOGGER.info(
        "Connexion de " + user.getPseudo() + "\n------------------------------------------------"
            + "---------------------------------------------- \n");

    try {
      return createToken(user);
    } catch (Exception e) {
      throw new WebApplicationException("Unable to create token", e, Status.INTERNAL_SERVER_ERROR);
    }
  }


  /**
   * register user.
   * 
   * @Param json json with user information
   */
  @POST
  @Path("register")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response register(JsonNode json) {

    if (!verifyRegisterInputs(json)) {
      return Response.status(Status.UNAUTHORIZED).entity("One or multiple inputs are missing")
          .type(MediaType.TEXT_PLAIN).build();
    }

    String pseudo = json.get("pseudo").asText();
    String email = json.get("email").asText();

    // Check if pseudo exists
    if (userUcc.checkIfUserExist(pseudo)) {
      return Response.status(Status.CONFLICT).entity("This login is already in use")
          .type(MediaType.TEXT_PLAIN).build();
    }

    // Check if email exists
    if (userUcc.checkIfEmailExist(email)) {
      return Response.status(Status.CONFLICT).entity("This email is already in use")
          .type(MediaType.TEXT_PLAIN).build();
    }

    // create user
    UserDTO user = this.userFactory.getUser();
    user.setPseudo(pseudo);
    user.setFirstName(json.get("fName").asText());
    user.setName(json.get("lName").asText());
    user.setEmail(email);
    user.setPassword(json.get("password").asText());

    // create adresse
    Address adresse = addressFactory.getAddress();
    adresse.setStreet(json.get("street").asText());
    adresse.setNumber(json.get("number").asText());
    adresse.setBox(json.get("box").asText());
    adresse.setPostalCode(json.get("zip").asText());
    adresse.setMunicipality(json.get("city").asText());
    adresse.setCountry(json.get("country").asText());

    // register the user
    user = userUcc.register(user, adresse);

    LOGGER.setLevel(Level.INFO);
    LOGGER.info("Inscription de " + user.getFirstName() + " " + user.getName()
        + " sous le pseudo : " + user.getPseudo() + "\n-----------------------------------------"
        + "----------------------------------------------------- \n");

    try {
      return createToken(user);
    } catch (RegisterException e) {
      throw new WebApplicationException("Unable to create token", e, Status.INTERNAL_SERVER_ERROR);
    }
  }

  private Response createToken(UserDTO user)
      throws IllegalArgumentException, WebApplicationException {
    if (user == null) {
      throw new IllegalArgumentException("user is null");
    }

    String token;
    try {
      token = JWT.create().withIssuer("auth0")
          .withExpiresAt(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)))
          .withClaim("user", user.getID()).sign(jwtAlgorithm);
    } catch (Exception e) {
      throw new WebApplicationException("Unable to create token", e, Status.INTERNAL_SERVER_ERROR);
    }

    User publicUser = Json.filterPublicJsonView((User) user, User.class);
    ObjectNode node = jsonMapper.createObjectNode().put("token", token).putPOJO("user", publicUser);
    return Response.ok(node, MediaType.APPLICATION_JSON).build();
  }

  /**
   * Verify if all inputs needed are given.
   * 
   * @param json JsonNode containing all data
   * @return true if all inputs exists, return false otherwise
   */
  private boolean verifyRegisterInputs(JsonNode json) {
    // verify user data
    if (!json.hasNonNull("fName") || !json.hasNonNull("lName") || !json.hasNonNull("pseudo")
        || !json.hasNonNull("email") || !json.hasNonNull("password")) {
      return false;
    }
    // verify address data
    if (!json.hasNonNull("street") || !json.hasNonNull("number") || !json.hasNonNull("box")
        || !json.hasNonNull("zip") || !json.hasNonNull("city") || !json.hasNonNull("country")) {
      return false;
    }
    return true;
  }

  /**
   * get currently logged user.
   * 
   * @param request info about current user
   */
  @GET
  @Path("me")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public Response getUser(@Context ContainerRequest request) {
    User currentUser = (User) request.getProperty("user");
    return createToken(currentUser);
  }

}
