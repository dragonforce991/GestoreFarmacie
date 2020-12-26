package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Model.Prodotto;
import Model.User;
import RestServices.Utility;

public class ProdottiManagement {
	//metodo per servizio acquisto
	public ArrayList<Prodotto> getProdotti(){
		try {
			ArrayList<Prodotto> pList = new ArrayList<Prodotto>();
			Connection conn = Connect.getConnection();
			String sql = "SELECT idProdotto,ObbligoRicetta,CostoUnitario,Nome,Azienda,Descizione,Codice,ParoleChiave FROM prodotti";
			PreparedStatement stmt =(PreparedStatement) conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				pList.add(getProductFromResultSet(rs));
			}
			return pList;
		}catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	public ArrayList<Prodotto> getProdotti(User u){
		try {
			ArrayList<Prodotto> pList = new ArrayList<Prodotto>();
			Connection conn = Connect.getConnection();
			String sql = "SELECT idProdotto,ObbligoRicetta,CostoUnitario,Nome,Azienda,Descizione,Codice,ParoleChiave FROM prodotti";
			if(!u.getRole().getRicettaEnabled())
				sql += " WHERE ObbligoRicetta = false";
			PreparedStatement stmt =(PreparedStatement) conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				pList.add(getProductFromResultSet(rs));
			}
			return pList;
		}catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	private Prodotto getProductFromResultSet(ResultSet rs) throws SQLException {
		Prodotto p = new Prodotto();
		p.setAzienda(rs.getString("Azienda"));
		p.setCodice(rs.getString("Codice"));
		p.setCostoUnitario(rs.getFloat("costoUnitario"));
		p.setDescizione(rs.getString("Descizione"));
		p.setIdProdotto(rs.getString("IdProdotto"));
		p.setNome(rs.getString("Nome"));
		p.setObbligoRicetta(rs.getBoolean("ObbligoRicetta"));
		String parole = rs.getString("ParoleChiave");
		if(parole!=null) {
			ArrayList<String> chiavi = new ArrayList<String>();
			for(String s : parole.split("-")) {
				chiavi.add(s);
			}
			p.setParoleChiave(chiavi);
		}
		
		return p;
	}
	public Prodotto getProdotto(String id, User u){
		try {
			Connection conn = Connect.getConnection();
			PreparedStatement stmt =(PreparedStatement) conn.prepareStatement("SELECT idProdotto,ObbligoRicetta,CostoUnitario,Nome,Azienda,Descizione,Codice,ParoleChiave FROM prodotti where idProdotto = (?)");
			Utility.setStatement(stmt,1, id);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			Prodotto p = getProductFromResultSet(rs);
			if(p.getObbligoRicetta())
				if(u.getRole().getRicettaEnabled())
					return null;
			return p;
		}catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	public Prodotto getProdottoByCodice(String codice, User u) {
		try {
			Connection conn = Connect.getConnection();
			PreparedStatement stmt =(PreparedStatement) conn.prepareStatement("SELECT idProdotto,ObbligoRicetta,CostoUnitario,Nome,Azienda,Descizione,Codice,ParoleChiave FROM prodotti where Codice = (?)");
			Utility.setStatement(stmt,1,codice);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			Prodotto p = getProductFromResultSet(rs);
			if(p.getObbligoRicetta())
				if(u.getRole().getRicettaEnabled())
					return null;
			return p;
		}catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	public Integer insertProdotto(Prodotto p) {
		try {
			return insertProdotto(p, Connect.getConnection());
		}catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	public Integer insertProdotto(Prodotto p,Connection conn) {
		try {
			String sql = "INSERT INTO prodotti(ObbligoRicetta,CostoUnitario,Nome,Azienda,Descizione,Codice)VALUES ((?),(?),(?),(?),(?),(?))";
			if(p.getParoleChiave() != null && !p.getParoleChiave().isEmpty()) {
				sql = "INSERT INTO prodotti(ObbligoRicetta,CostoUnitario,Nome,Azienda,Descizione,Codice,ParoleChiave)VALUES ((?),(?),(?),(?),(?),(?),(?))";
			}
			 
			PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			Utility.setStatement(stmt,1, p.getObbligoRicetta()); 
			Utility.setStatement(stmt,2,p.getCostoUnitario());
			Utility.setStatement(stmt,3,p.getNome());
			Utility.setStatement(stmt,4,p.getAzienda());
			Utility.setStatement(stmt,5,p.getDescizione());
			Utility.setStatement(stmt,6,p.getCodice());
			if(p.getParoleChiave() != null && !p.getParoleChiave().isEmpty()) {
				System.out.println("Qui");
				String s = String.join("-", p.getParoleChiave());
				Utility.setStatement(stmt,7,s);
			}
			stmt.execute();
			ResultSet generatedKey = stmt.getGeneratedKeys();
			generatedKey.next();
			return generatedKey.getInt(1);
		}catch(Exception e) {
			System.out.println(e.getCause());
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
}
