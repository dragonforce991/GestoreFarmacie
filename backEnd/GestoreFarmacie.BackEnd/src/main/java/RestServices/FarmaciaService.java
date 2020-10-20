package RestServices;
import java.sql.Connection;

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

import Database.Connect;
import Database.FarmaciaManagement;
import Database.UserManagement;
import Model.Farmacia;
import Model.User;
import Model.UserWrapper;

@Path("Farmacia")
public class FarmaciaService {

	public class FarmaciaUserWrapper{
		public Farmacia farmacia;
		public User user;
	}
	@POST
	@Path("/insert")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertFarmaciaAndUser(String jsonString,@Context ContainerRequestContext crc) {
		Connection conn;
		if(!((User)crc.getProperty("User")).getRole().getId().equals("1")) {
			return Response.status(402).build();
		}
		try {
			 conn = Connect.getConnection();
			 conn.setAutoCommit(false);
		}catch(Exception e) {
			return Response.status(400).entity(e).build();
		}
		
		try {
			Gson g = new Gson();
			FarmaciaUserWrapper fuw = g.fromJson(jsonString,FarmaciaUserWrapper.class);
			UserManagement userManagement = new UserManagement();
			FarmaciaManagement farmaciaManagement = new FarmaciaManagement();
			Boolean newFarmacia = false;
			Boolean newUser= userManagement.insertNewUser(fuw.user, fuw.user.getRole().getId(), fuw.user.getPassword());
			if(newUser) {
				User u = userManagement.getUser(fuw.user.getEmail(), fuw.user.getPassword()).getUser();
				fuw.farmacia.setTitolare(u.getId());
				newFarmacia = farmaciaManagement.insertFarmacia(fuw.farmacia);
			}
			if(newUser && newFarmacia) {
				conn.commit();
				return Response.status(200).build();
			} else {
				conn.rollback();
				return Response.status(400).build();
			}
		}catch(Exception e) {
			System.out.println(e);
			try {
				conn.rollback();
			}catch(Exception ex) {
				System.out.println(ex);
			}
			return Response.status(500).entity(e).build();
		}
		
	}
	
}
