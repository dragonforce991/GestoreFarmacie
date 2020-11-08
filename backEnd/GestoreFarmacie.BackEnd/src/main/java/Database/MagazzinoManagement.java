package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import Model.Magazzino;
import Model.Prodotto;
import RestServices.Utility;

public class MagazzinoManagement {

	public ArrayList<Magazzino> getMagazzino(String idFarmacia) {
		try {
			ArrayList<Magazzino> magList = new ArrayList<Magazzino>();
			PreparedStatement stmt = (PreparedStatement) Connect.getConnection().prepareStatement("SELECT magazzino.Id, magazzino.IdFarmacia,magazzino.quantita,\r\n"
					+ "prodotti.*\r\n"
					+ "FROM magazzino, prodotti WHERE IdFarmacia = (?) "
					+ "AND magazzino.IdProdotto = prodotti.idProdotto");
			Utility.setStatement(stmt,1, idFarmacia);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				magList.add(getFullMagazzinoFromResultSet(rs));
			}
			return magList;
		}catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	
	
	public Magazzino insertSingleProduct(String IdProdotto, String IdFarmacia, Integer quantita,Connection conn) {
		try {
			Magazzino m;

			PreparedStatement stmt = (PreparedStatement) conn.prepareStatement("SELECT Id, IdProdotto, IdFarmacia,quantita FROM magazzino WHERE IdFarmacia = (?) AND IdProdotto = (?)");
			Utility.setStatement(stmt,1,IdFarmacia);
			Utility.setStatement(stmt,2, IdProdotto);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				m = getMagazzinoFromResultSet(rs);
			}else {
				m = new Magazzino("",0,IdProdotto,IdFarmacia);
			}
			m.setQuantita(m.getQuantita()+quantita);
			
			//"UPSERT" statement
			stmt = (PreparedStatement) conn.prepareStatement("INSERT INTO gestfarma.magazzino(IdProdotto,IdFarmacia,quantita)VALUES((?),(?),(?))  ON DUPLICATE KEY UPDATE quantita = (?)",Statement.RETURN_GENERATED_KEYS);
			Utility.setStatement(stmt,1, IdProdotto);
			Utility.setStatement(stmt,2, IdFarmacia);
			Utility.setStatement(stmt,3, m.getQuantita());
			Utility.setStatement(stmt,4, m.getQuantita());
			stmt.execute();
			rs = stmt.getGeneratedKeys();
			if(rs.next())
				m.setId(rs.getString(1));
			return m;
		
		}catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	public Magazzino insertSingleProduct(String IdProdotto, String IdFarmacia, Integer quantita) {
		try{
			return insertSingleProduct(IdProdotto, IdFarmacia, quantita, Connect.getConnection());
		}catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	public Boolean insertMagazzino(ArrayList<Magazzino> prodotti){
		try {
			return insertMagazzino(prodotti, Connect.getConnection());
		}catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public Boolean insertMagazzino(ArrayList<Magazzino> prodotti, Connection Conn) {
		try {
			HashMap<String,Magazzino> fromDb = new HashMap<String,Magazzino>();
			HashMap<String,Magazzino> toUpsert = new HashMap<String,Magazzino>();
			String IdFarmacia = prodotti.get(0).getIdFarmacia();
			for(Magazzino m : prodotti )
				if((m.getIdFarmacia().isBlank() || m.getIdProdotto().isBlank()) && IdFarmacia != m.getIdFarmacia() )
					return null;
				else {
					IdFarmacia = m.getIdFarmacia();
					toUpsert.put(m.getIdProdotto(), m);
				}
			
			ArrayList<Magazzino> magList = getMagazzino(IdFarmacia);
			for(Magazzino m: magList) {
				fromDb.put(m.getIdProdotto(), m);
			}
			
			for(String key : toUpsert.keySet()) {
				if(fromDb.containsKey(key)) {
					Magazzino m = fromDb.get(key);
					m.setQuantita(m.getQuantita() + toUpsert.get(key).getQuantita());
					toUpsert.put(key,m);
				}
			}
			String sqlClause = "INSERT INTO gestfarma.magazzino(IdProdotto,IdFarmacia,quantita)VALUES((?),(?),(?))  ON DUPLICATE KEY UPDATE quantita = (?)";
			PreparedStatement stmt = (PreparedStatement) Conn.prepareStatement(sqlClause);
			int count = 0;
			for(String key:toUpsert.keySet()) { 
				Magazzino m = toUpsert.get(key);
				Utility.setStatement(stmt,1, m.getIdProdotto());
				Utility.setStatement(stmt,2, m.getIdFarmacia());
				Utility.setStatement(stmt,3, m.getQuantita());
				Utility.setStatement(stmt,4, m.getQuantita());
				stmt.addBatch();
				count++;
				if(count % 100 == 0 || count == toUpsert.size())
					stmt.executeBatch();
			}
			
			return true;
		}catch(Exception e) {
			
			try {
				Conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			System.out.println(e);
			return false;
		}
	}
	
	private Magazzino getMagazzinoFromResultSet(ResultSet rs) throws SQLException {
		Magazzino m = new Magazzino();
		m.setIdProdotto(rs.getString("idProdotto"));
		m.setIdFarmacia(rs.getString("IdFarmacia"));
		m.setId(rs.getString("Id"));
		m.setQuantita(rs.getInt("quantita"));
		
		return m;
	}
	private Magazzino getFullMagazzinoFromResultSet(ResultSet rs) throws SQLException {
		Magazzino m = new Magazzino();
		m.setIdProdotto(rs.getString("prodotti.idProdotto"));
		m.setIdFarmacia(rs.getString("magazzino.IdFarmacia"));
		m.setId(rs.getString("magazzino.Id"));
		m.setQuantita(rs.getInt("magazzino.quantita"));
		Prodotto p = new Prodotto();
		p.setAzienda(rs.getString("prodotti.Azienda"));
		p.setCodice(rs.getString("prodotti.Codice"));
		p.setCostoUnitario(rs.getFloat("prodotti.CostoUnitario"));
		p.setDescizione(rs.getString("prodotti.Descizione"));
		p.setIdProdotto(rs.getString("prodotti.idProdotto"));
		p.setNome(rs.getString("prodotti.Nome"));
		p.setObbligoRicetta(rs.getBoolean("prodotti.ObbligoRicetta"));
		//p.setParoleChiave(rs.getString(""));
		m.setProduct(p);
		return m;
	}
}
