package Server;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

import Model.User;
import RestServices.Utility;


public class middleware implements ContainerRequestFilter{

	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		if(!requestContext.getUriInfo().getPath().equals("Login")) {
			String accessToken = requestContext.getCookies().get("accessToken").getValue();
			System.out.println("access Token = " + accessToken);
			try {
				
				User u = Utility.getUserFromJWT(accessToken);
				requestContext.setProperty("User",u);
				System.out.println("Middleware ok");
			}catch(Exception e) {
				requestContext.abortWith(Response.status(401).build());
			}
		}
	}

	
}
