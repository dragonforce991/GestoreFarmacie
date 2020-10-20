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
@Path("")
public class Login {
	 	@POST
	 	@Path("/Login")
	    @Produces(MediaType.APPLICATION_JSON)
	 	@Consumes(MediaType.APPLICATION_JSON)
	    public Response doLogin(String jsonString) {
	 		Gson g = new Gson(); 
	 		LoginWrapper wrapper= g.fromJson(jsonString, LoginWrapper.class);
	 		UserManagement userManagement = new UserManagement();
	 		UserWrapper userWrapper = userManagement.getUser(wrapper.email, wrapper.password);
	        NewCookie cookie = new NewCookie("accessToken", userWrapper.getAccessToken());
	        return Response.status(200).entity(userWrapper).cookie(cookie).build();
	    }
	 	
	 	public class LoginWrapper{
	 		String email;
	 		String password;
	 		public LoginWrapper(String email, String password) {
	 			this.email = email;
	 			this.password=password;
	 		}
	 	}
}
