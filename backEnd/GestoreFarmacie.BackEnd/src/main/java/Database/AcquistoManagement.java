package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import Model.Acquisto;
import Model.Farmacia;
import Model.Paziente;
import Model.ProdottiAcquistatiFull;
import Model.Prodotto;
import Model.User;
import RestServices.Utility;

public class AcquistoManagement {

	public Boolean insertAcquisto(Acquisto a) {
		try {
			return insertAcquisto(a,Connect.getConnection());
		}catch(Exception e) {
			System.out.println(e);
			return false;
		}
	}

	public Boolean insertAcquisto(Acquisto a,Connection conn){
		try {
			String sql = "INSERT INTO acquisti(IdPaziente,IdUser,Totale,DataAcquisto,IdFarmacia)VALUES((?),(?),(?),(?),(?))";
			PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			Utility.setStatement(stmt, 1, a.getPaziente().getIdPazienti());
			Utility.setStatement(stmt, 2, a.getUser().getId());
			Utility.setStatement(stmt, 3, a.getTotale());
			Utility.setStatement(stmt, 4, a.getDataAcquisto());
			Utility.setStatement(stmt, 5, a.getUser().getFarmacia());
			stmt.execute();
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			a.setAcquisto(rs.getString(1));
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e);
			return false;
		}
	}
	
	public Boolean insertAcquisti(ArrayList<Acquisto> aList) {
		try {
			return insertAcquisti(aList,Connect.getConnection());
		}catch(Exception e) {
			System.out.println(e);
			return false;
		}
	}
	public Boolean insertAcquisti(ArrayList<Acquisto> aList, Connection conn) {
		try {
			String sql = "INSERT INTO acquisti(IdPaziente,IdUser,Totale,DataAcquisto,IdFarmacia)VALUES((?),(?),(?),(?),(?))";
			PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(sql);
			int count = 0;
			for(Acquisto a : aList) {
				Utility.setStatement(stmt, 1, a.getAcquisto());
				Utility.setStatement(stmt, 2, a.getUser().getId());
				Utility.setStatement(stmt, 3, a.getTotale());
				Utility.setStatement(stmt, 4, a.getDataAcquisto());
				Utility.setStatement(stmt, 5, a.getIdFarmacia());
				stmt.addBatch();
				count++;
				if(count %100 == 0 || count == aList.size()){
					stmt.executeBatch();
				}
			}
			return true;
		}catch(Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	public ArrayList<Acquisto> getAcquisti(String idFarmacia){
		try {
			String sql = "SELECT  IdPaziente, IdUser, Totale,DataAcquisto,IdFarmacia,IdAcquisto,IdProdotto,quantita, user.name, user.surname, Farmacia.Nome, Farmacia.Telefono FROM acquisti inner join acquistiprodotti__r on (idAcquisti = IdAcquisto), user , Farmacia WHERE idFarmacia = Farmacia.Id AND idUser = user.ID ";
			if(idFarmacia != null) {
				sql+= "AND idFarmacia = (?)";
			}
			
			HashMap <String,Prodotto> mapProdotti = new HashMap<String,Prodotto>();
			HashMap <String,Acquisto> mapAcquisti = new HashMap<String,Acquisto>();
			ProdottiManagement prodottiManagement = new ProdottiManagement();
			ArrayList<Prodotto> prodList = prodottiManagement.getProdotti();
			for(Prodotto p : prodList) {
				mapProdotti.put(p.getIdProdotto(), p);
			}
			Connection conn = Connect.getConnection();
			PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(sql);
			if(idFarmacia != null)
				Utility.setStatement(stmt,1, idFarmacia);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				String idAcquisto = rs.getString("IdAcquisto");
				Acquisto a;
				if(mapAcquisti.containsKey(idAcquisto)) {
					a = mapAcquisti.get(idAcquisto);
				}else {
					a = new Acquisto();
					a.setAcquisto(idAcquisto);
					a.setDataAcquisto(Utility.fromDateToLocalDate(rs.getDate("DataAcquisto")));
					
					a.setIdFarmacia(rs.getString("IdFarmacia"));
					Farmacia f = new Farmacia();
					f.setNome(rs.getString("Farmacia.Nome"));
					f.setTelefono(rs.getString("Farmacia.Telefono"));
					f.setId(a.getIdFarmacia());
					a.setFarmacia(f);
					Paziente p = new Paziente();
					p.setIdPazienti(rs.getString("IdPaziente"));
					a.setPaziente(p);
					a.setTotale(rs.getFloat("Totale"));
					User u = new User();
					u.setId(rs.getString("IdUser"));
					u.setName(rs.getString("user.name"));
					u.setSurname(rs.getString("user.surname"));
					u.setFull_name(u.getName() + " " + u.getSurname());
					a.setUser(u);
				}
				ArrayList<ProdottiAcquistatiFull> prodFullList = a.getProdotti();
				if(prodFullList == null)
					prodFullList = new ArrayList<ProdottiAcquistatiFull>(); 
				ProdottiAcquistatiFull prod = new ProdottiAcquistatiFull();
				prod.setQuantita(rs.getInt("quantita"));
				Prodotto pr = prod.getProdotti();
				
				pr=mapProdotti.get(rs.getString("IdProdotto"));
				
				prod.setProdotti(pr);
				prodFullList.add(prod);
				a.setProdotti(prodFullList);
				mapAcquisti.put(idAcquisto, a);
			}
			//return (ArrayList<Acquisto>)mapAcquisto.values();
			ArrayList<Acquisto> retList = new ArrayList<Acquisto>();
			for(String key : mapAcquisti.keySet()) {
				retList.add(mapAcquisti.get(key));
			}
			return retList;
			
			
		}catch(Exception e) {
			System.out.println(e);
			return null;
		}
		
	}
	
}
