package RestServices;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Database.MagazzinoManagement;
import Model.Magazzino;
import Model.User;

@Path("Magazzino")
public class MagazzinoService {

	@GET
	@Path("/getMagazzino")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMagazzino(@Context ContainerRequestContext crc) {
		try {
			User u = (User) crc.getProperty("User");
			MagazzinoManagement magazzinoManagement = new MagazzinoManagement();
			ArrayList<Magazzino> magazzinoList = magazzinoManagement.getMagazzino(String.valueOf(u.getFarmacia()));
			if(magazzinoList != null)
				return Response.status(200).entity(magazzinoList).build();
			return Response.status(400).build();
		}catch(Exception e) {
			System.out.println(e);
			return Response.status(500).entity(e).build();
		}
	}
	@POST
	@Path("/insertProduct")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertSingleProduct(@Context ContainerRequestContext crc, String jsonRequest) {
		try {
			Gson g = new Gson();
			User u = (User) crc.getProperty("User");
			String idFarmacia = String.valueOf(u.getFarmacia());
			Magazzino wrapper = g.fromJson(jsonRequest,Magazzino.class );
			MagazzinoManagement magazzinoManagement = new MagazzinoManagement();
			Magazzino m=magazzinoManagement.insertSingleProduct(wrapper.getIdProdotto(), idFarmacia, wrapper.getQuantita());
			if(m != null) {
				return Response.status(200).entity(m).build();
			}
			return Response.status(400).build();
		}catch(Exception e) {
			System.out.println(e);
			return Response.status(500).entity(e).build();
		}
	}
	
	@POST
	@Path("/insertMagazzino")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertMagazzino(@Context ContainerRequestContext crc, String jsonRequest) {
		try {
			Gson g = new Gson();
			wrapperMagazzino wrapperList = g.fromJson(jsonRequest,wrapperMagazzino.class);
			User u = (User) crc.getProperty("User");
			for(Magazzino m :wrapperList.magazzino) {
				m.setIdFarmacia(String.valueOf(u.getFarmacia()));
			}
			MagazzinoManagement magazzinoManagement = new MagazzinoManagement();
			Boolean result = magazzinoManagement.insertMagazzino(wrapperList.magazzino);
			if(result){
				return Response.status(200).entity("OK").build();
			}else if(result == null) {
				return Response.status(400).entity("Errore nella valorizzazione dei valori di IdProdotto e/o IdFarmacia").build();
			}
			return Response.status(400).build();
		}catch(Exception e) {
			System.out.println(e);
			return Response.status(500).entity(e).build();
		}
	}
	
	public class wrapperMagazzino {
		ArrayList<Magazzino> magazzino;
	}
}
