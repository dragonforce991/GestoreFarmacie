package RestServices;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import Database.UserManagement;
import Model.User;

@Path("user")
public class UserServices {
	
	@GET
	@Path("/getUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers(@Context ContainerRequestContext crc) {
		try {
			UserManagement userManagement = new UserManagement();
			User u = (User) crc.getProperty("User");
			ArrayList<User> uList = new ArrayList<User>();
			if(u.getRole().getId().equals("1"))
				uList = userManagement.getUsers(null);
			else
				uList = userManagement.getUsers(String.valueOf(u.getFarmacia()));
			if(uList != null)
				return Response.status(200).entity(uList).build();
			return Response.status(400).build();
		}catch(Exception e) {
			return Response.status(500).build();
		}
	}
	
	
	@GET
	@Path("/getUserById")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserById(@QueryParam("Id") String id, @Context ContainerRequestContext crc) {
		try {
			UserManagement userManagement = new UserManagement();
			User u = userManagement.getUser(id);
			if(u != null)
				return Response.status(200).entity(u).build();
			return Response.status(400).entity("Id non trovato").build();
		}catch(Exception e) {
			return Response.status(500).build();
		}
	}
	
	@POST
	@Path("/insert")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertUser(String jsonString,@Context ContainerRequestContext crc) {
		try {
			User LoggedUser = (User)crc.getProperty("User");
			Gson g = new Gson();
			User toInsert = g.fromJson(jsonString,User.class);
	
			if(LoggedUser.getRole().getId().equals("1") && !toInsert.getRole().getId().equals("2")) {
				System.out.println("primo");
				return Response.status(400).entity("Operazione non permessa all'utente").build();
				
			}
			if(LoggedUser.getRole().getId().equals("2") && !toInsert.getRole().getId().equals("1") && !toInsert.getRole().getId().equals("2") ) {
				UserManagement userManagement = new UserManagement();
				Integer userId = userManagement.insertNewUser(toInsert, toInsert.getRole().getId(), toInsert.getPassword());
				Integer state = userId != null ? 200 : 400;
				String entity = state == 200 ? "OK" : "Errore generico";
				return Response.status(state).entity(entity).build();
			} else {
				System.out.println("secondo");
				return Response.status(400).entity("Operazione non permessa all'utente").build();
			}
		}
		catch(Exception e) {
			System.out.println(e.getStackTrace());
			System.out.println(e.getCause());
			return Response.status(500).build();
		}
	}
	@Path("/getUser")
 	@GET
    @Produces(MediaType.APPLICATION_JSON)
 	public Response getUser(@Context ContainerRequestContext crc) {
 		User u= (User)crc.getProperty("User");
	 	return Response.status(200).entity(u).build();
 	}

}
