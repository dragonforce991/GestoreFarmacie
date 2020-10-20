package RestServices;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import Database.UserManagement;
import Model.User;
import Model.UserWrapper;

@Path("user")
public class UserServices {
	
	@POST
	@Path("/insert")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertUser(String jsonString,@Context ContainerRequestContext crc) {
		
		User LoggedUser = (User)crc.getProperty("User");
		Gson g = new Gson();
		User toInsert = g.fromJson(jsonString,User.class);

		if(LoggedUser.getRole().getId() == "0" && toInsert.getRole().getId() != "1") {
			return Response.status(400).build();
		}
		if(LoggedUser.getRole().getId() == "1" && toInsert.getRole().getId()!="0" && toInsert.getRole().getId()!="1" ) {
			UserManagement userManagement = new UserManagement();
			Boolean inserted = userManagement.insertNewUser(toInsert, toInsert.getRole().getId(), toInsert.getPassword());
			Integer state = inserted ? 200 : 400;
			return Response.status(state).build();
		} else {
			return Response.status(400).build();
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
