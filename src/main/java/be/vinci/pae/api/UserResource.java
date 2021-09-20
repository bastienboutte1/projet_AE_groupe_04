package be.vinci.pae.api;

import java.util.ArrayList;

import be.vinci.pae.api.filters.AuthorizeForAdmin;
import be.vinci.pae.domain.users.UserDTO;
import be.vinci.pae.domain.users.UserUCC;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("/users")
public class UserResource {

  @Inject
  private UserUCC userUCC;

  /**
   * get all users.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeForAdmin
  public ArrayList<UserDTO> getAllUsers() {
    return userUCC.getAllUser();
  }

  /**
   * get all users order by register date desc.
   */
  @GET
  @Path("/sorted-desc")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeForAdmin
  public ArrayList<UserDTO> getAllUsersDesc() {
    return userUCC.getAllUserDesc();
  }

  /**
   * check if pseudo is available.
   */
  @GET
  @Path("pseudo-available/{pseudo}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response isPseudoAvailible(@PathParam("pseudo") String pseudo) {
    if (userUCC.checkIfUserExist(pseudo)) {
      return Response.status(Status.CONFLICT).entity("Pseudo is already used")
          .type(MediaType.TEXT_PLAIN).build();
    }
    return Response.status(Status.OK).entity("Pseudo is not used").type(MediaType.TEXT_PLAIN)
        .build();
  }

  /**
   * check if email is available.
   */
  @GET
  @Path("email-available/{email}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response isEmailAvailible(@PathParam("email") String email) {
    try {
      if (userUCC.checkIfEmailExist(email)) {
        return Response.status(Status.CONFLICT).entity("Email is already used")
            .type(MediaType.TEXT_PLAIN).build();
      }
    } catch (IllegalArgumentException e) {
      return Response.status(Status.BAD_REQUEST).entity("Email not good format")
          .type(MediaType.TEXT_PLAIN).build();
    }

    return Response.status(Status.OK).entity("Email is not used").type(MediaType.TEXT_PLAIN)
        .build();
  }

  /**
   * Get the user with username passed.
   * 
   * @param username of the user wanted
   * @return the user with thr corresponding u
   */
  @GET
  @Path("/{username}")
  @Produces(MediaType.APPLICATION_JSON)
  public UserDTO getUserByUsername(@PathParam("username") String username) {
    UserDTO user = this.userUCC.getUserByUsername(username);
    if (user == null) {
      throw new WebApplicationException("User avec pseudo = " + username + " na pas ete trouve",
          null, Status.NOT_FOUND);
    }
    return user;
  }
}
