package RestServices;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

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

import Database.AcquistoManagement;
import Database.Connect;
import Database.ProdottiAcquistatiManagement;
import Database.ProdottiManagement;
import Model.Acquisto;
import Model.ProdottiAcquistati;
import Model.ProdottiAcquistatiFull;
import Model.Prodotto;
import Model.User;

@Path("Acquisti")
public class AcquistiService {

	
	private static Boolean containsRicetta(ArrayList<ProdottiAcquistatiFull> prodotti, HashMap<String,Prodotto> mapProdotti) {
		for(ProdottiAcquistatiFull prod : prodotti) {
			if((mapProdotti.get(String.valueOf(prod.getProdotti().getIdProdotto()))).getObbligoRicetta())
					return true;
		}
		return false;
	}
	
	private static Boolean checkUser(Boolean ricettaEnabled, Boolean ricetta) {
		if(ricetta)
			return ricettaEnabled;
		return true;
			
	}
	private static String isRequestValid(Acquisto a,Boolean ricetta,HashMap<String,Prodotto>prodMap) {
		float totale = 0;
		
		for(ProdottiAcquistatiFull prod : a.getProdotti()) {
			if(prodMap.get(String.valueOf(prod.getProdotti().getIdProdotto())).getObbligoRicetta()){
				if(prod.getCodice_Regionale_Medico() == null || prod.getDataRicetta() == null)
					return "Per vendere un prodotto con ricetta bisogna specificare la ricetta";
			}
			totale+= prod.getQuantita()*prodMap.get(prod.getProdotti().getIdProdotto()).getCostoUnitario();
		} 
		if(totale != a.getTotale() || totale <= 0) {
			return "Errore nel calcolo del totale";
		}
		if(ricetta) {
			if(a.getPaziente() == null)
				return "Bisogna specificare il paziente per vendere prodotti con ricetta";
		}
		
		return "SUCCESS";
	}
	@POST
	@Path("/newAcquisto")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertNewAcquisto(String jsonString,@Context ContainerRequestContext crc) {
		Connection conn;
		try {
			conn=Connect.getConnection();
		}catch(Exception e) {
			return Response.status(500).entity(e).build();
		}
		try {
			
			User u = ((User)crc.getProperty("User"));
			Gson g = new GsonBuilder().registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                public LocalDate deserialize(JsonElement json, java.lang.reflect.Type type, JsonDeserializationContext jsonDeserializationContext) {
                    System.out.println(LocalDate.parse(json.getAsJsonPrimitive().getAsString()));
                    return LocalDate.parse(json.getAsJsonPrimitive().getAsString());
                }
            }).create();
			Acquisto a = g.fromJson(jsonString,Acquisto.class);
			ProdottiManagement pm = new ProdottiManagement();
			HashMap<String,Prodotto> mapProdotti = new HashMap<String,Prodotto>();
			for(Prodotto p : pm.getProdotti()){
				mapProdotti.put(p.getIdProdotto(),p);
			}
			Boolean ricetta = containsRicetta(a.getProdotti(),mapProdotti);
			Boolean isUserAllowed = checkUser(u.getRole().getRicettaEnabled(),ricetta);
			if(!isUserAllowed) {
				return Response.status(401).entity("Utente non autorizzato").build();
			}
			String response = isRequestValid(a, ricetta, mapProdotti);
			if(!response.equals("SUCCESS")) {
				return Response.status(400).entity(response).build();
			}
			//a.setIdFarmacia(String.valueOf(u.getFarmacia()));
			a.setUser(u);
			conn.setAutoCommit(false);
			AcquistoManagement acquistoManagement = new AcquistoManagement();
			Boolean result = acquistoManagement.insertAcquisto(a, conn);
			if(result) {
				ArrayList<ProdottiAcquistati> prodAcqList = new ArrayList<ProdottiAcquistati>();
				for(ProdottiAcquistatiFull pr : a.getProdotti()) {
					ProdottiAcquistati p = new ProdottiAcquistati();
					p.setCodice_Regionale_Medico(pr.getCodice_Regionale_Medico());
					p.setDataRicetta(pr.getDataRicetta());
					p.setIdAcquisto(a.getAcquisto());
					p.setIdProdotto(pr.getProdotti().getIdProdotto());
					p.setQuantita(pr.getQuantita());
					prodAcqList.add(p);
				}
				ProdottiAcquistatiManagement prodottiAcquistatiManagement = new ProdottiAcquistatiManagement();
				String res = prodottiAcquistatiManagement.insertProdottiAcquistati(prodAcqList, conn,String.valueOf(u.getFarmacia()), a.getAcquisto());
				System.out.println(res);
				if(res.equals("OK")) {
					conn.commit();
					return Response.status(200).build();
				}else {
					conn.rollback();
					return Response.status(400).entity(res).build();
				}
			}
			conn.rollback();
			return Response.status(400).build();
			
		}catch(Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				System.out.println(e1);
			}
			System.out.println(e);
			return Response.status(500).entity(e).build();
		}
		
	}
	
	@GET
	@Path("/getAcquisti")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAcquisti(@Context ContainerRequestContext crc) {
		User u = ((User)crc.getProperty("User"));
		AcquistoManagement acquistoManagement = new AcquistoManagement();
		ArrayList<Acquisto> acquisti;
		if(u.getRole().getId().equals("1")) {
			acquisti = acquistoManagement.getAcquisti(null);
		}else {
			acquisti = acquistoManagement.getAcquisti(String.valueOf(u.getFarmacia()));
		}
		if(acquisti == null)
			return Response.status(500).build();
		else 
			return Response.status(200).entity(acquisti).build();
		
	}
}
