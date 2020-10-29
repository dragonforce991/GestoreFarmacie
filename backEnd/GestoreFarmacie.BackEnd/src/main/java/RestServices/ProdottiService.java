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

import Database.ProdottiManagement;
import Model.Prodotto;
import Model.User;

@Path("Product")
public class ProdottiService {

	@GET
	@Path("/getProducts")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProducts(@Context ContainerRequestContext crc) {
		try {
			ProdottiManagement prodottiManagement = new ProdottiManagement();
			User u = (User) crc.getProperty("User");
			ArrayList<Prodotto> prodList = prodottiManagement.getProdotti(u);
			if(prodList != null)
				return Response.status(200).entity(prodList).build();
			return Response.status(400).entity("Nessun prodotto disponibile").build();
		}catch(Exception e) {
			System.out.println(e);
			return Response.status(500).entity(e).build();
		}
	}
	
	@GET
	@Path("/getProductById")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProductById(@Context ContainerRequestContext crc,@QueryParam("Id") String Id) {
		try {
			ProdottiManagement prodottiManagement = new ProdottiManagement();
			User u = (User) crc.getProperty("User");
			Prodotto prod = prodottiManagement.getProdotto(Id,u);
			if(prod == null) {
				return Response.status(400).entity("Nessun prodotto disponibile con l'id indicato").build();
			}
			return Response.status(200).entity(prod).build();
		}catch(Exception e) {
			System.out.println(e);
			return Response.status(500).entity(e).build();
		}
	}
	
	@GET
	@Path("/getProductByCode")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProductByCode(@Context ContainerRequestContext crc,@QueryParam("Codice") String Codice) {
		try {
			ProdottiManagement prodottiManagement = new ProdottiManagement();
			User u = (User) crc.getProperty("User");
			Prodotto prod = prodottiManagement.getProdottoByCodice(Codice,u);
			if(prod == null) {
				return Response.status(400).entity("Nessun prodotto disponibile con il codice indicato").build();
			}
			return Response.status(200).entity(prod).build();
		}catch(Exception e) {
			System.out.println(e);
			return Response.status(500).entity(e).build();
		}
	}
	
	@POST
	@Path("/insertProduct")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertProduct(@Context ContainerRequestContext crc, String jsonString) {
		try {
			Gson g = new Gson();
			ProdottiManagement prodottiManagement = new ProdottiManagement();
			Prodotto p = g.fromJson(jsonString, Prodotto.class);
			Integer id = prodottiManagement.insertProdotto(p);
			if(id != null) {
				return Response.status(200).entity(id).build();
			}
			return Response.status(400).build();
		}catch(Exception e) {
			System.out.println(e);
			return Response.status(500).entity(e).build();
		}
	}
	
}
