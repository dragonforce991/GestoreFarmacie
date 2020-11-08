package RestServices;

import java.time.LocalDate;
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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import Database.PazientiManagement;
import Model.Paziente;
import Model.User;
@Path("Pazienti")
public class PazientiService {

	@POST
	@Path("/insert")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertPaziente(String jsonString,@Context ContainerRequestContext crc) {
		try {
			Gson g = new GsonBuilder().registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                public LocalDate deserialize(JsonElement json, java.lang.reflect.Type type, JsonDeserializationContext jsonDeserializationContext) {
                    System.out.println(LocalDate.parse(json.getAsJsonPrimitive().getAsString()));
                    return LocalDate.parse(json.getAsJsonPrimitive().getAsString());
                }
            }).create();
			Paziente p = g.fromJson(jsonString, Paziente.class);
			PazientiManagement pazientiManagement = new PazientiManagement();
			User u = (User) crc.getProperty("User");
			p.setOperatoreRegistrazione(u);
			Integer pazId = pazientiManagement.insertPaziente(p);
			if(pazId != null){
				p.setIdPazienti(String.valueOf(pazId));
				return Response.status(200).entity(p).build();
			}else
				return Response.status(400).build();
		}catch(Exception e) {
			System.out.println(e);
			return Response.status(500).build();
		}
	}
		
	
	@GET
	@Path("/getPazienti")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPazienti(@Context ContainerRequestContext crc) {
		try {
			PazientiManagement pazientiManagement = new PazientiManagement();
			ArrayList<Paziente> pazList = pazientiManagement.getPazienti();
			if(pazList != null)
				return Response.status(200).entity(pazList).build();
			else 
				return Response.status(500).build();
		}catch(Exception e) {
			return Response.status(500).build();
		}
	}
	
	@POST
	@Path("/searchPazienti")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response searchPazienti(String jsonString, @Context ContainerRequestContext crc) {
		try {
			Gson g = new Gson();
			Paziente p = g.fromJson(jsonString, Paziente.class);
			PazientiManagement pazientiManagement = new PazientiManagement();
			ArrayList<Paziente> pazList = pazientiManagement.getPaziente(p);
			if(pazList != null) {
				if(pazList.size()>0) {
					return Response.status(200).entity(pazList).build();
				}
			}
			return Response.status(400).build();
		}catch(Exception e) {
			return Response.status(500).build();
		}
		
	}
}
